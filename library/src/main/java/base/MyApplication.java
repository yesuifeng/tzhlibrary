package base;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.kingja.loadsir.core.LoadSir;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.BuildConfig;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.umeng.commonsdk.UMConfigure;

import loadsir.EmptyCallback;
import loadsir.LoadFailCallback;
import loadsir.LoadingCallback;
import loadsir.NoNetWorkCallback;
import loadsir.OverTimeCallback;
import locservice.LocationService;
import locservice.LocationUtils;
import retrofit.CrashHandler;
import utils.QiNiuUtils;

/**
 * Application类 初始化各种配置
 */
public class MyApplication extends Application {

    public static Context mContext;//全局上下文对象
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        /**
         *初始化统一加载
         */
        initLoadSir();
        /**
         *初始化log
         */
        initLogger();

        /**
         *初始化友盟
         */
        UMConfigure.init(this, "5afe8d56f29d98254900006d", null, UMConfigure.DEVICE_TYPE_PHONE, null);
    //    CrashReport.initCrashReport(getApplicationContext(), "fb305f7762", false);
        /**
         *初始化定位
         */
        initLocationSdk();

        /**
         *初始化七牛
         */
        QiNiuUtils.getInstance();

        /**
         *初始化自定义错误统计
         */
        CrashHandler.getInstance().init(this);

        /**
         * 开启定位
         */
        LocationUtils.getInstance().startLocation(this);

        /**
         * 初始化热更新
         */
        initHotfix();

        refWatcher = setupLeakCanary();
    }



    private RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication leakApplication = (MyApplication) context.getApplicationContext();
        return leakApplication.refWatcher;
    }

    private void initHotfix() {
        setStrictMode();
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁
        Beta.canAutoDownloadPatch = true;
        // 设置是否提示用户重启
        Beta.canNotifyUserRestart = false;
        // 设置是否自动合成补丁
        Beta.canAutoPatch = true;
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId,调试时将第三个参数设置为true
        Bugly.init(this, "087a4d6abc", false);
    }

    public LocationService locationService;
    public Vibrator mVibrator;
    private void initLocationSdk() {
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
    }

    private void initLogger() {
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }

    private void initLoadSir() {
        LoadSir.beginBuilder()
                .addCallback(new LoadFailCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new OverTimeCallback())
                .addCallback(new NoNetWorkCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();

    }

    public static Context getContext() {
        return mContext;
    }

    @TargetApi(9)
    protected void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }


}
