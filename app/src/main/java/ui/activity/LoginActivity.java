package ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lala.heimawaimaizhunbei.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import dragger.module.PresenterModule;
import model.dao.bean.UserBean;
import utils.SMSUtil;

/**
 * Created by lala on 2017/7/27.
 */

public class LoginActivity extends BaseActivty {
    @InjectView(R.id.iv_user_back)
    ImageView ivUserBack;
    @InjectView(R.id.iv_user_password_login)
    TextView ivUserPasswordLogin;
    @InjectView(R.id.et_user_phone)
    EditText etUserPhone;
    @InjectView(R.id.tv_user_code)
    TextView tvUserCode;
    @InjectView(R.id.et_user_code)
    EditText etUserCode;
    @InjectView(R.id.login)
    TextView login;
    private int i;
    private EventHandler eventHandler;
    private String num;
    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            if (msg.what==-9){
                tvUserCode.setText("重新发送("+i+")");
            }else if (msg.what==-8){
                tvUserCode.setText("获取验证码");
                tvUserCode.setClickable(true);
            }else{
                int event=msg.arg1;
                int result=msg.arg2;
                Object data=msg.obj;
                if (result==SMSSDK.RESULT_COMPLETE){
                    if (event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                        Toast.makeText(getApplicationContext(),"提交验证码成功",Toast.LENGTH_SHORT).show();
                        presenter.getData(num);
                        finish();
                    }else if (event==SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        Toast.makeText(getApplicationContext(),"验证码已经发送",Toast.LENGTH_SHORT).show();
                    }else{
                        ((Throwable)data).printStackTrace();
                        String message = ((Throwable) data).getMessage();
                        Log.d("LoginActivity", "短信码获取失败 = " + message);
                    }}}}};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //vent表示操作的类型，result表示操作的结果，data表示操作返回的数据

        eventHandler = new EventHandler() {
            //vent表示操作的类型，result表示操作的结果，data表示操作返回的数据
            public void afterEvent(int event, int result, Object data) {
                Message msg=new Message();
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
                //通过handler把回调的处理交给了handler
                handler.sendMessage(msg);
                }};
        // 注册监听器
        SMSSDK.registerEventHandler(eventHandler);
        ButterKnife.inject(this);
        SMSUtil.checkPermission(this);
    }
    
    
    //各种点击事件的处理
    @OnClick({R.id.tv_user_code, R.id.login,R.id.iv_user_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //获取验证码按钮
            case R.id.tv_user_code:
                 num= etUserPhone.getText().toString();
                boolean judgePhoneNums=SMSUtil.judgePhoneNums(this,num);
                if (!judgePhoneNums){
                    return;
                }
                SMSSDK.getVerificationCode("86",num );
                tvUserCode.setClickable(false);
                tvUserCode.setText("重新发送("+ i +")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (i=30;i>0;i--){
                            handler.sendEmptyMessage(-9);
                            if (i<=0){
                                break;
                            }
                            SystemClock.sleep(999);
                            }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;
            //提交按钮
            case R.id.login:
                testData();
                break;
            //回退按钮
            case R.id.iv_user_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    public void success(Object o) {
        if(o != null && o instanceof UserBean){
            String name = ((UserBean) o).name;
            String phone = ((UserBean) o).phone;
            Toast.makeText(LoginActivity.this, "用户登录成功: name = " + name + "; phone = " + phone, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(LoginActivity.this, "用户登录失败", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    public void failed(String msg) {

    }
    private void testData(){
        presenter.getData(etUserPhone.getText().toString());
    }

}
