package dragger.component;

import dagger.Component;
import dragger.module.PresenterModule;
import ui.fragment.BaseFragment;
import ui.fragment.GoodsFragment;

/**
 * Created by lala on 2017/8/6.
 */
@Component  (modules = PresenterModule.class)
public interface CommonFragmentComponent {
    void in(BaseFragment fragment);
}
