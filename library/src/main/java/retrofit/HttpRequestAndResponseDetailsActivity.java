package retrofit;

import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import base.BaseActivity;
import base.BasePresenter;
import base.BaseView;
import utils.BindEventBus;
import utils.StringUtils;

/**
 * author： admin
 * date： 2018/4/24
 * describe：
 */
@BindEventBus
public class HttpRequestAndResponseDetailsActivity extends BaseActivity implements View.OnLongClickListener {

    private CustomHttpEntity mEntity;

    @Override
    public int getLayoutId() {
        return R.layout.act_request_response_details_layout;
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
        initTitle("数据详情");
        TextView mContent = findViewById(R.id.tv_content);
        TextView mUrl = findViewById(R.id.tv_url);
        TextView mMethod= findViewById(R.id.tv_method);
        TextView mStatus = findViewById(R.id.tv_status);
        TextView mTimeStart = findViewById(R.id.tv_tiem_start);
        TextView mTimeEnd = findViewById(R.id.tv_time_end);
        TextView mHeader = findViewById(R.id.tv_header);
        TextView mHeader2 = findViewById(R.id.tv_header2);
        TextView mSize = findViewById(R.id.tv_size);

        mUrl.setOnLongClickListener(this);
        mContent.setOnLongClickListener(this);
        mHeader.setOnLongClickListener(this);
        mHeader2.setOnLongClickListener(this);
        mContent.setText(mEntity.getResponse());
        StringUtils.setCustomText("请求地址：    ", mEntity.getUrl(),mUrl,15,true);
        StringUtils.setCustomText("请求方式：    ", mEntity.getMethod(),mMethod,15,true);
        StringUtils.setCustomText("请求状态：    ", mEntity.isOk()?"请求成功":"请求失败",mStatus,15,true);
        StringUtils.setCustomText("请求时间：    ", mEntity.getStartTime(),mTimeStart,15,true);
        StringUtils.setCustomText("返回时间：    ", mEntity.getEndTime(),mTimeEnd,15,true);
        StringUtils.setCustomText("请求头userData：    ", mEntity.getUserDataHeader(),mHeader,15,true);
        StringUtils.setCustomText("请求头apiAuth：    ", mEntity.getApiAuthHeader(),mHeader2,15,true);
        StringUtils.setCustomText("数据大小：    ", mEntity.getSize(),mSize,15,true);
    }

    @Override
    public boolean onLongClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_content) {
            MonitoringPopUtils.onLongClickCopy(this,mEntity.getResponse());
        }else if(i==R.id.tv_url){
            MonitoringPopUtils.onLongClickCopy(this,mEntity.getUrl());
        }else if(i==R.id.tv_header){
            MonitoringPopUtils.onLongClickCopy(this,mEntity.getUserDataHeader());
        }else if(i==R.id.tv_header2){
            MonitoringPopUtils.onLongClickCopy(this,mEntity.getApiAuthHeader());
        }
        return false;
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(CustomHttpEntity event) {
// UI updates must run on MainThread
       mEntity  = event;
    }

}
