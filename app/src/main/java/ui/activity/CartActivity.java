package ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lala.heimawaimaizhunbei.R;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import model.net.bean.GoodsInfo;
import presenter.base.MyApplication;
import ui.ShoppingCartManager;
import ui.fragment.GoodsFragment;
import utils.NumberFormatUtils;
import utils.RecycleViewDivider;

/**
 * Created by lala on 2017/7/27.
 */

public class CartActivity extends AppCompatActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tv_total)
    TextView tvTotal;
    @InjectView(R.id.tv_money)
    TextView tvMoney;
    @InjectView(R.id.button)
    Button button;
    @InjectView(R.id.bottom)
    RelativeLayout bottom;
    @InjectView(R.id.cart_rv)
    RecyclerView cartRv;
    public  float money=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.inject(this);
        toolbar.setTitle("购物车");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MyCarAdapter adapter = new MyCarAdapter();
        cartRv.setAdapter(adapter);
        cartRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cartRv.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 1, Color.BLACK));
        tvMoney.setText(NumberFormatUtils.formatDigits(ShoppingCartManager.getInstance().getMoney()));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (MyApplication.USERID != 0) {
                    intent = new Intent(getApplicationContext(), SettleCenterActicity.class);
                } else {
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                }
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyCarAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder holder = new ViewHolder(View.inflate( GoodsFragment.context, R.layout.item_cart, null));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder)holder).setData(ShoppingCartManager.getInstance().goodsInfos.get(position));
        }

        @Override
        public int getItemCount() {
            return ShoppingCartManager.getInstance().goodsInfos.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder{
            @InjectView(R.id.item_iv)
            ImageView itemIv;
            @InjectView(R.id.item_tv_name)
            TextView itemTvName;
            @InjectView(R.id.item_tv_price)
            TextView itemTvPrice;
            @InjectView(R.id.item_tv_num)
            TextView itemTvNum;
            private GoodsInfo data;

            ViewHolder(View view) {
                super(view);
                ButterKnife.inject(this, view);
            }
            public void setData(GoodsInfo data){
                this.data=data;
                Picasso.with(MyApplication.context).load(data.icon).into(itemIv);
                itemTvName.setText(data.name);
                itemTvPrice.setText(NumberFormatUtils.formatDigits(data.newPrice));
                itemTvNum.setText(data.count+"");
                money+=(data.newPrice*data.count);
                tvMoney.setText(money+"");

            }
        }
    }
}



