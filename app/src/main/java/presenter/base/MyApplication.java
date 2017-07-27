package presenter.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by lala on 2017/7/21.
 */

public class MyApplication extends Application{
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

}
