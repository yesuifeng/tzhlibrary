package base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.kingja.loadsir.core.Transport;
import com.scwang.smartrefresh.layout.BuildConfig;
import com.scwang.smartrefresh.layout.R;
import com.squareup.leakcanary.RefWatcher;

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

/**
 * author： admin
 * date： 2018/4/9
 * describe：
 */

public abstract class BaseFragment<V extends BaseView,P extends BasePresenter<V>> extends Fragment implements BaseView {

    //引用V层和P层
    private P presenter;
    private V view;
    private LoadService mBaseLoadService;
    protected RelativeLayout mTitleRoot;
    protected ImageView mTitleBack;
    protected TextView mTitleTv, mTitleRight, mTitleRight2;
    protected View mRootView;

    public P getPresenter() {
        return presenter;
    }

    protected ImmersionBar mImmersionBar;
    protected FragmentActivity mActivity = null;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBus.getDefault().register(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mActivity = getActivity();
            mRootView = inflater.inflate(getLayoutId(), container, false);
            unbinder = ButterKnife.bind(this, mRootView);

            if (presenter == null) {
                presenter = createPresenter();
            }
            if (view == null) {
                view = createView();
            }
            if (presenter != null && view != null) {
                presenter.attachView(view);
            }
            init(mRootView);

        }
        return mRootView;

    }

    //由子类指定具体类型
    public abstract int getLayoutId();

    public abstract P createPresenter();

    public abstract V createView();

    public abstract void init(View view);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBus.getDefault().unregister(this);
        }

        RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
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
     * 自定义空页面图片和文字
     *
     * @param image
     * @param string
     */
    protected void intCustomCallBack(final int image, final String string) {
        if (null!=mBaseLoadService)
        mBaseLoadService.setCallBack(EmptyCallback.class, new Transport() {
            @Override
            public void order(Context context, View view) {

                ImageView imageView = view.findViewById(R.id.state_image);
                Glide.with(context)
                        .load(image)
                        .into(imageView);
                TextView mText = view.findViewById(R.id.tv_text);
                mText.setText(string);
            }
        });

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


    public void initTitle(View view, String title) {
        mTitleRoot = view.findViewById(R.id.title_root);
        mTitleBack = view.findViewById(R.id.title_back);
        mTitleTv = view.findViewById(R.id.title_content);
        mTitleRight = view.findViewById(R.id.title_right);
        mTitleRight2 = view.findViewById(R.id.title_right2);
        mTitleTv.setText(title);
        initImmersionBar(mTitleRoot);
        if (BuildConfig.DEBUG)
            mTitleRoot.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    MonitoringPopUtils.showMonitoringPop(mActivity, mTitleRight);
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
    public void onDetach() {
        mActivity = null;
        unbinder.unbind();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
        super.onDetach();
    }

    @Override
    public int getDataSize() {
        return 0;
    }
}