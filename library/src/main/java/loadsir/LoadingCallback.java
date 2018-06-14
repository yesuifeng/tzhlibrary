package loadsir;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingja.loadsir.callback.Callback;
import com.scwang.smartrefresh.layout.R;

import utils.ImageLoaderUtils;

/**
 * author : daiwenbo
 * e-mail : daiwwenb@163.com
 * date   : 2017/11/17
 * description   : 加载中页面
 */

public class LoadingCallback extends Callback {
    private ImageView imageView;
    @Override
    protected int onCreateView() {
        return R.layout.loading_layout;
    }
    @Override
    public void onAttach(Context context, View view) {
        imageView = view.findViewById(R.id.im_loading);
        TextView tv_text = view.findViewById(R.id.tv_text);
        tv_text.setVisibility(View.GONE);
        ImageLoaderUtils.showGIF(imageView,R.drawable.gif_loading);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (imageView != null) {
            imageView.clearAnimation();
        }
    }
}
