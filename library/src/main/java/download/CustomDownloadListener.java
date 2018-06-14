package download;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import java.io.File;
import java.text.DecimalFormat;

import utils.LogUtils;
import utils.SystemUtils;
import utils.ToastUtil;

/**
 * 下载回调工具类
 */
public class CustomDownloadListener implements DownloadListener {

    private int mProgress = 0;
    private Context mContext = null;
    private ProgressDialog progressDialog;
    private DecimalFormat mDeciFormat;

    public CustomDownloadListener(Context context) {
        this.mContext = context;
    }

    @Override
    public void onStartDownload() {


    }


    public int getProgress() {
        return mProgress;
    }

    @Override
    public void onFinishDownload(File filepath) {
        SystemUtils.installApk(filepath,mContext);
    }

    @Override
    public void onFail(String errorInfo) {
        if (progressDialog != null && progressDialog.isShowing()) {
            ToastUtil.showShortToast(errorInfo);
            progressDialog.dismiss();
        }
    }

    @Override
    public void onProgress(int i, final long bytesWritten, final long totalSize) {
        this.mProgress = i;
        LogUtils.e(i+"");
        ((FragmentActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(mContext);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
                    progressDialog.setTitle("下载新版本");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.setMax((int) totalSize / 1024 / 1024);
                    progressDialog.show();
                    mDeciFormat = new DecimalFormat("######0.00");
                }
                String format = mDeciFormat.format(((double) bytesWritten) / 1024 / 1024);
                String totalSizeFormat = mDeciFormat.format(((double) totalSize) / 1024 / 1024);
                progressDialog.setProgress((int) bytesWritten / 1024 / 1024);
                progressDialog.setProgressNumberFormat(format + "/" + totalSizeFormat + "M");
            }
        });


    }
}
