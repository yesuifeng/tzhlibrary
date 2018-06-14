package utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author： admin
 * date： 2018/4/16
 * describe：字符串判断工具类
 */
public class StringUtils {

    /**
     * 判断是否为空
     *
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
        if (TextUtils.isEmpty(string) || "null".equals(string) || "".equals(string.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 空内容返回""
     * @param string
     * @return
     */
    public static String trim(String string) {
        if (TextUtils.isEmpty(string) || "null".equals(string) || "".equals(string.trim())) {
            return "";
        } else {
            return string;
        }
    }

    public static String trim(String string,String defaultStr) {
        if (TextUtils.isEmpty(string) || "null".equals(string) || "".equals(string.trim())) {
            return defaultStr;
        } else {
            return string;
        }
    }
    public static String trim(String string,String appendStr,String defaultStr) {
        if (TextUtils.isEmpty(string) || "null".equals(string) || "".equals(string.trim())) {
            return defaultStr;
        } else {
            return string+appendStr;
        }
    }

    /**
     * 自定义设置文字显示 AbsoluteSizeSpan 是sp的三倍  1sp = AbsoluteSizeSpan（3）
     *
     * @param startStr
     * @param endStr
     * @param textView
     * @param changeStartColor true 为 改变 前部分的颜色,false 改变后部分的颜色
     */
    public static void setCustomText(String startStr, String endStr, TextView textView, int size,boolean changeStartColor) {

        if (StringUtils.isEmpty(startStr)) return;
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(startStr + endStr);

        /**
         * 设置颜色
         */
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0388FB"));
        if(changeStartColor){
            spannableString.setSpan(colorSpan, 0, startStr.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }else{
            spannableString.setSpan(colorSpan, startStr.length(), (startStr + endStr).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        /**
         * 设置字体大小
         */
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(size ,true);
        spannableString.setSpan(absoluteSizeSpan, 0, startStr.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        textView.setText(spannableString);


    }
    public static void setCustomText(String startStr, String endStr, TextView textView, int size,int color) {

        if (StringUtils.isEmpty(startStr)) return;
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(startStr + endStr);

        /**
         * 设置颜色
         */
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spannableString.setSpan(colorSpan, 0, startStr.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        /**
         * 设置字体大小
         */
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(size ,true);
        spannableString.setSpan(absoluteSizeSpan, 0, startStr.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        textView.setText(spannableString);


    }

    /**
     * 判断手机号是否有效
     *
     * @param phoneNumber 手机号码
     * @return
     */
    public static Boolean checkPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtil.showShortToast("手机号不能为空");
            return false;
        }
        if (phoneNumber.length() < 11) {
            ToastUtil.showShortToast("手机号不能少于11位");
            return false;
        }

        if (!isMobileNO(phoneNumber.toString())) {
            ToastUtil.showShortToast("该手机号无效");
            return false;
        }
        return true;

    }


    /**
     * 验证手机号的有效性
     *
     * @param mobiles
     * @return
     * @author jmn
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][0123456789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)){
            ToastUtil.showShortToast("手机号不能为空");
            return false;
        }else if(mobiles.length()!=11){
            ToastUtil.showShortToast("请填写正确格式的手机号");
            return false;
        }else {
            boolean matches = mobiles.matches(telRegex);
            if(!matches){
                ToastUtil.showShortToast("请填写正确格式的手机号");
            }
            return mobiles.matches(telRegex);
        }

    }

  /**
     * 验证手机号的有效性
     *
     * @param mobiles
     * @return
     * @author jmn
     */
    public static boolean isMobileNO(String mobiles,String hint) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][0123456789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)){
            ToastUtil.showShortToast(hint);
            return false;
        }else {
            boolean matches = mobiles.matches(telRegex);
            if(!matches){
                ToastUtil.showShortToast("请填写正确格式的手机号");
            }
            return mobiles.matches(telRegex);
        }

    }

    /**
     * 验证码校验
     * @param code
     * @return
     */
    public static boolean isCodeNo(String code){

        if(isEmpty(code)){
            ToastUtil.showShortToast("验证码不能为空");
            return false;
        }else if(code.length() != 4){
            ToastUtil.showShortToast("请填写正确格式的验证码");
            return  false;
        }else{
            return  true;
        }
    }
    /**
     * 密码校验
     * @param passworld
     * @return
     */
    public static boolean isPassworNo(String passworld){

        if(isEmpty(passworld)){
            ToastUtil.showShortToast("密码不能为空");
            return false;
        }else if(passworld.length() != 6){
            ToastUtil.showShortToast("请填写正确格式的密码");
            return  false;
        }else{
            return  true;
        }
    }
    /**
     * 密码校验
     * @param passworld
     * @return
     */
    public static boolean isPassworNo(String passworld,String hint){

        if(isEmpty(passworld)){
            ToastUtil.showShortToast(hint);
            return false;
        }else if(passworld.length() != 6){
            ToastUtil.showShortToast("请填写正确格式的密码");
            return  false;
        }else{
            return  true;
        }
    }
    /**
     * 姓名校验
     * @param name
     * @return
     */
    public static boolean isNameNo(String name){

        if(isEmpty(name)){
            ToastUtil.showShortToast("请填写您的真实姓名");
            return false;
        }else if(name.length() < 2){
            ToastUtil.showShortToast("请填写正确格式的姓名");
            return  false;
        }else if (name.contains("·") || name.contains("•")){
            if (name.matches("^[\\u4e00-\\u9fa5]+[·•][\\u4e00-\\u9fa5]+$")){
                return true;
            }else {
                ToastUtil.showShortToast("请填写正确格式的姓名");
                return false;
            }
        }else {
            if (name.matches("^[\\u4e00-\\u9fa5]+$")){
                return true;
            }else {
                ToastUtil.showShortToast("请填写正确格式的姓名");
                return false;
            }
        }
    }


    /**
     * 验证字符串是否为中文
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]{1,10}");
        Matcher m = p.matcher(str);
        if (m.matches()) {
            return true;
        } else return false;
    }

    public static String changeTellXing(String tell){
        if(!isMobileNO(tell))return "";
        String maskNumber = tell.substring(0,3)+"****"+tell.substring(7,tell.length());
        return maskNumber;
    }

    /**
     * 检验是否为有效车牌号
     * @param licence
     * @return
     */
    public static boolean isLicenceNO(String licence) {
        String licenceRegex = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Za-z]{1}[A-Za-z]{1}[A-Z0-9a-z]{4}[A-Z0-9a-z挂学警港澳]{1}$";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
//            boolean matches = licence.matches(licenceRegex);
//            if(!matches){
//////                ToastUtil.showShortToast("请填写正确格式的车牌号");
//////            }
            return licence.matches(licenceRegex);


    }

}
