package reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import cn.jpush.android.api.JPushInterface;
import ui.Observer.OrderObserver;
import utils.Constant;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JIGUANG-Example";

	@Override
	public void onReceive(Context context, Intent intent) {
		//1010 8027 3652 5689 39
		Bundle bundle = intent.getExtras();
		if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())){
			String extras=bundle.getString(JPushInterface.EXTRA_EXTRA);
			if (extras==null){
				return ;
			}
			String type="";
			String orderId="";
			String lat = null;
			String lng = null;
			try {
				JSONObject object=new JSONObject(extras);
				type=object.getString("type");
				orderId=object.getString("orderId");
				lat=object.getString(Constant.LAT);
				lng=object.getString(Constant.LNG);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			HashMap<String,String> data=new HashMap<>();
			if (!TextUtils.isEmpty(type)){
				data.put("type",type);
			}
			if (!TextUtils.isEmpty(orderId)){
				data.put("orderId",orderId);
			}
			if (!TextUtils.isEmpty(lat)){
				data.put(Constant.LAT,lat);
			}
			if (!TextUtils.isEmpty(lng)){
				data.put(Constant.LNG,lng);
			}
			if (data.size()>0){
				OrderObserver.getObserver().changeOrderInfo(data);
			}
		}

	}
}
