package dragger.module.fragment;

import dagger.Module;
import dagger.Provides;
import presenter.fragment.HomeFragmentPresenter;
import ui.fragment.HomeFragment;

/**
 * Created by lala on 2017/7/21.
 */
//使用 @Module 注解标记 Module 类；
@Module
public class HomeFragmentPresenterModule {
    private HomeFragment fragment;

    public HomeFragmentPresenterModule(HomeFragment fragment) {
        this.fragment = fragment;
    }
    //使用 @Provides 注解标记创建对象的方法。
    @Provides
    HomeFragmentPresenter provideHomeFragmentPresenter(){
        return new HomeFragmentPresenter(fragment);
    }
}
