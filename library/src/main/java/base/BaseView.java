package base;

/**
 * base 的一些方法
 */

public interface BaseView {

    /**加载中*/
    void showLoading();
    /**加载成功*/
    void showSuccess();
    /**加载失败*/
    void showLoadingFail();
    /**没有网络*/
    void showNoNetWork();
    /**请求超时*/
    void showOverTime();
    /**没有数据*/
    void showEmpty();
    /**刷新数据*/
    void onRefreshData();

    int getDataSize();
}
