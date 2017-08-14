package utils;



/**
 * Created by Teacher on 2016/9/2.
 */
public interface Constant {

//    String BASEURL = "http://10.0.2.2:8080/";
    String BASEURL = "http://192.168.191.1:8080/";
//    String BASEURL = "http://47.94.91.212:8080/";
    String LOGIN = "TakeoutService/login";
    String HOME = "TakeoutService/home";
    String GOODS = "TakeoutService/goods";
    String ADDRESS = "TakeoutService/address";
    String ORDER = "TakeoutService/order";
    String PAY = "TakeoutService/pay";
    // 短信登陆的分类值
    int LOGIN_TYPE_SMS = 2;
    String LAT = "Lat";
    String LNG = "Lng";
    int EDIT_ADDRESS_REQUESTCODE = 0x100;
    int ADD_ADDRESS_REQUESTCODE = 0x200;
    int DELETE_ADDRESS_REQUESTCODE = 0x300;

}
