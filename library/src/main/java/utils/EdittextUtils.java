package utils;

import android.text.InputFilter;
import android.widget.EditText;

/**
 * author： admin
 * date： 2018/4/18
 * describe：设置 edittext 输入位数 比如前几位后几位  999.99 就是前三位和后两位
 */
public class EdittextUtils {

    public static void setInputLength(double lenth, EditText editText){

        InputFilter[] filters = {new PriceInputFilter(lenth)};
        editText.setFilters(filters);

    }

}
