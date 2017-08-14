package dragger.component;

import dagger.Component;
import dragger.module.PresenterModule;
import ui.activity.BaseActivty;

/**
 * Created by lala on 2017/8/5.
 */
@Component(modules = PresenterModule.class)
public interface CommonComponent {
    void in(BaseActivty view);
}
