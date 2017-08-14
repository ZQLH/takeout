package model;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.mob.MobApplication;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;
/**
 * Created by lala on 2017/7/21.
 */

public class MyApplication extends MobApplication{
    public static ArrayList<LatLng> position=new ArrayList<>();
    public static  String LAT = "";
    public static  String LNG = "";
    public static  LatLonPoint LOCATION = null;
    public static int USERID=1 ;
    public static Context context;
    public static TextView count;
    public static String phone="13280000000";
    public static String[] addressLabels = new String[]{"家", "公司", "学校"};
    public static int[] bgLabels = new int[]{
            Color.parseColor("#fc7251"),//家  橙色
            Color.parseColor("#468ade"),//公司 蓝色
            Color.parseColor("#02c14b"),//学校   绿色
    };
    public static boolean showMap=false;
    public static String type;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        JPushInterface.setDebugMode(true);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);
        MobclickAgent.openActivityDurationTrack(false);


    }
    public static int getlabelIndex(String label) {
        int index = 0;
        for (int i = 0; i < MyApplication.addressLabels.length; i++) {
            if (label.equals(MyApplication.addressLabels[i])) {
                index = i;
                break;
            }
        }
        return index;
    }
}
