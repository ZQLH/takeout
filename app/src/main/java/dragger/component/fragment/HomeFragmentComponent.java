package dragger.component.fragment;

import dagger.Component;
import dragger.module.fragment.HomeFragmentPresenterModule;
import ui.fragment.HomeFragment;

/**
 * Created by lala on 2017/7/21.
 */
@Component(modules = HomeFragmentPresenterModule.class)
public interface HomeFragmentComponent {
    void in(HomeFragment fragment);
}
