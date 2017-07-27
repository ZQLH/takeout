package presenter.base;

import android.util.Log;

import model.dao.bean.DBHelper;

import model.net.bean.ResponseInfo;
import presenter.api.ResponseInfoAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.Constant;
import utils.ErrorInfo;

/**
 * Created by lala on 2017/7/21.
 */

public abstract class BasePresenter {
    //业务层网络访问的封装
    protected Retrofit retrofit;
    protected ResponseInfoAPI responseInfoAPI;
    protected DBHelper helper;
    public BasePresenter(){
        retrofit=new Retrofit.Builder().baseUrl(Constant.BASEURL).addConverterFactory(GsonConverterFactory
        .create()).build();
        //retrofit需要创建ResponseInfoAPI
        responseInfoAPI = retrofit.create(ResponseInfoAPI.class);
        helper=DBHelper.getInstance(MyApplication.context);
    }



    public class CallbackAdapter implements Callback<ResponseInfo> {
        @Override
        public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
            // 处理回复
            if (response != null && response.isSuccessful()) {
                ResponseInfo info = response.body();
                if("0".equals(info.code)){
                    // 走子类重写的方法
                    parserData(info.data);
                }else{
                    // 服务器端处理成功，返回错误提示，该信息需要展示给用户
                    // 依据code值获取到失败的数据
                    String msg = ErrorInfo.INFO.get(info.code);
                    failed(msg);
                }}}

        @Override
        public void onFailure(Call<ResponseInfo> call, Throwable t) {
            // 联网过程中的异常
            Log.d("tag", t.getMessage().toString());
        }
    }

    /**
     * 错误处理
     * @param msg
     */
    protected abstract void failed(String msg);

    /**
     * 解析服务器回复数据
     * @param data
     */
    protected abstract void parserData(String data);
}
