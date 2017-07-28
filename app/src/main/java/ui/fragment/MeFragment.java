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
import com.j256.ormlite.stmt.query.In;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ui.activity.LoginActivity;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.inject(this, view);
        return view;
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

    @OnClick(R.id.ll_userinfo)
    public void onViewClicked() {
        Intent intent=new Intent(this.getActivity(), LoginActivity.class);
        this.getActivity().startActivity(intent);
    }
}
