package utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.MyApplication;

/**
 * Created by tz on 2015/11/6.
 */
public class SystemUtils {


    /**
     * 发送短信
     *
     * @param context
     * @param number
     * @param content
     */
    public static void sendSmsWithAskBuy(Context context, String number, String content) {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
        sendIntent.putExtra("sms_body", content);
        context.startActivity(sendIntent);
    }

    public static void sendSmsWithAskBuy(Context context, String number, int stringResId) {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
        sendIntent.putExtra("sms_body", context.getResources().getString(stringResId));
        context.startActivity(sendIntent);
    }

    /**
     * 到拨号盘
     *
     * @param context
     * @param phoneNumber
     */
    public static void dialPhoneNumber(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }


    /**
     * 直接拨打 默认的不请求网络
     *
     * @param context
     * @param phoneNumber
     */
    public static void phoneNumberDef(Context context, String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        }
    }


    /**
     * 弹出键盘
     *
     * @param phoneEdt
     */
    public static void showSoftMode(final EditText phoneEdt) {
        phoneEdt.setFocusable(true);
        phoneEdt.setFocusableInTouchMode(true);
        phoneEdt.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager = (InputMethodManager) phoneEdt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(phoneEdt, 0);
                           }
                       },
                500);
    }

    /**
     * 收起键盘
     *
     * @param phoneEdt
     */
    public static void closeSoftMode(final EditText phoneEdt) {
        InputMethodManager imm = (InputMethodManager) phoneEdt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(phoneEdt.getWindowToken(), 0);
    }



    /**
     * 判断软键盘是否显示
     * 如果显示，关闭
     * @param context
     */
    public static void softKeyClose(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {//如果显示，关闭
            try{
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }

    }


    /**
     * 获取版本号
     *
     * @return
     */
    public static int getVersion() {
        int result = 0;
        try {
            PackageInfo info = MyApplication.mContext.getPackageManager().getPackageInfo(MyApplication.mContext.getPackageName(), 0);
            result = info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取版本名称
     *
     * @return
     */
    public static String getVersionName() {
        String result = "";
        try {
            PackageInfo info = MyApplication.mContext.getPackageManager().getPackageInfo(MyApplication.mContext.getPackageName(), 0);
            result = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int getScreen(Activity con) {
        WindowManager wm = con.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return width;
    }

    public static int getScreenHeight(Context con) {
        if (null == con)
            con = MyApplication.mContext;
        WindowManager wm = (WindowManager) con.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    public static int getScreenWidth(Context con) {
        if (null == con)
            con = MyApplication.mContext;
        Resources resources = con.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        return width;
    }

    public static String getTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String date = sDateFormat.format(new java.util.Date());

        return date;

    }

    /**
     * 检查当前网络是否可用
     *
     * @param context
     * @return
     */

    public static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        if (null == context) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值 ， 或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    //安装apk
    public static void installApk(File file, Context content) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        content.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    /**
     * 判断手机是否有SD卡。
     *
     * @return 有SD卡返回true，没有返回false。
     */
    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }


    public static boolean isStartGps(Context context) {

        LocationManager locationManager = (LocationManager) context.
                getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void startGaps(Context context) {


       /* Intent i = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");

        String pkg = "com.android.settings";
        String cls = "com.android.settings.applications.InstalledAppDetails";

        i.setComponent(new ComponentName(pkg, cls));
        i.setData(Uri.parse("package:" + getPackageName()));
        startActivity(i);
        */


        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);


        } catch (ActivityNotFoundException ex) {

            // The Android SDK doc says that the location settings activity
            // may not be found. In that case show the general settings.

            // General settings activity
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
            }
        }
    }

    /**
     * 获取手机唯一标识
     * @return
     */
    public static String getSzImei() {
        TelephonyManager TelephonyMgr = (TelephonyManager) MyApplication.mContext.getSystemService(MyApplication.mContext.TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();//手机唯一标识
        return szImei;
    }

    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public static String getRunningActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }

    public static boolean startActivityIsExist(Context context) {
      /*  Intent intent = new Intent();
        intent.setClassName("com.guangan.woniu", "MainPageActivity");
        if(intent.resolveActivity(con.getPackageManager()) == null) {
            // 说明系统中不存在这个activity
            return false;
        }else{
            return true;
        }
*/
        //判断应用是否在运行
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        String MY_PKG_NAME = "com.guangan.woniu";
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME) || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;

    }

    public static void customTitle(Activity welcomeActivity) {

        welcomeActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //   welcomeActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

    }

    /**
     * 自动填写验证码
     *//*
    public static String getSmsFromPhone(long sendtime,Context context) {
        String[] projection = new String[]{"date", "body"};
        String where = " date >  " + (sendtime - 60 * 1000);//60s以内的短信
        //先记录发送命令的时间，找这时间以后收到的短信  ，按时间降序排序
        Cursor cur = context.getContentResolver().query(Uri.parse("content://sms/"), projection, where, null, "date desc");

        if (null == cur) {

            return "";
        }
        // 1458371840372   1458365678000
        //只要一条，使用if,如果是读取所有的短信，使用while
        if (cur.moveToNext()) {
            String date = cur.getString(cur.getColumnIndex("date"));
            String body = cur.getString(cur.getColumnIndex("body"));

            //这里我是要获取短信中的验证码
            Pattern pattern = Pattern.compile("(?<![0-9])([0-9]{" + 4 + "})(?![0-9])");
            Matcher matcher = pattern.matcher(body);
            if (matcher.find()) {
                String res = matcher.group(0).substring(0, 4);
                ToastUtils.showCenter(res);
                return res;
            }
        }
        return "";
    }*/
    public static String onSmsReceive(Context context, Intent intent) {
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj : objs) {
            byte[] pdu = (byte[]) obj;
            SmsMessage sms = SmsMessage.createFromPdu(pdu);
            String message = sms.getMessageBody();

            // 短息的手机号。。+86开头？
            String from = sms.getOriginatingAddress();

            if (!TextUtils.isEmpty(from)) {
                //这里我是要获取短信中的验证码
                Pattern pattern = Pattern.compile("(?<![0-9])([0-9]{" + 4 + "})(?![0-9])");
                Matcher matcher = pattern.matcher(message);
                if (matcher.find()) {
                    String res = matcher.group(0).substring(0, 4);

                    return res;
                }
            }
        }

        return "";
    }


    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getMobileModel() {
        return android.os.Build.MODEL;
    }

    public static String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public static String getMobileSysVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取网络格式
     *
     * @return
     */
    public static String getNetworkType() {
        String strNetworkType = "";

        ConnectivityManager manager = (ConnectivityManager) (MyApplication.mContext).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                }
            }
        }
        return strNetworkType;
    }

    public static void openAppByPackageName(Context context, String packageName) {
        PackageInfo pi;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.setPackage(pi.packageName);
            PackageManager pManager = context.getPackageManager();
            List<ResolveInfo> apps = pManager.queryIntentActivities(resolveIntent, 0);
            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//重点是加这个
                ComponentName cn = new ComponentName(packageName, className);
                intent.setComponent(cn);
                context.startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Intent intent = null;
            switch (packageName){
                case "com.android.woauctioncar":
                    try {
                        intent = new Intent(context,Class.forName("com.guangan.merchant.ui.web.WebViewActivity"));
                        intent.putExtra("Url","http://t.cn/RnvuQLx");
                        intent.putExtra("title","下载蜗拍车");
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }

                    break;

                case "com.guangan.woniu":
                    try {
                        intent = new Intent(context,Class.forName("com.guangan.merchant.ui.web.WebViewActivity"));
                        intent.putExtra("Url","http://www.woniuhuoche.com/downPage/index.html");
                        intent.putExtra("title","下载蜗牛二手货车");
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    break;

            }
            if(intent != null)
            context.startActivity(intent);
        }
    }

    /**
     * 读取assets下的txt文件，返回utf-8 String
     *
     * @param context
     * @param
     * @return
     */
    public static String readAssetsTxt(Context context) {
        try {
            InputStream is = context.getAssets().open("condition" + ".txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer, "utf-8");
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public interface getAllCall {
        void success(String tell, String isDirectSeed);
    }


}