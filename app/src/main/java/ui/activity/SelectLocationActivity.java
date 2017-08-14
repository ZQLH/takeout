package ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.lala.heimawaimaizhunbei.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import model.MyApplication;

/**
 * Created by lala on 2017/8/11.
 */

public class SelectLocationActivity extends AppCompatActivity implements LocationSource, AMapLocationListener, PoiSearch.OnPoiSearchListener {

    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private RecyclerView rvPoiList;
    private MyAdapter adapter;
    private List<PoiItem> poiItems;// poi数据,操作的重点
    private PoiSearch.Query query;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        mapView = (MapView) findViewById(R.id.map);
        rvPoiList = (RecyclerView) findViewById(R.id.rv_poiList);
        rvPoiList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mapView.onCreate(savedInstanceState);
        //地图管理工具
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

    }

    //基本设置与小蓝点的配置
    private void setUpMap() {
        // 自定义系统定位小蓝点样式
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(0.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));


        aMap.setLocationSource(this);// 设置定位监听
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
//拒绝
            if(checkSelfPermission == PackageManager.PERMISSION_DENIED){
                //申请权限
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},100);
            }else if(checkSelfPermission == PackageManager.PERMISSION_GRANTED){//已经授权
                //// TODO: 2016/11/4
            }
            mlocationClient.startLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 100:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //执行后续的操作
                    Toast.makeText(this, "已经授权成功了", Toast.LENGTH_SHORT).show();
                    // TODO: 2016/11/4
                }
                break;
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                aMapLocation.getLatitude();
                aMapLocation.getLongitude();
                //停止定位
                mlocationClient.stopLocation();
                //查询附近的信息,却存储在内存中
                MyApplication.LOCATION = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                doSearchQuery(aMapLocation.getCity());
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    private void doSearchQuery(String city) {
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query("", "", city);
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);// 设置查第一页

        if (MyApplication.LOCATION != null) {
            PoiSearch poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(MyApplication.LOCATION, 500, true));//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    //搜索周邊的地點數據
                    poiItems = result.getPois();
                    adapter = new MyAdapter();
                    rvPoiList.setAdapter(adapter);
                }
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int rcode) {

    }

     class MyAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(SelectLocationActivity.this).inflate(R.layout.item_select_receipt_address, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            PoiItem poiItem = poiItems.get(position);
            viewHolder.setData(poiItem);
        }

        @Override
        public int getItemCount() {
            return poiItems.size();
        }

         class ViewHolder extends RecyclerView.ViewHolder {
            @InjectView(R.id.iv)
            ImageView iv;
            @InjectView(R.id.tv_title)
            TextView tvTitle;
            @InjectView(R.id.tv_snippet)
            TextView tvSnippet;

            private PoiItem data;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.inject(this, itemView);
            }

            public void setData(PoiItem item) {
                this.data = item;
                tvTitle.setText(item.getTitle());
                tvSnippet.setText(item.getSnippet());
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("title",data.getTitle());
                        intent.putExtra("snippet",data.getSnippet());
                        LatLonPoint point = data.getLatLonPoint();
                        intent.putExtra("latitude",point.getLatitude());
                        intent.putExtra("longitude", point.getLongitude());
                        SelectLocationActivity.this.setResult(200,intent);
                        SelectLocationActivity.this.finish();
                    }
                });

            }

        }
    }
}
