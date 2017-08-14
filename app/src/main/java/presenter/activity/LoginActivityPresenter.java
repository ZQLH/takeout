package presenter.activity;

import com.google.gson.Gson;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

import model.dao.bean.UserBean;
import model.net.bean.ResponseInfo;
import presenter.base.BasePresenter;
import model.MyApplication;
import retrofit2.Call;
import ui.view.IView;
import utils.Constant;

/**
 * Created by lala on 2017/8/2.
 * 用户登录界面业务处理
 */

public class LoginActivityPresenter  extends BasePresenter{
    IView activity;
    private UserBean userBean2;

    public LoginActivityPresenter(IView activity)
    {
        this.activity = activity;
    }

    @Override
    protected void failed(String msg) {
        activity.failed(msg);
    }

    @Override
    protected void parserData(String data)  {
        Gson gson=new Gson();
        UserBean userBean=gson.fromJson(data,UserBean.class);
        //无论如何，把当前登录的状态给保存下来
        //进行数据库连接，开始数据库操作
        AndroidDatabaseConnection connection=null;
        Savepoint start=null;
        try {
            Dao<UserBean, Integer> dao = helper.getDao(UserBean.class);
            connection=new AndroidDatabaseConnection(helper.getReadableDatabase(),true);
            // 1.开启事务   还原点的设置
            start=connection.setSavePoint("start");
            // 2.一系列数据库操作不能立即生效
            dao.setAutoCommit(connection,false);
            // 3.查询所有的用户数据，修改登陆状态为false
            List<UserBean> userBeen = dao.queryForAll();
            if (userBeen!=null&&userBeen.size()>0){
                for (UserBean bean : userBeen) {
                    bean.login=false;
                    dao.update(bean);
                }
            }
            // 4、查询数据库中是否有当前已经登陆的信息，如果有改成已经登陆
            //userbean是解析json数据后返回来的数据
            List<UserBean> userBeens = dao.queryForEq("phone", userBean.phone);

            if (userBeens.size()>0){
                userBeens.get(0).login=true;
                MyApplication.USERID=  userBean._id;
                MyApplication.phone=userBean.phone;
            }else{
                userBean2 = new UserBean();
                userBean2._id=userBean._id;
                userBean2.phone=userBean.phone;
                userBean2.name=userBean.name;
                userBean2.login=true;
                MyApplication.USERID= userBean2._id;
                MyApplication.phone=userBean2.phone;
                dao.create(userBean2);
            }
            connection.commit(start);
            activity.success(userBean);
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection!=null){
                try {
                    connection.rollback(start);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            failed("修改本地数据异常");
        }
    }

    public void getData(String phone){
        Call<ResponseInfo> login = responseInfoAPI.login(phone, Constant.LOGIN_TYPE_SMS);
        login.enqueue(new CallbackAdapter());
    }
}
