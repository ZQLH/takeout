package ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lala.heimawaimaizhunbei.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import model.dao.bean.AddressBean;
import model.MyApplication;
import utils.Constant;

/**
 * 收货地址列表Activity
 */

public class ReceiptAddressActivity extends BaseActivty {

    //功能描述
    //1.添加数据给rvReceiptAddress
    //2.新增地址入口

    //获取地址数据
    //获取网络地址，获取本地地址
    //如果网络地址获取到后，存储到本地

    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.rv_receipt_address)
    RecyclerView rvReceiptAddress;
    @InjectView(R.id.tv_add_address)
    TextView tvAddAddress;
    MyAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_address);
        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addressPresenter.getData();
    }

    @OnClick({R.id.ib_back, R.id.tv_add_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            //新增地址
            case R.id.tv_add_address:
                Intent intent = new Intent(ReceiptAddressActivity.this, EditReceiptAddressActivity.class);
                startActivityForResult(intent, Constant.ADD_ADDRESS_REQUESTCODE);
                break;
        }
    }

    //在该方法中，进行数据的绑定。
    public void success(Object o) {
        List<AddressBean> been = (List<AddressBean>) o;
        if (been.size() > 0) {
            // 填充RecyclerView
            rvReceiptAddress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            adapter = new MyAdapter(been);
            rvReceiptAddress.setAdapter(adapter);
        } else {
            Toast.makeText(this, "请添加新地址", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constant.ADD_ADDRESS_REQUESTCODE){

        }else if(requestCode== Constant.EDIT_ADDRESS_REQUESTCODE){

        }
    //对地址的各种操作完成后回来了,在地址列表界面，获取最新的地址信息
        addressPresenter.getData();
    }

    class MyAdapter extends RecyclerView.Adapter {

        private List<AddressBean> been;

        public MyAdapter(List<AddressBean> been) {
            this.been = been;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MyApplication.context).inflate(R.layout.item_receipt_address, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            //把数据库里面的每一条地址数据，依次绑定到显示地址的条目上
            AddressBean data = been.get(position);
            viewHolder.setData(data);
        }

        @Override
        public int getItemCount() {
            return been.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
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
            @InjectView(R.id.iv_edit)
            ImageView ivEdit;
            private AddressBean data;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.inject(this, itemView);
            }

            @OnClick(R.id.iv_edit)
            public void onViewClicked() {
                Intent intent = new Intent(ReceiptAddressActivity.this, EditReceiptAddressActivity.class);
                intent.putExtra("id", data._id);// 地址id
                startActivityForResult(intent, Constant.EDIT_ADDRESS_REQUESTCODE);
            }

            public void setData(AddressBean data) {
                this.data = data;
                tvName.setText(data.name);
                tvSex.setText(data.sex);
                tvPhone.setText(data.phone);
                if (!TextUtils.isEmpty(data.label)) {
                    tvLabel.setVisibility(View.VISIBLE);
                    int index = MyApplication.getlabelIndex(data.label);
                    tvLabel.setText(MyApplication.addressLabels[index]);
                    tvLabel.setBackgroundColor(MyApplication.bgLabels[index]);
                } else {
                    tvLabel.setVisibility(View.GONE);
                }
                tvAddress.setText(data.receiptAddress + data.detailAddress);
                itemView.setOnClickListener(new View.OnClickListener() {
                    //用户点击地址时，将地址信息返回给结算中心。
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("id", ViewHolder.this.data._id);
                        ReceiptAddressActivity.this.setResult(200, intent);
                        ReceiptAddressActivity.this.finish();
                    }
                });
            }
        }
    }
}
