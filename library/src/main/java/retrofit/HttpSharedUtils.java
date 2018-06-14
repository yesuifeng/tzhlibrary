package retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import base.MyApplication;

/**
 * 储存数据工具类
 */
public class HttpSharedUtils {

    private static SharedPreferences mySharedPreferences = MyApplication.mContext.getSharedPreferences("http",Context.MODE_PRIVATE);
    private static Editor editor = mySharedPreferences.edit();
    public static final String HTTP = "http";
    public static final String  HOST = "host";
    public static final String LOG = "log";

    /**
     * 储存用户Id
     * @param id
     */
    public static void setUserId(String id){
        editor.putString("userId", id);
        editor.commit();
    }

    /**
     * 取出用户id
     */
    public static String getUserId(){
        return mySharedPreferences.getString("userId","");
    }
/**
     * 保存List
     * @param datalist
     *//*

    public static void setDataList(List<CustomHttpEntity> datalist) {
        if (null == datalist)
            return;
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString("httplist", strJson);
        editor.commit();

    }

    */
/**
     * 获取List
     * @return
     *//*

    public static List<CustomHttpEntity> getDataList() {
        List<CustomHttpEntity> datalist=new ArrayList<>();
        String strJson = mySharedPreferences.getString("httplist", null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<CustomHttpEntity>>() {
        }.getType());
        return datalist;

    }
*/







    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    public static  <T> void setDataList(String tag, List<T> datalist) {
        if (null == datalist )
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.putString(tag, strJson);
        editor.commit();

    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public static  <T> List<T> getDataList(String tag,Class<T> t) {
        List<T> datalist=new ArrayList<T>();
        String strJson = mySharedPreferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        JsonParser parser = new JsonParser();
        JsonArray jsonarray = parser.parse(strJson).getAsJsonArray();
        for (JsonElement element : jsonarray
                ) {
            datalist.add(new Gson().fromJson(element, t));
        }
        return datalist;

    }

    /**
     * 设置选中
     * @param selectorHost
     */
    public static void setSelectorHost(String selectorHost){
        editor.putString("selecthost",selectorHost);
        editor.commit();
    }

    /**
     * 获取选中host
     * @return
     */
    public static String getSelectorHost(){

        return mySharedPreferences.getString("selecthost","");
    }


    /**
     * 存储省市
     */
    public static void setCity(String id) {
        editor.putString("city", id);
        editor.commit();
    }

    /**
     * 获取省市
     */
    public static String getCity() {
        return mySharedPreferences.getString("city", "");
    }

    /**
     * 保存定位到的省
     *
     * @param province
     */
    public static void setLocationProvince(String province) {
        editor.putString("location_province", province);
        editor.commit();
    }

    /**
     * 获取保存定位的省
     *
     * @return
     */
    public static String getLocationProvince() {
        return mySharedPreferences.getString("location_province", "");
    }

    /**
     * 保存定位到的区县
     *
     * @param district
     */
    public static void setLocationDistrict(String district) {
        editor.putString("location_district", district);
        editor.commit();
    }

    /**
     * 获取保存定位的区县
     *
     * @return
     */
    public static String getLocationDistrict() {
        return mySharedPreferences.getString("location_district", "");
    }

    /**
     * 保存定位到的地址（详细）
     *
     * @param address
     */
    public static void setLocationAddress(String address) {
        editor.putString("location_address", address);
        editor.commit();
    }

    /**
     * 获取保存定位的地址（详细）
     *
     * @return
     */
    public static String getLocationAddress() {
        return mySharedPreferences.getString("location_address", "");
    }


    public static void setLocationCity(String id) {
        editor.putString("location", id);
        editor.commit();
    }

    public static String getLocationCity() {
        return mySharedPreferences.getString("location", "");
    }


    /**
     * 删除指定Key
     * @param key
     */
    public static void removeKey(String key){
        editor.remove(key);
        editor.commit();

    }
}
