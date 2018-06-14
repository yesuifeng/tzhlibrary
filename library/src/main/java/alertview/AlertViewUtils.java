package alertview;

import android.content.Context;

/**
 * author： admin
 * date： 2018/4/11
 * describe：弹框Utils 可根据业务需求继续添加
 */

public class AlertViewUtils {



    /**
     *  弹出 双
     * @param context
     * @param msg
     * @param alertCallBack
     */
    public static void showAlertDouble(Context context, String msg,  OnItemClickListener alertCallBack){
       new AlertView("标题", msg, "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert,alertCallBack).setCancelable(true).show();

    }


    /**
     *  弹出 双 带标题
     * @param context
     * @param msg
     * @param alertCallBack
     */
    public static void showAlertDouble(Context context,String title, String msg,  OnItemClickListener alertCallBack){
       new AlertView(title, msg, "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert,alertCallBack).setCancelable(true).show();

    }

    /**
     * 弹出单
     * @param context
     */
    public static void showAlertSingle(Context context) {
        new AlertView("标题", "内容", null, new String[]{"确定"}, null, context, AlertView.Style.Alert, null).show();
    }

    /**
     * 底部弹出
     * @param context
     * @param onItemClickListener
     */
    public static void showAlertBottom(Context context,OnItemClickListener onItemClickListener) {
        new AlertView("上传图片", null, "取消", null,
                new String[]{"拍照", "从相册中选择"},
                context, AlertView.Style.ActionSheet, onItemClickListener).show();

    }



}
