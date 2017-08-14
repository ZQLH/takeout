package ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lala.heimawaimaizhunbei.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import model.dao.bean.AddressBean;
import model.MyApplication;

import static utils.SMSUtil.isMobileNO;

/**
 * Created by lala on 2017/8/4.
 */

public class EditReceiptAddressActivity extends BaseActivty {
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ib_delete_address)
    ImageButton ibDeleteAddress;
    @InjectView(R.id.et_name)
    EditText etName;
    @InjectView(R.id.rb_man)
    RadioButton rbMan;
    @InjectView(R.id.rb_women)
    RadioButton rbWomen;
    @InjectView(R.id.rg_sex)
    RadioGroup rgSex;
    @InjectView(R.id.et_phone)
    EditText etPhone;
    @InjectView(R.id.ib_delete_phone)
    ImageButton ibDeletePhone;
    @InjectView(R.id.tv_receipt_address)
    TextView tvReceiptAddress;
    @InjectView(R.id.et_detail_address)
    EditText etDetailAddress;
    @InjectView(R.id.tv_label)
    TextView tvLabel;
    @InjectView(R.id.ib_select_label)
    ImageView ibSelectLabel;
    @InjectView(R.id.bt_ok)
    Button btOk;
    private int id;
    private double latitude;
    private double longitude;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_receipt_address);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        //初始化新增界面
        if (!TextUtils.isEmpty(MyApplication.phone)) {
            etPhone.setText(MyApplication.phone);
            ibDeletePhone.setVisibility(View.VISIBLE);
        }
        //是否有地址的编号传递到编辑界面
        // 如果有，地址的修改或删除
        // 如果没有，添加地址
        id = getIntent().getIntExtra("id", -1);
        if (id != -1) {
            tvTitle.setText("修改地址");
            ibDeletePhone.setVisibility(View.VISIBLE);
            // 需要将本地地址信息查询出来，将值设置到界面上
            addressPresenter.findById(id);
        } else {
            tvTitle.setText("新增地址");
            ibDeleteAddress.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.ib_back, R.id.ib_delete_phone, R.id.ib_delete_address,R.id.tv_receipt_address, R.id.et_detail_address, R.id.ib_select_label, R.id.bt_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_delete_phone:
                break;
            case R.id.tv_receipt_address:
                Intent intent=new Intent(this,SelectLocationActivity.class);
                startActivityForResult(intent,200);
                break;
            case R.id.ib_delete_address:
                showDeleteAlert();
                break;
            case R.id.et_detail_address:
                break;
            case R.id.ib_select_label:
                showSelectLabelAerdailog();
                break;
            case R.id.bt_ok:
                if (checkReceiptAddressInfo()) {
                    //获取界面数据
                    String name = etName.getText().toString().trim();
                    String sex = "";
                    //根据radioGroup的API来判断用户选中了哪一个性别
                    int checkedRadioButtonId = rgSex.getCheckedRadioButtonId();
                    switch (checkedRadioButtonId) {
                        case R.id.rb_man:
                            sex = "先生";
                            break;
                        case R.id.rb_women:
                            sex = "女士";
                            break;
                    }
                    String phone = etPhone.getText().toString().trim();
                    String receiptAddress = tvReceiptAddress.getText().toString().trim();
                    String detailAddress = etDetailAddress.getText().toString().trim();
                    String label = tvLabel.getText().toString();
                    //经纬度如何得到的呢？
                    if (id != -1) {
                        addressPresenter.update(id, name, sex, phone, receiptAddress, detailAddress, label, longitude, latitude);
                    } else {
                        addressPresenter.create(name, sex, phone, receiptAddress, detailAddress, label, longitude, latitude);
                    }
                }
                break;
        }
    }

    private void showSelectLabelAerdailog() {
        new AlertDialog.Builder(this).setTitle("选择标签").setItems(MyApplication.addressLabels, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    tvLabel.setText(MyApplication.addressLabels[which]);
                    tvLabel.setBackgroundColor(MyApplication.bgLabels[which]);
            }
        }).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==200){
            String title=data.getStringExtra("title");
            String snippet=data.getStringExtra("snippet");
            tvReceiptAddress.setText(title+"\r\n"+snippet);
            latitude = data.getDoubleExtra("latitude",0);
            longitude = data.getDoubleExtra("longitude",0);
        }
    }

    private void showDeleteAlert() {
        new AlertDialog.Builder(this).setTitle("删除地址").setMessage("确定要删除地址么？").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addressPresenter.delete(id);
            }
        }).create().show();
    }

    //检验用户输入的信息是否全面与正确
    public boolean checkReceiptAddressInfo() {
        String name = etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请填写联系人", Toast.LENGTH_SHORT).show();
            return false;
        }
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请填写手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isMobileNO(phone)) {
            Toast.makeText(this, "请填写合法的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        String receiptAddress = tvReceiptAddress.getText().toString().trim();
        if (TextUtils.isEmpty(receiptAddress)) {
            Toast.makeText(this, "请填写收获地址", Toast.LENGTH_SHORT).show();
            return false;
        }
        String address = etDetailAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请填写详细地址", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //将查询的地址信息，绑定到UI上。地址添加成功以后，关闭当前的地址编辑界面。
    @Override
    public void success(Object o) {
        if (o instanceof AddressBean) {
            AddressBean bean = (AddressBean) o;
            etName.setText(bean.name);
            etName.setSelection(bean.name.length());
            if (!TextUtils.isEmpty(bean.sex)) {
                if (bean.sex.equals("先生")) {
                    rbMan.setChecked(true);
                } else {
                    rbWomen.setChecked(true);
                }
            }
            etPhone.setText(bean.phone);
            tvReceiptAddress.setText(bean.receiptAddress);
            etDetailAddress.setText(bean.detailAddress);
            if (!TextUtils.isEmpty(bean.label)) {
                int index = MyApplication.getlabelIndex(bean.label);
                tvLabel.setText(MyApplication.addressLabels[index]);
                tvLabel.setBackgroundColor(MyApplication.bgLabels[index]);
            }
        }
        //新增地址或者 更新地址成功
        else {
            int request_code = (int) o;
            setResult(request_code);
            finish();
        }
    }
//    private int getIndexLabel(String label) {
//        int index = 0;
//        for (int i = 0; i < addressLabels.length; i++) {
//            if (label.equals(addressLabels[i])) {
//                index = i;
//                break;
//            }
//        }
//        return index;
//    }
}
