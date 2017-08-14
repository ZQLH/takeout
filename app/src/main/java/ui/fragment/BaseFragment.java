package ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

import dragger.component.DaggerCommonFragmentComponent;
import dragger.module.PresenterModule;
import presenter.fragment.GoodsFragmentPresenter;
import presenter.fragment.HomeFragmentPresenter;
import presenter.fragment.OrderFragmentPresenter;
import ui.view.IView;


/**
 * Created by itheima.
 */
public class BaseFragment extends Fragment implements IView{
    @Inject
    public HomeFragmentPresenter HomePresenter;
    @Inject
    public GoodsFragmentPresenter GoodsPresenter;
    @Inject
    OrderFragmentPresenter orderPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCommonFragmentComponent.builder().presenterModule(new PresenterModule(this)).build().in(this);
    }

    @Override
    public void success(Object o) {

    }

    @Override
    public void failed(String msg) {

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen"); //统计页面,SplashScreen是类名
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen");
    }
}
