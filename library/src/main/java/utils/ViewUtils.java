package utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by tz on 2015/12/19.
 */
public class ViewUtils {

    public static void setViewHeight(View view, Activity ac) {

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = SystemUtils.getScreen(ac);
        layoutParams.height = (int) (SystemUtils.getScreen(ac)*0.7);
        view.setLayoutParams(layoutParams);
    }
    public static void setViewHeightByWidth(View view, int width, Activity ac) {

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = (int) (width*0.7);
        view.setLayoutParams(layoutParams);
    }
    public static void setViewHeight1(View view, Activity ac) {

        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) view.getLayoutParams();
        layoutParams.width = SystemUtils.getScreen(ac);
        layoutParams.height = (int) (SystemUtils.getScreen(ac)*0.7);
        view.setLayoutParams(layoutParams);
    }


    public static void setViewHeightOwnSellCar(View view, Activity ac) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = SystemUtils.getScreen(ac);
        layoutParams.height = (int) (SystemUtils.getScreen(ac)*0.5);
        view.setLayoutParams(layoutParams);
    }

    public static void setViewHeightOwnSellCar(View view, Activity ac, float height) {

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = SystemUtils.getScreen(ac);
        layoutParams.height = (int) (SystemUtils.getScreen(ac)*height);
        view.setLayoutParams(layoutParams);
    }

    public static void setHomeViewHeight(View view, Activity ac) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = SystemUtils.getScreen(ac);
        layoutParams.height =  SystemUtils.getScreen(ac)/2;
        view.setLayoutParams(layoutParams);
    }

    public static void setHomeViewHeighTwo(View view, Activity ac) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = SystemUtils.getScreen(ac);
        layoutParams.height = (int) (SystemUtils.getScreen(ac)*0.3);
        view.setLayoutParams(layoutParams);
    }

    public static void setHomeGame(View view, Activity ac) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = (int) (SystemUtils.getScreen(ac)*0.6);
        layoutParams.width = SystemUtils.getScreen(ac);
        view.setLayoutParams(layoutParams);
    }

    /**
     * 监听软键盘弹起
     * @param activityRootView 布局跟view
     * @param context
     * @param view 隐藏或者显示的view
     */
    public static void addOnGlobalLayoutListener(final View activityRootView, final Context context, final View view){
        if(null==activityRootView || null==context|| null==view)return;
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > SystemUtils.dip2px(context, 200)) {
                    view.setVisibility(View.GONE);
                }else{
                    view.setVisibility(View.VISIBLE);
                }
            }
        });

    }



}
