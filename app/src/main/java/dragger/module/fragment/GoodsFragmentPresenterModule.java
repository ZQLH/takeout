package dragger.module.fragment;

import dagger.Module;
import dagger.Provides;
import presenter.fragment.GoodsFragmentPresenter;
import ui.fragment.GoodsFragment;

/**
 * Created by lala on 2017/7/24.
 */
@Module
public class GoodsFragmentPresenterModule {
    GoodsFragment goodsFragment;

    public GoodsFragmentPresenterModule(GoodsFragment goodsFragment) {
        this.goodsFragment = goodsFragment;
    }
    @Provides
    GoodsFragmentPresenter provideGoodsFragmentPresenter(){
        return  new GoodsFragmentPresenter(goodsFragment);
    }
}
