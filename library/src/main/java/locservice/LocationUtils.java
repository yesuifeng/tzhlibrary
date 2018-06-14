package locservice;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import base.MyApplication;
import retrofit.HttpSharedUtils;

/**
 * author： admin
 * date： 2018/4/23
 * describe：百度定位工具类
 */
public class LocationUtils {

    private LocationUtils (){

    }
    private static volatile LocationUtils instance=null;
    public static  LocationUtils getInstance(){
        if(instance==null){
            synchronized(LocationUtils .class){
                if(instance==null){
                    instance=new LocationUtils ();
                }
            }
        }
        return instance;
    }

    public LocationService locationService;
    public  void startLocation(Context context){
        // -----------location config ------------
        locationService = ((MyApplication) context).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.registerListener(mListener);
        locationService.start();
    }
    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                HttpSharedUtils.setLocationProvince(location.getProvince());
                HttpSharedUtils.setLocationDistrict(location.getDistrict());
                HttpSharedUtils.setLocationAddress(location.getStreet());
                HttpSharedUtils.setLocationCity(location.getCity());//每次都重新定位
                locationService.stop();
            }
        }
    };
}
