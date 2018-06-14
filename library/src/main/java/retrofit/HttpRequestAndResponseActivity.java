package retrofit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.R;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import base.BaseActivity;
import base.BasePresenter;
import base.BaseView;
import baserecyadapter.BaseQuickAdapter;
import butterknife.ButterKnife;
import utils.CustomFileUitils;

import static retrofit.HttpSharedUtils.HTTP;
import static retrofit.HttpSharedUtils.LOG;

/**
 * author： admin
 * date： 2018/4/24
 * describe：
 */
public class HttpRequestAndResponseActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    private HttpRequestAndResponseAdapter mAdapter;
    private List<CustomHttpEntity> dataList;
    private String mType;

    @Override
    public int getLayoutId() {
        return R.layout.act_request_response_layout;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public BaseView createView() {
        return null;
    }

    public static void startActivity(Context context, String type){
        Intent intent = new Intent(context,HttpRequestAndResponseActivity.class);
        intent.putExtra("type",type);
         context.startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void init() {
        mType = getIntent().getStringExtra("type");
        if(mType.equals(HTTP)){
            initTitle("网络列表");
        }else {
            initTitle("Log日志");
        }
        recyclerView = findViewById(R.id.recyclerView);
        TextView mClear = findViewById(R.id.tv_clear);
        mClear.setOnClickListener(this);
        dataList = HttpSharedUtils.getDataList(mType,CustomHttpEntity.class);
        if(dataList.size()>1)
            Collections.sort(dataList,new Comparator<CustomHttpEntity>() {//Comparator 比较器. 需要实现比较方法
                @Override
                public int compare(CustomHttpEntity o1, CustomHttpEntity o2) {
                    return (int) (o2.getSaveTime() - o1.getSaveTime());//从小到大 , 如果是o2.age-o1.age 则表示从大到小
                }
            });
        mAdapter = new HttpRequestAndResponseAdapter(dataList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.openLoadAnimation();
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int i = view.getId();
                if (i == R.id.bt_delete) {
                    String url = dataList.get(position).getUrl();
                    CustomFileUitils.deleteFile(url);

                    dataList.remove(position);

                    HttpSharedUtils.setDataList(mType,dataList);
                    mAdapter.notifyDataSetChanged();
                }else if(i == R.id.ll_item){
                    if(mType.equals(LOG)){
                        Intent intent = new Intent();
                        File file = new File(dataList.get(position).getUrl());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//设置标记
                        intent.setAction(Intent.ACTION_VIEW);//动作，查看
                        intent.setDataAndType(Uri.fromFile(file), "text/plain");//设置类型
                        startActivity(intent);
                    }else{
                        EventBus.getDefault().postSticky(dataList.get(position));
                        Intent intent = new Intent(HttpRequestAndResponseActivity.this,HttpRequestAndResponseDetailsActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
    @Override
    public void onClick(View view) {

        HttpSharedUtils.setDataList(mType,new ArrayList<CustomHttpEntity>());
        mAdapter.setNewData(new ArrayList<CustomHttpEntity>());

     if(mType.equals(LOG)){
         CustomFileUitils.deleteDir(Environment.getExternalStorageDirectory().getPath() + "/crash/");
     }
    }

}
