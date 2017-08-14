package presenter.fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import model.net.bean.GoodsTypeInfo;
import model.net.bean.ResponseInfo;
import presenter.base.BasePresenter;
import retrofit2.Call;
import ui.activity.BusinessActivity;
import ui.view.IView;

/**
 * Created by lala on 2017/7/24.
 */

public class GoodsFragmentPresenter extends BasePresenter {
    IView fragment;
    public GoodsFragmentPresenter(IView fragment) {
        this.fragment = fragment;
    }
    public  void getData(){
        //因为 responseInfoAPI = retrofit.create(ResponseInfoAPI.class);所以此时相当于是retrofit在继续执行
        //发起一个网络请求需要一个Call的对象，对象的数据类型由泛型来定义的，右边同时定义了网络请求的方式
        Call<ResponseInfo> call=responseInfoAPI.goods(BusinessActivity.sellerId);
        call.enqueue(new CallbackAdapter());
    }

    @Override
    protected void failed(String msg) {
        fragment.failed(msg);
    }

    @Override
    protected void parserData(String data) {
        Gson gson=new Gson();
        //data是一个字符串，正好gson解析的就是json的字符串，但如果想看起数据对应的bean的结构，则自己需要把
        //字符串外面的双引号去掉，就是一个json数据了，此时HiJson就可以方便解析出数据的结构了

        ArrayList<GoodsTypeInfo> info=gson.fromJson(data,new TypeToken<ArrayList<GoodsTypeInfo>>(){}.getType());
        //此时设配器就得到了HomeInfo数据，即
        fragment.success(info);
    }
}
