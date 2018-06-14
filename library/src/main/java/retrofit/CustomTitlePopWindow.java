package retrofit;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.R;


/**
 * author： admin
 * date： 2018/2/9
 * describe：
 */

public class CustomTitlePopWindow extends PopupWindow implements AdapterView.OnItemClickListener {

    private String[] strings = new String[]{};
    private Context mContext;
    private final int itemWhiteColor,itemBlackColor;
    private OnItemClick mOnItemClick = null;
    public CustomTitlePopWindow(Context context, String[] mstrs){
        this.mContext = context;
        this.strings = mstrs;
        itemWhiteColor = context.getResources().getColor(R.color.col_fff);
        itemBlackColor = context.getResources().getColor(R.color.col_ff351d);
        initView(context);

    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);

    }

    @Override
    public void dismiss() {
        super.dismiss();
        changeActivitys((Activity) mContext,1f);
    }

    private void initView(Context context) {
       View rootView = LayoutInflater.from(context).inflate(R.layout.pop_myorders_selector_layout,null);
        ListView mList = rootView.findViewById(R.id.lv_pop);
        MyPopListAdapter mPopListAdapter = new MyPopListAdapter();
        mList.setAdapter(mPopListAdapter);
        mList.setOnItemClickListener(this);
        setContentView(rootView);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        changeActivitys((Activity) mContext,0.7f);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(null != mOnItemClick){
            mOnItemClick.OnItemClickListener(i);
        }
    }

    public void setOnPopItemClickListener(OnItemClick onItemClick){
        this.mOnItemClick = onItemClick;
    }


    public interface OnItemClick{

        void OnItemClickListener(int postion);

    }


    public class  MyPopListAdapter extends BaseAdapter {

      @Override
      public int getCount() {
          return strings.length;
      }

      @Override
      public Object getItem(int i) {
          return null;
      }

      @Override
      public long getItemId(int i) {
          return 0;
      }

      @Override
      public View getView(int i, View view, ViewGroup viewGroup) {
          TextView textView = new TextView(mContext);
          textView.setText(strings[i]);
          textView.setBackgroundColor(itemWhiteColor);
          textView.setGravity(Gravity.CENTER);
          textView.setTextSize(14);
          textView.setTextColor(itemBlackColor);
          textView.setPadding(0,25,0,25);
          return textView;
      }
  }

    /**
     * 回复屏幕
     */
    public static void changeActivitys(Activity con, float alpha) {
        WindowManager.LayoutParams params = con.getWindow().getAttributes();
        params.alpha = alpha;
        con.getWindow().setAttributes(params);
    }


}
