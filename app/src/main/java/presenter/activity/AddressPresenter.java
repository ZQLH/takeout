package presenter.activity;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;
import model.dao.bean.AddressBean;
import model.dao.bean.UserBean;
import model.net.bean.ResponseInfo;
import presenter.base.BasePresenter;
import model.MyApplication;
import retrofit2.Call;
import ui.view.IView;
import utils.Constant;

/**
 * Created by lala on 2017/8/2.
 */

public class AddressPresenter extends BasePresenter {
    //处理地址信息的dao，专门用于操纵数据库了关于地址的表，即dao是管理表的工具
    static Dao<AddressBean,Integer> dao ;
    private IView view;

    public AddressPresenter(IView view) {
        this.view = view;
        if (dao==null){
            try {
                dao=helper.getDao(AddressBean.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //地址的增删改查
    public void getData(){
        Call<ResponseInfo> address = responseInfoAPI.address(MyApplication.USERID);
        address.enqueue(new CallbackAdapter());
    }
    @Override
    protected void failed(String msg) {

    }

    @Override
    protected void parserData(String data) {
        if (TextUtils.isEmpty(data)){
           //网络请求返回的数据是空，则读取本地的地址
            findAllByUserId(MyApplication.USERID);
        }else{
            //存储到本地
            Gson gson=new Gson();
            //获取网络信息记录入库
            List<AddressBean > addressBeen=gson.fromJson(data,new TypeToken<List<AddressBean>>(){}.getType());
            for (AddressBean addressBean : addressBeen) {
                create(addressBean);
            }
            findAllByUserId(MyApplication.USERID);
        }
    }
    /**
     * 本地数据库新增收货地址的方法
     */
    public void create(String name, String sex, String phone, String receiptAddress, String detailAddress,
                       String label, double longitude, double latitude ) {
        //把用户输入的数据封装成了一个数据bean,即地址的数据bean
        AddressBean bean=new AddressBean(name,sex,phone,receiptAddress,detailAddress,label,longitude,latitude);
        //根据bean在数据库里插入一条数据，此时应该没有实现界面的数据更新
        int i=create(bean);
        if (i==1){
           view.success(i);
        }else{
            view.failed("添加操作失败");
        }
    }

    //每一个地址bean里面还存储了所对应的用户的信息
    public int create(AddressBean item){
        UserBean userBean=new UserBean();
        userBean._id=MyApplication.USERID;
        item.user=userBean;
        try {
            return dao.create(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     *  把找到的本地数据返回给recycleView来进行填充
     * @param userId
     */
    public void findAllByUserId(int userId){
        try {
            List<AddressBean> addressBean = dao.queryForEq("user_id", userId);
            view.success(addressBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void findById(int id) {
        try {
            AddressBean addressBean = dao.queryForId(id);
            view.success(addressBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void update(int id, String name, String sex, String phone, String receiptAddress, String detailAddress, String label, double longitude, double latitude) {
        AddressBean bean = new AddressBean(name, sex, phone, receiptAddress, detailAddress, label,longitude,latitude);
        UserBean user=new UserBean();
        user._id=MyApplication.USERID;
        bean.user=user;
        bean._id=id;
        try {
            int update = dao.update(bean);
            if(update==1){
                view.success(Constant.EDIT_ADDRESS_REQUESTCODE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void delete(int id) {
        try {
            int i = dao.deleteById(id);
            if (i==1){
                view.success(Constant.DELETE_ADDRESS_REQUESTCODE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
