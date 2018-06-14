package retrofit;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.R;

import java.util.List;

import baserecyadapter.BaseQuickAdapter;
import baserecyadapter.BaseViewHolder;
import utils.StringUtils;

/**
 * author： admin
 * date： 2018/4/24
 * describe：
 */
public class HttpRequestAndResponseAdapter extends BaseQuickAdapter<CustomHttpEntity, BaseViewHolder> {
    public HttpRequestAndResponseAdapter(@Nullable List<CustomHttpEntity> data) {
        super(R.layout.item_request_response_layout,data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, CustomHttpEntity item) {
        TextView mTitle = helper.getView(R.id.tv_title);
        TextView mContent= helper.getView(R.id.tv_context);
        mTitle.setText(item.getUrl());
        mContent.setText(item.getMethod()+"    " + item.getSize()+"    请求时间"+item.getStartTime());
        helper.addOnClickListener(R.id.bt_delete);
        helper.addOnClickListener(R.id.ll_item);
        View view = helper.getView(R.id.ll_item);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
              MonitoringPopUtils.onLongClickCopy(mContext,mData.get(helper.getAdapterPosition()).getUrl());
                return true;
            }
        });
        if(StringUtils.isEmpty(item.getType())){
            if(item.isOk()){
                mContent.setTextColor(mContent.getResources().getColor(R.color.col_1dff2c));
                mContent.setText("请求成功    "+ item.getMethod()+"    " + item.getSize()+"    请求时间"+item.getStartTime());
            }else{
                mContent.setTextColor(mContent.getResources().getColor(R.color.col_ff351d));
                mContent.setText("请求失败    "+ item.getMethod()+"    " + item.getSize()+"    请求时间"+item.getStartTime());
            }

        }else{
            mContent.setTextColor(mContent.getResources().getColor(R.color.col_ff351d));
            mContent.setText("错误时间    "+item.getStartTime());
        }


    }
}
