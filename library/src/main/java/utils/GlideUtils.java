package utils;

import android.content.Context;
import android.widget.ImageView;

import base.GlideApp;

/**
 * author： admin
 * date： 2018/3/29
 * describe：
 */

public class GlideUtils {

    /**
     * 正常加载图片
     *
     * @param context
     * @param iv       ImageView
     * @param url      图片地址
     * @param emptyImg 默认占位图
     */
    public static void displayImage(Context context, ImageView iv, String url, int emptyImg) {

        GlideApp.with(context)
                .load(url)
                .placeholder(emptyImg)
                .into(iv);
    }
    /**
     * 正常加载图片
     * @param context
     * @param iv       ImageView
     * @param url      图片地址
     */
    public static void displayImage(Context context, ImageView iv, Object url) {
        GlideApp.with(context)
                .load(url)
                .into(iv);
    }
}
