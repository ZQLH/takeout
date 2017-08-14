package presenter.fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import model.net.bean.Cart;
import model.net.bean.GoodsInfo;
import model.net.bean.Order;
import model.net.bean.OrderOverview;
import model.net.bean.ResponseInfo;
import presenter.base.BasePresenter;
import model.MyApplication;
import retrofit2.Call;
import ui.ShoppingCartManager;
import ui.view.IView;

/**
 * Created by lala on 2017/8/3.
 */

public class OrderFragmentPresenter extends BasePresenter{
    //生成订单      结算中心
    //订单列表查询  订单列表界面
    //订单详情查询   详情展示

    private IView veiw;
    int operation = 0;// 操作的标识
    private Object orderInfo;

    public OrderFragmentPresenter(IView veiw) {
        this.veiw = veiw;
    }

    @Override
    protected void failed(String msg) {

    }

    @Override
    protected void parserData(String data)  {
        switch(operation){
            case 1:
                veiw.success(data);
                break;
            case 2:
                Gson gson=new Gson();
                List<Order> orders=gson.fromJson(data,new TypeToken<List<Order>>(){}.getType());
                veiw.success(orders);
                break;
        }
    }
    //生成订单
    public void create(int userid,int addressId,int type){
        operation = 1;
        OrderOverview overview = new OrderOverview();
        overview.addressId = addressId;
        overview.sellerid = ShoppingCartManager.getInstance().sellerId;
        overview.type = type;
        overview.userId = userid;
        overview.cart = new ArrayList<>();
        for (GoodsInfo info : ShoppingCartManager.getInstance().goodsInfos) {
            Cart cart = new Cart();
            cart.id = info.id;
            cart.count = info.count;
            overview.cart.add(cart);
        }
        // 发送数据到服务器端（POST）
        Gson gson = new Gson();
        String json = gson.toJson(overview);
        Call<ResponseInfo> order = responseInfoAPI.creatOrder(json);
        order.enqueue(new CallbackAdapter());
    }

    public void getData() {
        operation=2;
        Call<ResponseInfo> call = responseInfoAPI.orderList(MyApplication.USERID);
        call.enqueue(new CallbackAdapter());
    }
    /**
     * 获取用户的订单列表数据
     */
    public void getOrderInfo() {
        Call<ResponseInfo> order = responseInfoAPI.order(1);
        order.enqueue(new CallbackAdapter());
    }
}
