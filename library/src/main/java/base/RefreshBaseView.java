package base;

import java.util.ArrayList;

/**
 * author： admin
 * date： 2018/5/15
 * describe：刷新view
 */
public interface RefreshBaseView extends BaseView {



    /**
     *
     */
    void onComplete();

    /**
     * 新数据
     * @param lists
     */
    void setNewData(ArrayList<? extends BaseEntity> lists);


    int getPage();


}
