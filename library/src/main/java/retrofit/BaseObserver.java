package retrofit;
import android.content.Context;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import base.BaseView;
import base.MyApplication;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import utils.LoadingDialogUtil;
import utils.SystemUtils;
import utils.ToastUtil;

/**
 * @author admin
 * @explain 封装Observer
 * @time 2018/4/3 18:05
 */

public class  BaseObserver<T> implements Observer<T> {
    private BaseView mBaseView;
    private Context mContext = null;
    private Disposable mDisposable;

    /**
     * 普通的网络请求，自己处理请求完成或者请求失败的处理
     */
    public BaseObserver(){

    }

    /**
     *   封装好的网络请求自动处理请求完成或者请求失败的处理
     * @param view
     */
    public BaseObserver(BaseView view){
        this.mBaseView = view;
    }

    /**
     * 封装好的网络请求自动处理请求完成或者请求失败的处理
     * @param isShowLoading 是否显示loading
     * @param context
     */
    public BaseObserver(boolean isShowLoading,Context context){
        this.mContext = context;
        if(mContext != null&&isShowLoading)
            LoadingDialogUtil.showLoading(mContext,true);
    }

    /**
     *   封装好的网络请求自动处理请求完成或者请求失败的处理
     *   默认是可以取消弹框
     * @param
     */
    public BaseObserver(Context context){
        this.mContext = context;
         if(mContext != null)
             LoadingDialogUtil.showLoading(mContext,true);
    }
    /**
     *   封装好的网络请求自动处理请求完成或者请求失败的处理
     *   多用提交数据，自定义的弹出对话框 false 是不可取消 true 是可以取消
     * @param
     */
    public BaseObserver(Context context,boolean isCanceable){
        this.mContext = context;
         if(mContext != null)
         LoadingDialogUtil.showLoading(mContext,isCanceable);
    }
    @Override
    public void onSubscribe(Disposable d) {
           this.mDisposable = d;
    }





    @Override
    public void onNext(T value) {
        if(mBaseView != null)
            mBaseView.showSuccess();
    }

    /**
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {

        if(!SystemUtils.isNetworkAvailable(MyApplication.getContext())){
            ToastUtil.showShortToast("网络异常,请检查网络");
            if(mBaseView != null)
                mBaseView.showNoNetWork();
        }else{
            if(e instanceof TimeoutException || e instanceof SocketTimeoutException){
                ToastUtil.showShortToast("连接超时");
                if(mBaseView != null && mBaseView.getDataSize() == 0)
                    mBaseView.showOverTime();
            }else {
             //   ToastUtil.showShortToast("请求失败");
                if(mBaseView != null && mBaseView.getDataSize() == 0)
                    mBaseView.showLoadingFail();
            }
        }
        onComplete();
    }

    @Override
    public void onComplete() {
        if(mContext != null)
            LoadingDialogUtil.closeDialog();
    }


    public void onCancel(){

        if(mDisposable != null && !mDisposable.isDisposed()){
            mDisposable.dispose();
        }

    }

}
