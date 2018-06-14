package retrofit;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSource;
import okio.Okio;
import utils.TimeDataUtils;

import static retrofit.DesPlus.bjAY2008;
import static retrofit.HttpSharedUtils.HTTP;

/**
 * author： admin
 * date： 2018/4/24
 * describe：
 */
public class RxJavaUtils {

    public static  void rejava(final Interceptor.Chain chain){

        Observable.create(new ObservableOnSubscribe<Interceptor.Chain>() {
            @Override
            public void subscribe(ObservableEmitter<Interceptor.Chain> e) throws Exception {
                 e.onNext(chain);
            }
        }).map(new Function<Interceptor.Chain, CustomHttpEntity>() {
            @Override
            public CustomHttpEntity apply(Interceptor.Chain chain) throws Exception {
                CustomHttpEntity mEntity = new CustomHttpEntity();
                mEntity.setStartTime(TimeDataUtils.getYearMonth(new Date(),"yyyy HH:mm:ss:SSS"));
                mEntity.setSaveTime(new Date().getTime());
                Request request = chain.request();
                String userData = request.header("userData");

                if (!TextUtils.isEmpty(userData)) {
                    byte[] bytes2 = android.util.Base64.decode(userData.getBytes(), Base64.DEFAULT);
                    String str = null;
                    try {
                        str = new String(bytes2, "utf-8");
                        mEntity.setUserDataHeader(str);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                String apiAuth = request.header("api_auth");
                mEntity.setApiAuthHeader(DesPlus.decode(bjAY2008,apiAuth) );

                HttpUrl url = request.url();
                mEntity.setUrl(url.toString());
                String method = request.method();
                mEntity.setMethod(method);

                if("POST".equals(method)){
                  String string =  url.toString().substring(0,url.toString().indexOf("?")+1);
                    mEntity.setUrl(string + url.query());
                }

              try {
                Response response = chain.proceed(request);
                BufferedSource source = response.body().source();
                BufferedSource buffer = Okio.buffer(source);
                String message = response.message();
                if("OK".equals(message)){
                    mEntity.setOk(true);
                }else{
                    mEntity.setOk(false);
                }
                String s = buffer.readUtf8();
                byte[] bytes = s.getBytes();
                mEntity.setSize(bytes.length/1024+"KB");
                mEntity.setResponse(s);
                mEntity.setEndTime(TimeDataUtils.getYearMonth(new Date(),"yyyy HH:mm:ss:SSS"));
                }catch (Exception e){
                  mEntity.setResponse(e.toString());
                  mEntity.setEndTime(TimeDataUtils.getYearMonth(new Date(),"yyyy HH:mm:ss:SSS"));

              }
                return mEntity;
            }
        })  .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<CustomHttpEntity>() {
            @Override
            public void accept(CustomHttpEntity customHttpEntity) throws Exception {
                   List<CustomHttpEntity> dataList = HttpSharedUtils.getDataList(HTTP,CustomHttpEntity.class);
                   dataList.add(customHttpEntity);
                   HttpSharedUtils.setDataList(HTTP,dataList);
            }
        });

    }

}
