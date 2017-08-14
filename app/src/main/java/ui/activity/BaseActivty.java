package ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

import dragger.component.DaggerCommonComponent;
import dragger.module.PresenterModule;
import presenter.activity.AddressPresenter;
import presenter.activity.LoginActivityPresenter;
import presenter.fragment.OrderFragmentPresenter;
import presenter.activity.PaymentPresenter;
import ui.view.IView;

/**
 * Created by lala on 2017/8/3.
 */

public class BaseActivty extends AppCompatActivity implements IView {
    @Inject
    AddressPresenter addressPresenter;
    @Inject
    OrderFragmentPresenter orderPresenter;
    @Inject
    PaymentPresenter paymentPresenter;
    @Inject
    LoginActivityPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCommonComponent.builder().presenterModule(new PresenterModule(this)).build().in(this);
    }

    @Override
    public void success(Object o) {

    }

    @Override
    public void failed(String msg) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen"); // 保证 onPageEnd 在onPause 之前调用,因
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen"); //统计页面，SplashScreen是Activity的类名
        MobclickAgent.onResume(this);
    }
}
