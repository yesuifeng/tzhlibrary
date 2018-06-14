package utils;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * author： admin
 * date： 2018/4/11
 * describe：时间转换工具类
 */

public class TimeDataUtils {

    public static final String DATE_STR = "yyyy-MM-dd";
    public static final String TIME_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String CHINESE_STR = "yyyy年MM月dd日";
    public static final String CHINESE_STRYEARMONTH = "yyyy年MM月";
    public static final String CHINESE_STRMONTH = "MM月";

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getYearMonth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }
    public static String getYearMonth(Date date,String type) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        return format.format(date);
    }

    public static String getYear(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }

    public static String getMonthAfter(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(date);
    }


    public static void startCountDownTime(final TextView textView){
        textView.setEnabled(false);
        Flowable.intervalRange(0, 120, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        textView.setText("重新获取"+ String.valueOf(120 - aLong) + "s");
                    }
                }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                textView.setText("获取验证码");
                textView.setEnabled(true);
            }
        }).subscribe();
    }


}
