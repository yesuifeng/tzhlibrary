package base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.kingja.loadsir.core.Transport;
import com.scwang.smartrefresh.layout.BuildConfig;
import com.scwang.smartrefresh.layout.R;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import loadsir.EmptyCallback;
import loadsir.LoadFailCallback;
import loadsir.LoadingCallback;
import loadsir.NoNetWorkCallback;
import loadsir.OverTimeCallback;
import retrofit.MonitoringPopUtils;
import utils.BindEventBus;
import utils.MLeaks;

/**
 * author： admin
 * date： 2018/3/29
 * describe：
 */

public abstract class BaseActivity<V extends BaseView,P extends BasePresenter<V>> extends AppCompatActivity implements BaseView {

    //引用V层和P层
    private P presenter;
    private V view;
    private LoadService mBaseLoadService;
    protected RelativeLayout mTitleRoot;
    protected ImageView mTitleBack;
    protected TextView mTitleTv,mTitleRight,mTitleRight2;
    public P getPresenter() {
        return presenter;
    }
    protected ImmersionBar mImmersionBar;
    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);

        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBus.getDefault().register(this);
        }

        if (presenter == null) {
            presenter = createPresenter();
        }
        if (view == null) {
            view = createView();
        }
        if (presenter != null && view != null) {
            presenter.attachView(view);
        }
        init();
    }

    //由子类指定具体类型
    public abstract int getLayoutId();

    public abstract P createPresenter();

    public abstract V createView();

    public abstract void init();

    @Override
    protected void onDestroy() {
        MLeaks.fixFocusedViewLeak(getApplication());
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
        unbinder.unbind();
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBus.getDefault().unregister(this);
        }

        if (mImmersionBar != null){
            mImmersionBar.destroy();
            mImmersionBar = null;
        }
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);//1
        refWatcher.watch(this);

    }


    @Override
    public void showLoading() {
        if (null != mBaseLoadService) {
            mBaseLoadService.showCallback(LoadingCallback.class);
        }
    }


    @Override
    public void showSuccess() {
        if (null != mBaseLoadService)
            mBaseLoadService.showSuccess();
    }


    @Override
    public void showNoNetWork() {
        if (null != mBaseLoadService)
            mBaseLoadService.showCallback(NoNetWorkCallback.class);
    }


    @Override
    public void showLoadingFail() {
        if (null != mBaseLoadService)
            mBaseLoadService.showCallback(LoadFailCallback.class);
    }

    @Override
    public void showEmpty() {
        if (null != mBaseLoadService)
            mBaseLoadService.showCallback(EmptyCallback.class);
    }

    @Override
    public void showOverTime() {
        if (null != mBaseLoadService)
            mBaseLoadService.showCallback(OverTimeCallback.class);
    }

    @Override
    public void onRefreshData() {
        showLoading();
    }

    /**
     * 初始化 各种状态页(指定View)
     */
    protected void initCallbackView(Object context) {
        mBaseLoadService = LoadSir.getDefault().register(context, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
               onRefreshData();
            }
        });

    }

    /**
     * 自定义空页面图片和文字
     * @param image
     * @param string
     */
    protected void intCustomCallBack(final int image, final String string){
        if (null!=mBaseLoadService)
        mBaseLoadService.setCallBack(EmptyCallback.class, new Transport() {
            @Override
            public void order(Context context, View view) {

                ImageView imageView = view.findViewById(R.id.state_image);
                imageView.setImageResource(image);
                TextView mText = view.findViewById(R.id.tv_text);
                mText.setText(string);
            }
        });

    }


    public void initTitle(String title){
       initTitle(title,true);
    }

    public void initTitle(String title,boolean enableImmBar){
        mTitleRoot = findViewById(R.id.title_root);
        mTitleBack = findViewById(R.id.title_back);
        mTitleTv = findViewById(R.id.title_content);
        mTitleRight = findViewById(R.id.title_right);
        mTitleRight2 = findViewById(R.id.title_right2);
        mTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitleTv.setText(title);
        if (enableImmBar){
            initImmersionBar(mTitleRoot);
        }
        if(BuildConfig.DEBUG)
        mTitleRoot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MonitoringPopUtils.showMonitoringPop(BaseActivity.this,mTitleRight);
                return false;
            }
        });

    }


    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar(View mStatusView) {
        if (null == mStatusView) return;
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.titleBar(mStatusView)
                .statusBarDarkFont(true, 0.2f)
                .keyboardEnable(true)
                .init();//指定标题栏view

    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长
    }

    @Override
    public int getDataSize() {
        return 0;
    }

}