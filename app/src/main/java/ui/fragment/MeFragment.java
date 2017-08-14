package ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lala.heimawaimaizhunbei.R;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import model.MyApplication;
import ui.activity.LoginActivity;
import utils.CircleTransform;

/**
 * Created by lala on 2017/7/21.
 */

public class MeFragment extends BaseFragment {
    @InjectView(R.id.tv_user_setting)
    ImageView tvUserSetting;
    @InjectView(R.id.iv_user_notice)
    ImageView ivUserNotice;
    @InjectView(R.id.login)
    ImageView login;
    @InjectView(R.id.username)
    TextView username;
    @InjectView(R.id.phone)
    TextView phone;
    @InjectView(R.id.ll_userinfo)
    LinearLayout llUserinfo;
    @InjectView(R.id.address)
    ImageView address;
    @InjectView(R.id.imageView)
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.inject(this, view);
        tvUserSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.USERID=0;
                MyApplication.phone="";
                login.setVisibility(View.VISIBLE);
                llUserinfo.setVisibility(View.INVISIBLE);
                imageView.setImageResource(R.mipmap.user_center_default_avatar);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.USERID != 0) {
            login.setVisibility(View.INVISIBLE);
            llUserinfo.setVisibility(View.VISIBLE);
            username.setText("小黑");
            phone.setText(MyApplication.phone);
            Picasso.with(getActivity()).load(R.drawable.beaut).transform(new CircleTransform()).into(imageView);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.login)
    public void onViewClicked() {
        Intent intent = new Intent(this.getActivity(), LoginActivity.class);
        this.getActivity().startActivity(intent);
    }
}
