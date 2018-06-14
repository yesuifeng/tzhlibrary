package banner.loader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import banner.BannerEntity;
import base.BaseEntity;
import utils.ImageLoaderUtils;

/**
 * author : daiwenbo
 * e-mail : daiwwenb@163.com
 * date   : 2018/2/11
 * description   : Branner图片加载器
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView,int defResId) {
        ImageLoaderUtils.displayImage(((BannerEntity) path).pic,imageView,defResId);
//        Glide.with(context).load(((BannerEntity) path).pic).into(imageView);
    }
}
