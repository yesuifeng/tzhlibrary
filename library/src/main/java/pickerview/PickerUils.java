package pickerview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pickerview.builder.OptionsPickerBuilder;
import pickerview.builder.TimePickerBuilder;
import pickerview.listener.OnOptionsSelectListener;
import pickerview.listener.OnTimeSelectListener;
import pickerview.view.OptionsPickerView;
import utils.LogUtils;
import utils.StringUtils;
import utils.TimeDataUtils;

import static utils.TimeDataUtils.DATE_STR;

/**
 * author： admin
 * date： 2018/4/11
 * describe：选择器工具类，可根据需求定制
 */

public class PickerUils {

    /**
     * 时间选择器封装
     *
     * @param context
     * @param startYear 开始时间范围
     * @param endYear   结束时间范围
     * @param booleans  年月日 时分秒  不填写默认时分秒
     */
    public static void showTimePicker(Context context, int startYear, int endYear, final PickerCallBack pickerCallBack, boolean... booleans) {
        LogUtils.d("1:"+System.currentTimeMillis()+"");
        boolean[] defBoo = {true, true, true, false, false, false};
        int defType = -1;
        for (int i = 0; i < (booleans.length); i++) {
            defBoo[i] = booleans[i];
            defType++;
        }
        LogUtils.d("2:"+System.currentTimeMillis()+"");
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        int mYear = selectedDate.get(selectedDate.YEAR);
        Calendar startDate = Calendar.getInstance();
        startDate.set(mYear - startYear, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
        Calendar endDate = Calendar.getInstance();
        endDate.set(mYear + endYear, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
        final int finalDefType = defType;
        LogUtils.d("3:"+System.currentTimeMillis()+"");
        new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String type = DATE_STR;

                switch (finalDefType) {

                    case 0:
                        type = "yyyy";
                        break;
                    case 1:
                        type = "yyyy-MM";
                        break;
                    case -1:
                    case 2:
                        type = "yyyy-MM-dd";
                        break;
                    case 3:
                        type = "yyyy-MM-dd HH";
                        break;
                    case 4:
                        type = "yyyy-MM-dd HH:mm";
                        break;
                    case 5:
                        type = "yyyy-MM-dd HH:mm:ss";
                        break;

                }
                LogUtils.d("4:"+System.currentTimeMillis()+"");
                if (null != pickerCallBack) {
                    pickerCallBack.timeCallBack(TimeDataUtils.getYearMonth(date, type));
                }
            }
        }).setType(defBoo)
                .isCyclic(false)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .build().show();
        LogUtils.d("5:"+System.currentTimeMillis()+"");
    }

    public static void showTimePicker(Context context,ViewGroup viewGroup, int startYear, int endYear, final PickerCallBack pickerCallBack, boolean... booleans) {
        boolean[] defBoo = {true, true, true, false, false, false};
        int defType = -1;
        for (int i = 0; i < (booleans.length); i++) {
            defBoo[i] = booleans[i];
            defType++;
        }
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        int mYear = selectedDate.get(selectedDate.YEAR);
        Calendar startDate = Calendar.getInstance();
        startDate.set(mYear - startYear, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
        Calendar endDate = Calendar.getInstance();
        endDate.set(mYear + endYear, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
        final int finalDefType = defType;
        new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String type = DATE_STR;

                switch (finalDefType) {

                    case 0:
                        type = "yyyy";
                        break;
                    case 1:
                        type = "yyyy-MM";
                        break;
                    case -1:
                    case 2:
                        type = "yyyy-MM-dd";
                        break;
                    case 3:
                        type = "yyyy-MM-dd HH";
                        break;
                    case 4:
                        type = "yyyy-MM-dd HH:mm";
                        break;
                    case 5:
                        type = "yyyy-MM-dd HH:mm:ss";
                        break;

                }

                if (null != pickerCallBack) {
                    pickerCallBack.timeCallBack(TimeDataUtils.getYearMonth(date, type));
                }
            }
        }).setType(defBoo)
                .isCyclic(false)
                .setDate(selectedDate).setDecorView(viewGroup)
                .setRangDate(startDate, endDate)
                .build().show();
    }




    /**
     * 选择器封装
     *
     * @param context
     * @param arrayList
     * @param listener
     */
    public static void showOptionsPicker(Activity context, List<String> arrayList, final OnOptionsSelectListener listener) {// 不联动的多级选项
//        OptionsPickerView pvNoLinkOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                if (pickerCallBack != null) {
//                    pickerCallBack.timeCallBack(options1 + "");
//                }
//            }
//        }).build();
        OptionsPickerView pvNoLinkOptions = new OptionsPickerBuilder(context, listener).build();
        pvNoLinkOptions.setPicker(arrayList);
        pvNoLinkOptions.show();
        pvNoLinkOptions.setPicker(arrayList);
        pvNoLinkOptions.show();


    }

    /**
     * 选择器封装
     *
     * @param context
     * @param viewGroup
     * @param arrayList
     * @param listener
     */
    public static OptionsPickerView showOptionsPicker(Activity context, ViewGroup viewGroup, List<String> arrayList, final OnOptionsSelectListener listener) {// 不联动的多级选项
        OptionsPickerView pvNoLinkOptions = new OptionsPickerBuilder(context, listener).setDecorView(viewGroup).build();
        pvNoLinkOptions.setPicker(arrayList);
        pvNoLinkOptions.show();

        return pvNoLinkOptions;
    }


    public interface PickerCallBack {

        void timeCallBack(String data);
    }


}
