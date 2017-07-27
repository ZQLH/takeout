package presenter.fragment;

import com.google.gson.Gson;

import model.net.bean.HomeInfo;
import model.net.bean.ResponseInfo;
import presenter.base.BasePresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ui.fragment.HomeFragment;
import utils.ErrorInfo;

/**
 * Created by lala on 2017/7/21.
 */

public class HomeFragmentPresenter extends BasePresenter {
    private HomeFragment fragment;

    public HomeFragmentPresenter(HomeFragment fragment) {
        this.fragment = fragment;
    }
    /**
     * 获取首页数据的步骤：
     * 1.需要在联网的API接口中增加一个获取首页数据的方法（访问方式和请求参数配置）
     * 2.异步获取首页数据
     * 3.数据处理
     * 4.展示数据到界面
     */
    public  void getData(){
        //因为 responseInfoAPI = retrofit.create(ResponseInfoAPI.class);所以此时相当于是retrofit在继续执行
        //发起一个网络请求需要一个Call的对象，对象的数据类型由泛型来定义的，右边同时定义了网络请求的方式
        Call<ResponseInfo> call=responseInfoAPI.home();
        call.enqueue(new CallbackAdapter());
    }


    public void failed(String msg) {
        fragment.failed(msg);
    }

    @Override
    protected void parserData(String data) {
        Gson gson=new Gson();
        //data是一个字符串，正好gson解析的就是json的字符串，但如果想看起数据对应的bean的结构，则自己需要把
        //字符串外面的双引号去掉，就是一个json数据了，此时HiJson就可以方便解析出数据的结构了

        HomeInfo info=gson.fromJson(data,HomeInfo.class);
        //此时设配器就得到了HomeInfo数据，即
        fragment.getAdapter().setData(info);
    }
}
