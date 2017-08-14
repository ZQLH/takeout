package ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.example.lala.heimawaimaizhunbei.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import model.dao.bean.AddressBean;
import model.net.bean.GoodsInfo;
import model.MyApplication;
import ui.ShoppingCartManager;

/**
 * Created by lala on 2017/7/27.
 */

public class SettleCenterActicity extends BaseActivty {
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_sex)
    TextView tvSex;
    @InjectView(R.id.tv_phone)
    TextView tvPhone;
    @InjectView(R.id.tv_label)
    TextView tvLabel;
    @InjectView(R.id.tv_address)
    TextView tvAddress;
    @InjectView(R.id.ll_selected_address_container)
    LinearLayout llSelectedAddressContainer;
    @InjectView(R.id.tv_select_address)
    TextView tvSelectAddress;
    @InjectView(R.id.rl_location)
    RelativeLayout rlLocation;
    @InjectView(R.id.iv_logo)
    ImageView ivLogo;
    @InjectView(R.id.tv_seller_name)
    TextView tvSellerName;
    @InjectView(R.id.ll_select_goods)
    LinearLayout llSelectGoods;
    @InjectView(R.id.tv_send_price)
    TextView tvSendPrice;
    @InjectView(R.id.tv_count_price)
    TextView tvCountPrice;
    @InjectView(R.id.tv_submit)
    TextView tvSubmit;
    private AddressBean bean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle_center);
        ButterKnife.inject(this);
        setData();
    }
//手动添加商品信息列表
    private void setData() {
        //查询地址列表，判断定位点与地址列表中那个记录的距离近，则作为默认地址
        if (MyApplication.LOCATION!=null){
            addressPresenter.findAllByUserId(MyApplication.USERID);
        }
        //设置商家
        //设置购买商品
        //配送费设置
        //支付总额设置
        ivLogo.setImageResource(R.drawable.item_logo);
        tvSellerName.setText(ShoppingCartManager.getInstance().name);
        for (GoodsInfo goodsInfo : ShoppingCartManager.getInstance().goodsInfos) {
            View view=View.inflate(this,R.layout.item_settle_center_goods,null);
            ( (TextView) view.findViewById(R.id.tv_name)).setText(goodsInfo.name);
            ( (TextView) view.findViewById(R.id.tv_count)).setText("X"+goodsInfo.count);
            ( (TextView) view.findViewById(R.id.tv_price)).setText("¥"+goodsInfo.newPrice);

            int h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
            llSelectGoods.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,h);
        }
        tvSendPrice.setText("¥"+ShoppingCartManager.getInstance().sendPrice);
        float money=ShoppingCartManager.getInstance().getMoney()/100.0f+ShoppingCartManager.getInstance().sendPrice;
        tvCountPrice.setText("待支付¥"+money);

    }

    @OnClick({R.id.ib_back, R.id.rl_location, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.rl_location:
                //将收货地址返回给结算中心,进行数据的绑定
                Intent intent=new Intent(this,ReceiptAddressActivity.class);
                startActivityForResult(intent,200);
                break;
            case R.id.tv_submit:
                //用户输入校验：（是否选择了地址）
                if (addressId!=-1){
                    //提交订单到服务器，服务器会想订单数据中插入一条记录，生成对应的订单编号，该比那好会回复给手机端，手机端收到该比那好后去
                    //订单支付界面
                    orderPresenter.create(MyApplication.USERID,addressId,1);
                }else{
                    Toast.makeText(this,"请选择配送地址",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private int addressId=-1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==200){
            if (data!=null){
                int id=data.getIntExtra("id",-1);
                if (id!=-1){
                    addressId=id;
                    addressPresenter.findById(id);
                }
            }

        }
    }

    //在结算中心上进行绑定选中的地址
    @Override
    public void success(Object o) {
        if (o instanceof AddressBean){
            bean = (AddressBean) o;
            tvSelectAddress.setVisibility(View.GONE);
            llSelectedAddressContainer.setVisibility(View.VISIBLE);
            tvName.setText(bean.name);
            tvSex.setText(bean.sex);
            tvPhone.setText(bean.phone);
            tvAddress.setText(bean.detailAddress);
        }
        if(o instanceof  String){
            String s = o.toString();
            Intent intent=new Intent(this,OnlinePayActivity.class);
            intent.putExtra("orderId",s);

            intent.putExtra("address",bean);
            startActivity(intent);
        }
        if (o instanceof List){
            List<AddressBean> bean= (List<AddressBean>) o;
            for (AddressBean item:bean){
                LatLonPoint point=new LatLonPoint(item.latitude, item.longitude);
                double distance=getDistance(MyApplication.LOCATION,point);
                if (distance<500){
                    //该条目为默认地址
                    tvSelectAddress.setVisibility(View.GONE);
                    llSelectedAddressContainer.setVisibility(View.VISIBLE);
                    tvName.setText(item.name);
                    tvSex.setText(item.sex);
                    tvPhone.setText(item.phone);
                    tvAddress.setText(item.detailAddress);
                }
            }
        }
    }


    /*
  * 计算两点之间距离
  * @param start
  * @param end
  * @return 米
  */
    public double getDistance(LatLonPoint start, LatLonPoint end) {
        double lon1 = (Math.PI / 180) * start.getLongitude();
        double lon2 = (Math.PI / 180) * end.getLongitude();
        double lat1 = (Math.PI / 180) * start.getLatitude();
        double lat2 = (Math.PI / 180) * end.getLatitude();
        // 地球半径
        double R = 6371;
        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
        return d * 1000;
    }
}
