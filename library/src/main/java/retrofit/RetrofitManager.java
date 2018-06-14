package retrofit;

import android.text.TextUtils;
import android.util.Base64;

import com.scwang.smartrefresh.layout.BuildConfig;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import utils.TimeDataUtils;

import static retrofit.DesPlus.bjAY2008;
import static retrofit.HttpSharedUtils.HTTP;

/**
 * 网络对象层
 */
public class RetrofitManager {
    //读超时长，单位：毫秒
    public static final int READ_TIME_OUT = 10;
    //连接时长，单位：毫秒
    public static final int CONNECT_TIME_OUT = 20;
    /**
     * 使用OkHttp配置了超时
     * @param baseUrl
     * @return retrofit
     */
    public Retrofit getRetrofit(String baseUrl) {
        //添加请求头
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                if (original != null) {
                    Request request = original.newBuilder()
                            .header("userData", Base64.encodeToString(DesPlus.getDefaultHeaderValue().getBytes(), Base64.NO_WRAP))
                            .header("api_auth", DesPlus.getStringTime())
                            .header("Content-Encoding", "utf-8")
                            .method(original.method(), original.body())
                            .build();
                    Response proceed = null;
                    proceed = chain.proceed(request);
                    return proceed;
                }
                return null;

            }
        };

        //创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .retryOnConnectionFailure(true)
                .addInterceptor(headerInterceptor)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)//超时时间
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(READ_TIME_OUT, TimeUnit.SECONDS);
             if(BuildConfig.DEBUG){
                 builder.addInterceptor(new HttpLogger());
             }
        OkHttpClient okHttpClient = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())//请求结果转换为基本类型，一般为String
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//适配RxJava2.0,
                // RxJava1.x则为RxJavaCallAdapterFactory.create()
                .build();
        return retrofit;

    }

    private static final Charset UTF8 = null;
    private class HttpLogger implements Interceptor {

        private Response response;

        @Override
        public synchronized Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            CustomHttpEntity mEntity = new CustomHttpEntity();
            mEntity.setStartTime(TimeDataUtils.getYearMonth(new Date(), "yyyy HH:mm:ss:SSS"));
            mEntity.setSaveTime(new Date().getTime());
            String userData = request.header("userData");
            try {
                if (!TextUtils.isEmpty(userData)) {
                    byte[] bytes2 = android.util.Base64.decode(userData.getBytes(), Base64.DEFAULT);
                    String str = null;
                    str = new String(bytes2, "utf-8");
                    mEntity.setUserDataHeader(str);
                }
                String apiAuth = request.header("api_auth");
                mEntity.setApiAuthHeader(DesPlus.decode(bjAY2008, apiAuth));
                HttpUrl url = request.url();
                String method = request.method();
                mEntity.setMethod(method);
                String string = url.toString();
               if(url.toString().contains("?")){
                   string = url.toString().substring(0, url.toString().indexOf("?") + 1);
                   mEntity.setUrl(string + url.query());
               }else{
                   mEntity.setUrl(string);
               }



                response = chain.proceed(request);
                ResponseBody responseBody = response.body();
                BufferedSource source = response.body().source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                String message = response.message();
                if ("OK".equals(message) || "".equals(message)) {
                    mEntity.setOk(true);
                } else {
                    mEntity.setOk(false);
                }
                String s = buffer.clone().readString(charset);
                byte[] bytes = s.getBytes();
                mEntity.setSize(bytes.length / 1024 + "KB");
                mEntity.setResponse(s);
                mEntity.setEndTime(TimeDataUtils.getYearMonth(new Date(), "yyyy HH:mm:ss:SSS"));
            } catch (Exception e) {
                mEntity.setResponse(e.toString());
                mEntity.setEndTime(TimeDataUtils.getYearMonth(new Date(), "yyyy HH:mm:ss:SSS"));

                /**
                 * 保存数据
                 */
                List<CustomHttpEntity> dataList = HttpSharedUtils.getDataList(HTTP, CustomHttpEntity.class);
                dataList.add(mEntity);
                HttpSharedUtils.setDataList(HTTP, dataList);
                return response;

            }

            /**
             * 保存数据
             */
            List<CustomHttpEntity> dataList = HttpSharedUtils.getDataList(HTTP, CustomHttpEntity.class);
            dataList.add(mEntity);
            HttpSharedUtils.setDataList(HTTP, dataList);
            return response;
        }

    }

}