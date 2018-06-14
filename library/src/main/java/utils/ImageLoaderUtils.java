package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.scwang.smartrefresh.layout.R;

import java.io.File;
import java.util.concurrent.ExecutionException;

import base.GlideApp;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * @author liuml
 * @explain
 * @time 2017/11/2 16:09
 */

public class ImageLoaderUtils {

    /**
     * 在一个imageView里面异步展示一个图片
     *
     * @param uri
     * @param imageView
     */
    public static void displayImage(String uri, ImageView imageView) {
        uri=replace(uri);
        GlideApp.with(imageView.getContext())
                .load(uri)
                .placeholder(R.drawable.def_list_icon)
                .error(R.drawable.def_list_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // 设置缓存的策略
                .into(imageView);
    }

    private static String replace(String uri) {
        if (TextUtils.isEmpty(uri)){
            return null;
        }
        StringBuffer stringBuffer=new StringBuffer(uri);
        if (uri.startsWith("https")){
            stringBuffer.replace(0,5,"http");
        }
        return stringBuffer.toString();
    }

    public static void displayImage(String uri, ImageView imageView,int defaultResId) {
        if (TextUtils.isEmpty(uri))
            uri="";
        uri=replace(uri);
        GlideApp.with(imageView.getContext())
                .load(uri)
                .placeholder(defaultResId)
                .error(defaultResId)
//                .dontAnimate() //不要动画
                .diskCacheStrategy(DiskCacheStrategy.ALL) // 设置缓存的策略
                .into(imageView);
    }

    public static void show(ImageView mImageView, String imageUrl) {
        imageUrl=replace(imageUrl);
        GlideApp.with(mImageView.getContext()).load(imageUrl)
                .transition(withCrossFade())//淡入淡出效果
                .into(mImageView);
    }

    /**
     * 以拉伸模式加载网络图片
     */
    public static void show1(ImageView mImageView, String imageUrl) {
        imageUrl=replace(imageUrl);
        GlideApp.with(mImageView.getContext()).load(imageUrl)
                .fitCenter()
                .into(mImageView);  //crossFade是个淡入淡出效果
    }

    //加载本地图片 sd卡
    public static void displayImageLocalFile(String thumbnailPath, ImageView imageView) {
        thumbnailPath=replace(thumbnailPath);
        GlideApp.with(imageView.getContext()).load(thumbnailPath).into(imageView);
    }


    //加载本地GIF
    public static void showGIF(ImageView imageView, int img) {
        GlideApp.with(imageView.getContext()).asGif().load(img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    //加载网络GIF
    public static void showGIF(ImageView imageView, String img) {
        img=replace(img);
        GlideApp.with(imageView.getContext()).asGif().load(img)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * 跳过缓存. 每次都从服务端获取最新.
     * diskCacheStrategy: 磁盘缓存
     * skipMemoryCache:内存缓存
     */
    private void skipCache(String uri, ImageView imageView) {
        GlideApp.with(imageView.getContext())
                .asBitmap()
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }


    /**
     * 异步加载一个图片，监听加载过程，指定大小，在回调中取得位图
     * 可以用来加载大图。
     *
     * @param context
     * @param uri
     * @param listener
     */
    public static void loadImage(Context context, String uri, final getImageListener listener) {
        uri=replace(uri);
        GlideApp.with(context)
                .asBitmap()
                .load(uri)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        listener.onSuccess(bitmap);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        listener.onError(errorDrawable);
                    }
                });

    }
    public static void loadImage(Context context, String imageUrl, final ImageLoadingListener listener) {
        final String uri=replace(imageUrl);
        GlideApp.with(context)
                .asBitmap()
                .load(uri)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        listener.onSuccess(bitmap, uri);
                    }
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        listener.onError(uri);
                    }
                });

    }



    /**
     * 注意！！！该方法必须在子线程中执行
     * 清除硬盘缓存
     */
    public static void cleanDiskCache(final Context context) {
        Glide.get(context).clearDiskCache();
    }

    /**
     * 清除内存缓存
     */
    public static void cleanMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * 内存和硬盘双清
     */
    public static void cleanDoubleCache(Context context) {
        cleanDiskCache(context);
        cleanMemoryCache(context);
    }

    /**
     * 恢复请求，一般在停止滚动的时候调用
     *
     * @param context
     */
    public static void resumeRequests(Context context) {
        Glide.with(context).resumeRequests();
    }

    /**
     * 暂停请求，一般在滚动的时候调用
     *
     * @param context
     */
    public static void pauseRequests(Context context) {
        Glide.with(context).pauseRequests();
    }

    /**
     * 根据图片的网络地址，拿到使用 Glide 进行缓存后的图片缓存地址
     * 注意！！！ 该方法要在子线程中调用，否则会出错
     *
     * @param imageUrl 图片的网络地址
     * @return 图片的缓存地址
     */
    public static String getImagePathFromCache(String imageUrl, Context context) {

        FutureTarget<File> futureTarget = Glide.with(context)
                .load(imageUrl)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        File cacheFile;
        try {
            cacheFile = futureTarget.get();
            return cacheFile.getAbsolutePath();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用于监听异步加载图片的过程
     */
    public interface ImageLoadingListener {
        void onSuccess(Bitmap bitmap, String url);

        void onError(String url);
    }

    /**
     * 用于以及加载图片获取 Bitmap
     */
    public interface getImageListener {
        void onSuccess(Bitmap bitmap);

        void onError(Drawable drawable);
    }

}
