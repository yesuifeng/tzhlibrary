package retrofit;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.scwang.smartrefresh.layout.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import alertview.AlertViewUtils;
import alertview.OnItemClickListener;
import base.BaseActivity;
import base.BaseEventMsg;
import base.BasePresenter;
import base.BaseView;
import utils.StringUtils;
import utils.ToastUtil;

import static retrofit.HttpSharedUtils.HOST;

/**
 * author： admin
 * date： 2018/4/25
 * describe：
 */
public class AddHostActivity extends BaseActivity implements View.OnClickListener {

    private EditText mHost;
    private RadioGroup mGroup;
    private List<String> dataList;

    @Override
    public int getLayoutId() {
        return R.layout.act_add_host_layout;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public BaseView createView() {
        return null;
    }

    @Override
    public void init() {
        initTitle("切换地址");
        dataList = HttpSharedUtils.getDataList(HOST,String.class);
        findViewById(R.id.tv_add).setOnClickListener(this);
        mHost = findViewById(R.id.et_host);
        mGroup = findViewById(R.id.radiogroup);
        setData();
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (!StringUtils.isEmpty(dataList.get(i))){
                        HttpSharedUtils.setSelectorHost(dataList.get(i));
                        EventBus.getDefault().post(new BaseEventMsg(HOST,dataList.get(i)));
                    }
            }
        });
    }

    private void setData() {
        mGroup.removeAllViews();
        for (int i = 0; i < dataList.size(); i++) {
            final RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i);
            radioButton.setText(dataList.get(i));
            if(HttpSharedUtils.getSelectorHost().equals(dataList.get(i))){
                radioButton.setChecked(true);
            }

            if(i>3){
                radioButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if(!radioButton.isChecked())
                        AlertViewUtils.showAlertDouble(AddHostActivity.this, "确认删除当前域名？", new OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int position) {
                                if(position==0){
                                    dataList.remove(radioButton.getId());
                                    HttpSharedUtils.setDataList(HOST,dataList);
                                    setData();
                                }
                            }
                        });

                        return true;
                    }
                });
            }


            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
            layoutParams.setMargins(0,0,0,2);
            mGroup.addView(radioButton,layoutParams);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_add) {
            if (StringUtils.isEmpty(mHost.getText().toString()) || !mHost.getText().toString().startsWith("http") ) {
                ToastUtil.showShortToast("请输入正确的地址");
            }else{
                dataList.add(mHost.getText().toString());
                HttpSharedUtils.setSelectorHost(mHost.getText().toString());
                setData();
                HttpSharedUtils.setDataList(HOST,dataList);
            }
        }
    }
}
