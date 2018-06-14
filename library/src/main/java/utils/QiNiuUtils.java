package utils;

import android.text.TextUtils;

import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import base.BaseEventMsg;

/**
 * author： admin
 * date： 2018/4/12
 * describe：七牛上传工具类
 */
public class QiNiuUtils {

    private static QiNiuUtils mQiNiuUtils = null;
    private boolean isSuccend = true;//默认是上传图片成功，防止重复执行
    public static final String IMGSUSSEND = "upimageSussend";//如果是成功
    public static final String IMGFAILUE = "upimageFailue";//如果是失败
    private static UploadManager uploadManager;

    public static QiNiuUtils getInstance() {
        synchronized (QiNiuUtils.class) {
            if (mQiNiuUtils == null) {
                mQiNiuUtils = new QiNiuUtils();
                init();
            }
        }
        return mQiNiuUtils;
    }

    public static void init() {
        Configuration config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
                .recorder(null)           // recorder分片上传时，已上传片记录器。默认null
                .recorder(null, null)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
// 重用uploadManager。一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);

    }

    public static String getQiNinPath() {
        return "az" + createTime() + "_" + "" + getMyRandom() + ".jpg";
    }

    public static String createTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        return filename;
    }

    /**
     * 随机生成两位数
     *
     * @return
     */
    public static int getMyRandom() {
        Random r = new Random();
        //	ToastUtils.showCenter(r.nextInt(89)+10+"");
        return r.nextInt(89) + 10;
    }

    /**
     * 图片地址集合，上传图片token
     *
     * @param imagePaths
     * @param token
     */
    public void upImages(final List<String> imagePaths, String token) {
        if (imagePaths == null || imagePaths.size() == 0) {
            return;
        }
        isSuccend = true;
        final List<String> succendList = new ArrayList<>();
        for (int i = 0; i < imagePaths.size(); i++) {
            String url = imagePaths.get(i);
            if (!url.startsWith("http")) {//如果有url
                uploadManager.put(url, i + "_" + getQiNinPath(), token, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.isOK()) {
                            //上传成功
                            succendList.add(key);
                            upImageFinish(imagePaths, succendList);
                        } else {
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                            if (isSuccend) {
                                isSuccend = false;
                                EventBus.getDefault().post(new BaseEventMsg(IMGFAILUE, succendList));
                            }
                        }
                    }
                }, null);
            } else {
                succendList.add(url);
                upImageFinish(imagePaths, succendList);
            }
        }

    }


    public void upCarImages(final List<String> imagePaths, String token) {
        if (imagePaths == null || imagePaths.size() == 0) {
            return;
        }
        isSuccend = true;
        final List<String> succendList = new ArrayList<>();
        final List<String> tempList = new ArrayList<>();
        for (int i = 0; i < imagePaths.size(); i++) {
            succendList.add("");
            String url = imagePaths.get(i);
            if (TextUtils.isEmpty(url)) {
                tempList.add("");
            } else if (!url.startsWith("http")) { //如果有url
                uploadManager.put(url, i + "_" + getQiNinPath(), token, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.isOK()) {
                            //上传成功
                            String index = key.substring(0, key.indexOf("_"));
                            int pisition = Integer.parseInt(index);
                            succendList.set(pisition, key);
                            tempList.add("");
                            if (tempList.size() == imagePaths.size()) {

                                EventBus.getDefault().post(new BaseEventMsg(IMGSUSSEND, succendList));
                            }
                        } else {
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                            if (isSuccend) {
                                isSuccend = false;
                                EventBus.getDefault().post(new BaseEventMsg(IMGFAILUE, succendList));
                            }
                        }
                    }
                }, null);
            } else {
                tempList.add("");
                succendList.add(url);
                upImageFinish(imagePaths, succendList);
            }
        }

    }

    /**
     * 判断是否是最后一张图片
     *
     * @param imagePaths
     * @param succendList
     */
    private void upImageFinish(List<String> imagePaths, List<String> succendList) {
        if (succendList.size() == imagePaths.size()) {
            EventBus.getDefault().post(new BaseEventMsg(IMGSUSSEND, succendList));
        }
    }
}
