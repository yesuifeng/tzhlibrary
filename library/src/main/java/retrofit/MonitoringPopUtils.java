package retrofit;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.PopupWindow;

import utils.ToastUtil;

/**
 * author： admin
 * date： 2018/4/25
 * describe：监控相关
 */
public class MonitoringPopUtils {

    private static CustomTitlePopWindow customTitlePopWindow;

    public static void showMonitoringPop(final Context context, View view){
        if(customTitlePopWindow != null && customTitlePopWindow.isShowing())return;
        customTitlePopWindow = new CustomTitlePopWindow(context,new String[]{"网络监控","切换地址","Log日志","下载更新"});
        customTitlePopWindow.showAsDropDown(view);
        customTitlePopWindow.setOnPopItemClickListener(new CustomTitlePopWindow.OnItemClick() {
            @Override
            public void OnItemClickListener(int postion) {
                switch (postion){
                    case 0:
                        HttpRequestAndResponseActivity.startActivity(context,HttpSharedUtils.HTTP);
                        break;
                    case 1:
                        context.startActivity(new Intent(context,AddHostActivity.class));
                        break;
                    case 2:
                        HttpRequestAndResponseActivity.startActivity(context,HttpSharedUtils.LOG);
                        break;
                    case 3:
                        try {
                            Intent
                            intent = new Intent(context,Class.forName("com.guangan.merchant.ui.web.WebViewActivity"));
                            intent.putExtra("Url","https://www.pgyer.com/WNSCSH");
                            intent.putExtra("title","下载蜗牛市场商户");
                            context.startActivity(intent);
                        } catch (ClassNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        break;
                }
            }
        });
        customTitlePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                if(customTitlePopWindow != null)customTitlePopWindow = null;

            }
        });
    }


    public static void onLongClickCopy(Context context,String string){
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(string);
        ToastUtil.showShortToast("已复制到剪切板");
    }

}
