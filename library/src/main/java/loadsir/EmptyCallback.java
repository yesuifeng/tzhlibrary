package loadsir;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.R;

/**
 * author : daiwenbo
 * e-mail : daiwwenb@163.com
 * date   : 2017/11/17
 * description   : 没有数据 状态页
 */

public class EmptyCallback  extends CurrentCallback{
    @Override
    void setImage(ImageView imageView) {
        Glide.with(context)
                .load(R.drawable.empty)
                .into(imageView);
    }

    @Override
    void setText(TextView tv_text) {
        tv_text.setText(context.getString(R.string.no_information_page));
    }


}
