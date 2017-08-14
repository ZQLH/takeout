package ui.fragment;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.example.lala.heimawaimaizhunbei.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import model.MyApplication;
import model.net.bean.HomeInfo;
import ui.activity.SelectLocationActivity;
import ui.adapter.HomeRecyclerViewAdapter;
import ui.view.IView;

/**
 * 工作内容：
 * 1、布局
 * 2、头容器的处理
 * a、需要侵入到状态栏中
 * b、状态栏为透明
 * c、随着RecyclerView的滑动，头的透明度会变动
 * 3、RecyclerView数据加载
 * a、简单数据加载
 * b、复杂数据加载
 */

public class HomeFragment extends BaseFragment implements IView, AMapLocationListener {
    @InjectView(R.id.rv_home)
    RecyclerView rvHome;

    @InjectView(R.id.home_tv_address)
    TextView homeTvAddress;
    @InjectView(R.id.ll_title_search)
    LinearLayout llTitleSearch;
    @InjectView(R.id.ll_title_container)
    LinearLayout llTitleContainer;
    private HomeRecyclerViewAdapter adapter;
    private AMapLocationClient mlocationClient;
    //@Inject 使用 @inject 注解标记要注入对象的字段。


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new HomeRecyclerViewAdapter(getActivity());
        rvHome.setAdapter(adapter);
        rvHome.setLayoutManager(new LinearLayoutManager(MyApplication.context, LinearLayoutManager.VERTICAL, false));
        rvHome.addOnScrollListener(listener);

        location();
    }

    private void location() {
        mlocationClient = new AMapLocationClient(this.getContext());
        //初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位监听，监听定位结果
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation!=null&&aMapLocation.getErrorCode()==0){
            MyApplication.LOCATION=new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            String address = aMapLocation.getAddress( );
            homeTvAddress.setText(address);
            mlocationClient.stopLocation();

        }
    }

    private int sumY = 0;
    private float duration = 350.0f;
    private ArgbEvaluator evaluator = new ArgbEvaluator();
    RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            sumY += dy;
            int bgColor;
            if (sumY < 0) {
                bgColor = 0X113190E8;
            } else if (sumY > 350) {
                bgColor = 0XBB3190E8;
            } else {
                bgColor = (int) evaluator.evaluate(sumY / duration, 0X113190E8, 0XFF3190E8);
            }
            llTitleContainer.setBackgroundColor(bgColor);
        }
        // 滚动的总距离相对0-150之间有一个百分比，头部的透明度也是从初始值变动到不透明，
        // 通过距离的百分比，得到透明度对应的值
        // 如果小于0那么透明度为初始值，如果大于150为不透明状态
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        HomePresenter.getData();
    }

    @Override
    public void success(Object o) {
        adapter.setData((HomeInfo) o);
    }

    public void failed(String msg) {

    }

    public HomeRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @OnClick(R.id.home_tv_address)
    public void onViewClicked() {
        Intent intent=new Intent(this.getContext(),SelectLocationActivity.class);
        //一个请求码为200
        startActivityForResult(intent,200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        homeTvAddress.setText(data.getStringExtra("title"));
    }


}
