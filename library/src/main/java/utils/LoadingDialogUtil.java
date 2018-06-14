package utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.R;


/**
 * 统计加载框
 */

public class LoadingDialogUtil {
    private static Dialog loadingDialog;


    /**
     * 默认的点击返回键可以取消
     * @return
     */
    public static Dialog showLoading(Context context) {
        return createLoadingDialog(context,"加载中...",true);
    }

    /**
     * 点击返回键不可取消，多用于上传操作
     * @return
     */
    public static Dialog showLoading(Context context,boolean isCanceable) {
        return createLoadingDialog(context,"加载中...",isCanceable);
    }
    public static Dialog createLoadingDialog(Context context, String msg,Boolean isCancelable) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局
        ImageView imageView = v.findViewById(R.id.iv_loading);// 提示文字
        ImageLoaderUtils.showGIF(imageView, R.drawable.gif_loading);
        if ( null != loadingDialog &&   loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
         loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局


        if (!isCancelable)//点击返回键不取消
            loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });


        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        loadingDialog.show();

        return loadingDialog;
    }

    /**
     * 关闭dialog
     *
     * http://blog.csdn.net/qq_21376985
     *
     *
     */
    public static void closeDialog() {
        if ( null != loadingDialog &&   loadingDialog.isShowing()) {
             loadingDialog.dismiss();
             loadingDialog = null;
        }
    }
}
