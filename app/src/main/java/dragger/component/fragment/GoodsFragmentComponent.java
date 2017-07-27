package dragger.component.fragment;

import dagger.Component;
import dragger.module.fragment.GoodsFragmentPresenterModule;
import ui.fragment.GoodsFragment;

/**
 * Created by lala on 2017/7/24.
 */
@Component (modules = GoodsFragmentPresenterModule.class)
public interface GoodsFragmentComponent {
    void in(GoodsFragment goodsFragment);
}
