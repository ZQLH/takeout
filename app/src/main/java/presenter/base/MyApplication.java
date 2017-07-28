package presenter.base;

import android.app.Application;
import android.content.Context;
import android.widget.TextView;

/**
 * Created by lala on 2017/7/21.
 */

public class MyApplication extends Application{
    public static int USERID=1 ;
    public static Context context;
    public static TextView count;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

}
