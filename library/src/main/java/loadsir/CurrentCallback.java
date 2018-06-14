package loadsir;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingja.loadsir.callback.Callback;
import com.scwang.smartrefresh.layout.R;

/**
 * author : daiwenbo
 * e-mail : daiwwenb@163.com
 * date   : 2017/11/17
 * description   : 各个状态页的基础类
 */

public abstract class CurrentCallback extends Callback {
    private ImageView imageView;
    protected Context context;

    @Override
    protected int onCreateView() {
        return R.layout.base_state_layout;
    }

    @Override
    public void onAttach(Context context, View view) {
        this.context=context;
        imageView = view.findViewById(R.id.state_image);
        TextView tv_text = view.findViewById(R.id.tv_text);
        TextView refresh = view.findViewById(R.id.refresh);
        setImage(imageView);
        setText(tv_text);
    }

    abstract void setImage(ImageView imageView);
    abstract void setText(TextView tv_text);
}
