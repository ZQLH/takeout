package ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lala.heimawaimaizhunbei.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lala on 2017/7/27.
 */

public class SettleCenterActicity extends AppCompatActivity {
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.rl_right)
    ImageView rlRight;
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
    @InjectView(R.id.rl_left)
    ImageView rlLeft;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle_center);
        ButterKnife.inject(this);
    }
}
