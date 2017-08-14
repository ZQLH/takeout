package presenter.activity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import model.net.bean.PaymentInfo;
import model.net.bean.ResponseInfo;
import presenter.base.BasePresenter;
import retrofit2.Call;
import ui.view.IView;

/**
 * Created by lala on 2017/8/5.
 */

public class PaymentPresenter extends BasePresenter {
    private IView view;

    public PaymentPresenter(IView view) {
        this.view = view;
    }

    @Override
    protected void failed(String msg) {

    }

    @Override
    protected void parserData(String data) throws SQLException {
        try {
            JSONObject object=new JSONObject(data);
            int payDownTime = object.getInt("payDownTime");
            //为何money的返回值总是50呢
            double money = object.getDouble("money");
            String paymentInfo = object.getString("paymentInfo");
            Gson gson=new Gson();
            List<PaymentInfo> paymentInfos=gson.fromJson(paymentInfo,new TypeToken<List<PaymentInfo>>(){}.getType());
            HashMap<String,Object> dataMap=new HashMap<>();
            dataMap.put("payDownTime",payDownTime);

            dataMap.put("money",money);
            dataMap.put("paymentInfo",paymentInfos);
            view.success(dataMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData(String orderId) {
        Call<ResponseInfo> payment = responseInfoAPI.payment(orderId);
        payment.enqueue(new CallbackAdapter());
    }
}
