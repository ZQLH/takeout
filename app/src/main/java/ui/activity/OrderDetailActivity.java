package ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.lala.heimawaimaizhunbei.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import model.MyApplication;
import ui.Observer.OrderObserver;
import utils.Constant;

/**
 * Created by itheima.
 */
public class OrderDetailActivity extends BaseActivty implements Observer {

    @InjectView(R.id.iv_order_detail_back)
    ImageView ivOrderDetailBack;
    @InjectView(R.id.tv_seller_name)
    TextView tvSellerName;
    @InjectView(R.id.tv_order_detail_time)
    TextView tvOrderDetailTime;
    @InjectView(R.id.ll_order_detail_type_container)
    LinearLayout llOrderDetailTypeContainer;
    @InjectView(R.id.ll_order_detail_type_point_container)
    LinearLayout llOrderDetailTypePointContainer;
    @InjectView(R.id.map)
    MapView map;


    private String type;
    private String orderId;
    private AMap aMap;
    private LatLng riderPos;
    private Marker markerRider;
    private DecimalFormat format;
    private String inf;
    LatLng latlngBuyer= new LatLng(40.100519, 116.365828);
    LatLng latlngSeller= new LatLng(40.060244, 116.343513);
    private Boolean riderMove;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.inject(this);
        //实现观察者添加集合
        OrderObserver.getObserver().addObserver(this);
        posList = new ArrayList<>();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        tvSellerName.setText(extras.getString("name"));
        type = extras.getString("type");
        orderId = extras.getString("orderId");
        map.onCreate(savedInstanceState);
        aMap = map.getMap();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
        format = new DecimalFormat(".00");
        if (MyApplication.showMap){
         updateUi(MyApplication.type);

            initMap();
            if ( MyApplication.position.size()>0){
                LatLng nowPos=new LatLng(Double.valueOf(MyApplication.LAT),Double.valueOf(MyApplication.LNG));
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("Lat",MyApplication.LAT);
                hashMap.put("Lng",MyApplication.LNG);
                riderMove = true;
                initCacheRider(hashMap);
                riderMove=false;
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(nowPos));
                switch (MyApplication.type){
                    case OrderObserver.ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL:
                        float ds = AMapUtils.calculateLineDistance(nowPos, latlngSeller);
                        inf = "距离商家" + format.format(ds) + "米";
                        markerRider.setSnippet(inf);
                        markerRider.showInfoWindow();
                        markerRider.setPosition(nowPos);
                        break;
                    case OrderObserver.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL:
                        float db = AMapUtils.calculateLineDistance(nowPos, latlngBuyer);
                        inf = "距离买家" + format.format(db) + "米";
                        markerRider.setSnippet(inf);
                        markerRider.showInfoWindow();
                        markerRider.setPosition(nowPos);
                        break;
                }
                for (int i=0;i<MyApplication.position.size()-1;i++){
                    drawLine(MyApplication.position.get(i),MyApplication.position.get(i+1));
                }
            }
        }


    }

    private void initCacheRider(HashMap<String, String> data) {
        String lat = data.get(Constant.LAT);
        String lng = data.get(Constant.LNG);
        MyApplication.LAT=lat;
        MyApplication.LNG=lng;
        if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng)) {
            return;
        }
        riderPos = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
        posList.add(riderPos);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(riderPos));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        markerRider = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1).position(riderPos));
        markerRider.setSnippet("骑手已接单");
        markerRider.showInfoWindow();
        ImageView markerRiderIcon = new ImageView(this);
        markerRiderIcon.setImageResource(R.mipmap.order_rider_icon);
        markerRider.setIcon(BitmapDescriptorFactory.fromView(markerRiderIcon));

    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    /*
    修改界面的思路：
        确定展示的每个Type，对应的Index信息
        通过Index信息找到两个容器：文字、图片对应的控件
        设置该控件的文字颜色或图片即可，还原其他控件为默认信息
     */

    private void updateUi(String type) {
        int index = getIndex(type);
        if (index == -1)
            return;
        // 还原所有的图片和文字为最初的状态
        for (int i = 0; i < llOrderDetailTypeContainer.getChildCount(); i++) {
            TextView typeInfo = (TextView) llOrderDetailTypeContainer.getChildAt(i);
            ImageView typePoint = (ImageView) llOrderDetailTypePointContainer.getChildAt(i);
            typeInfo.setTextColor(getResources().getColor(R.color.colorTextDefault));
            typePoint.setImageResource(R.drawable.order_time_node_normal);
        }
        // 修改当前订单对应的状态
        TextView typeInfo = (TextView) llOrderDetailTypeContainer.getChildAt(index);
        ImageView typePoint = (ImageView) llOrderDetailTypePointContainer.getChildAt(index);

        typeInfo.setTextColor(getResources().getColor(R.color.colorBase));
        typePoint.setImageResource(R.drawable.order_time_node_disabled);
    }


    //根据传进来的type来确定index,依据index，可以修改UI
    private int getIndex(String type) {
        int index = -1;
//        String typeInfo = "";
        switch (type) {
            case OrderObserver.ORDERTYPE_UNPAYMENT:
                index = -1;
//                typeInfo = "未支付";
                break;
            case OrderObserver.ORDERTYPE_SUBMIT:
//                typeInfo = "已提交订单";
                index = 0;
                break;
            case OrderObserver.ORDERTYPE_RECEIVEORDER:
//                typeInfo = "商家接单";
                index = 1;
                break;
            case OrderObserver.ORDERTYPE_DISTRIBUTION://配送中
            case OrderObserver.ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE://骑手接单
            case OrderObserver.ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL://取餐
            case OrderObserver.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL: //送餐
                index = 2;
                break;
            case OrderObserver.ORDERTYPE_SERVED:
//                typeInfo = "已送达";
                index = 3;
                break;
            case OrderObserver.ORDERTYPE_CANCELLEDORDER:
//                typeInfo = "取消的订单";
                break;

        }
        return index;
    }

    @Override
    public void update(Observable observable, Object data) {
        String orderId = ((HashMap<String, String>) data).get("orderId");
        type = ((HashMap<String, String>) data).get("type");
        MyApplication.type=type;
        if (orderId.equals(this.orderId)) {
            // 根据推送的type ，更新界面
            updateUi(type);
            //配送中，显示地图，展示配送状态
            switch (type) {
                case OrderObserver.ORDERTYPE_DISTRIBUTION:
                    // 开始配送：需要展示买卖双方的位置信息
                    initMap();
                    break;
                case OrderObserver.ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE:
                    // 在地图上展示骑手位置，骑手接单。
                    // 骑手所在的位置为地图的中心点，放大地图17
                    initRider((HashMap<String, String>) data);
                    break;
                case OrderObserver.ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL:
                case OrderObserver.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL:
                    changeRider((HashMap<String, String>) data);
                    break;
            }
        }

    }

    /**
     * 处理骑手位置变动
     *
     * @param data
     */
    private void changeRider(HashMap<String, String> data) {
        // 修改骑手的位置
        // 修改地图中心
        // 修改提示信息展示内容
        // 绘制骑手的行进轨迹
        String lat = data.get(Constant.LAT);
        String lng = data.get(Constant.LNG);
        MyApplication.LAT=lat;
        MyApplication.LNG=lng;
        if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng)) {
            return;
        }
        LatLng currentPos = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
        posList.add(currentPos);

        MyApplication.position.add(currentPos);
        markerRider.setPosition(currentPos);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(currentPos));
        String info = "";
        switch (type) {
            case OrderObserver.ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL:
                // 取餐
                float ds = AMapUtils.calculateLineDistance(currentPos, latlngSeller);
                info = "距离商家" + format.format(ds) + "米";
                break;
            case OrderObserver.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL:
                // 送餐
                float db = AMapUtils.calculateLineDistance(currentPos, latlngBuyer);
                info = "距离买家" + format.format(db) + "米";
                break;
        }
        markerRider.setSnippet(info);
        markerRider.showInfoWindow();
        drawLine(currentPos, posList.get(posList.size() - 2));
    }

    /**
     * 绘制骑手行进轨迹
     *
     * @param currentPos
     * @param pos
     */
    private void drawLine(LatLng currentPos, LatLng pos) {
        aMap.addPolyline(new PolylineOptions().add(pos, currentPos).width(2).color(Color.GREEN));

    }

    List<LatLng> posList;

    /**
     * 在地图上展示骑手位置，骑手接单。
     * 骑手所在的位置为地图的中心点，放大地图17
     * @param data
     */
    private void initRider(HashMap<String, String> data) {
        String lat = data.get(Constant.LAT);
        String lng = data.get(Constant.LNG);
        MyApplication.LAT=lat;
        MyApplication.LNG=lng;
        if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng)) {
            return;
        }

        riderPos = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
        posList.add(riderPos);
        MyApplication.position.add(riderPos);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(riderPos));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));

        markerRider = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1).position(riderPos));
        markerRider.setSnippet("骑手已接单");
        markerRider.showInfoWindow();
        ImageView markerRiderIcon = new ImageView(this);
        markerRiderIcon.setImageResource(R.mipmap.order_rider_icon);
        markerRider.setIcon(BitmapDescriptorFactory.fromView(markerRiderIcon));

    }
    /**
     * 开始配送：需要展示买卖双方的位置信息
     */
    private void initMap() {
        MyApplication.showMap=true;
        map.setVisibility(View.VISIBLE);
        //让小蓝点移动到中心点
        aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        // 添加买家marker
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlngBuyer));
        Marker markerLatlngBuye = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1).position(latlngBuyer));
        ImageView markerBuyerIcon = new ImageView(this);
        markerBuyerIcon.setImageResource(R.mipmap.order_buyer_icon);
        markerLatlngBuye.setIcon(BitmapDescriptorFactory.fromView(markerBuyerIcon));
        Marker markerLatlngSeller = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1).position(latlngSeller));
        ImageView markerSellerIcon = new ImageView(this);
        markerSellerIcon.setImageResource(R.mipmap.order_seller_icon);
        markerLatlngSeller.setIcon(BitmapDescriptorFactory.fromView(markerSellerIcon));

    }

    @OnClick(R.id.iv_order_detail_back)
    public void onViewClicked() {
        finish();
    }
}
