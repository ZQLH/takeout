package ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lala.heimawaimaizhunbei.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import utils.SMSUtil;

/**
 * Created by lala on 2017/7/27.
 */

public class LoginActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        SMSUtil.checkPermission(this);
        //初始化工具
//        SMSUtil.ini
    }

    @OnClick({R.id.tv_user_code, R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_user_code:
                break;
            case R.id.login:
                break;
        }
    }
}
