package loadsir;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.R;

/**
 * author : daiwenbo
 * e-mail : daiwwenb@163.com
 * date   : 2017/11/17
 * description   : 网络超时页面
 */
public class OverTimeCallback extends CurrentCallback {

    @Override
    void setImage(ImageView imageView) {
        Glide.with(context)
                .load(R.drawable.over_time_page)
                .into(imageView);
    }

    @Override
    void setText(TextView tv_text) {
        tv_text.setText(context.getString(R.string.no_over_time_page));
    }
}
