package loadsir;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.R;

/**
 * author : daiwenbo
 * e-mail : daiwwenb@163.com
 * date   : 2017/11/17
 * description   : 加载失败页面
 */

public class LoadFailCallback extends CurrentCallback {

    @Override
    void setImage(ImageView imageView) {
        Glide.with(context)
                .load(R.drawable.load_fail)
                .into(imageView);
    }

    @Override
    void setText(TextView tv_text) {
        tv_text.setText(context.getString(R.string.no_load_file_page));
    }
}
