package dragger.module;

import dagger.Module;
import dagger.Provides;
import presenter.activity.AddressPresenter;
import presenter.activity.LoginActivityPresenter;
import presenter.fragment.GoodsFragmentPresenter;
import presenter.fragment.HomeFragmentPresenter;
import presenter.fragment.OrderFragmentPresenter;
import presenter.activity.PaymentPresenter;
import ui.view.IView;

/**
 * Created by lala on 2017/8/5.
 */
@Module
public class PresenterModule {
    private IView view;

    public PresenterModule(IView view) {
        this.view = view;
    }
    @Provides
    GoodsFragmentPresenter provideGoodsFragmentPresenter(){
        return  new GoodsFragmentPresenter(view);
    }

    @Provides
    public HomeFragmentPresenter provideHomeFragmentPresenter(){
        return new HomeFragmentPresenter(view);
    }
    @Provides
    public OrderFragmentPresenter provideOrderPresenter(){
        return new OrderFragmentPresenter(view);
    }
    @Provides
    public AddressPresenter provideAddressPresenter(){
        return new AddressPresenter(view);
    }
    @Provides
    public PaymentPresenter providePaymentPresenter(){
        return new PaymentPresenter(view);
    }
    @Provides
    public LoginActivityPresenter provideLoginActivityPresenter(){
        return new LoginActivityPresenter(view);
    }

}
