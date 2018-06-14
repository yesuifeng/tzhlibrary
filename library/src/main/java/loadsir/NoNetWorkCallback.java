package loadsir;

import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.R;


/**
 * author : daiwenbo
 * e-mail : daiwwenb@163.com
 * date   : 2018/2/11
 * description   : 无网络页面
 */

public class NoNetWorkCallback extends CurrentCallback {

    @Override
    void setImage(ImageView imageView) {
        imageView.setImageResource(R.drawable.no_network);
    }

    @Override
    void setText(TextView tv_text) {
        tv_text.setText(context.getString(R.string.no_network_page));
    }
}
