package model.dao.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_address")
public class AddressBean implements Serializable{
    //每一个地址都有自己的ID,设置ID自增长功能
    @DatabaseField(generatedId =  true)
    public int _id;
    @DatabaseField(canBeNull = false)
    public String name;
    @DatabaseField(canBeNull = false)
    public String sex;
    @DatabaseField(canBeNull = false)
    public String phone;
    @DatabaseField(canBeNull = false)
    public String receiptAddress;
    @DatabaseField(canBeNull = false)
    public String detailAddress;
    @DatabaseField(canBeNull = false)
    public String label;
    @DatabaseField(canBeNull = false)
    public long timeStamp;
    @DatabaseField(canBeNull = false)
    public double longitude;
    @DatabaseField(canBeNull = false)
    public double latitude;

    @DatabaseField(canBeNull = false,foreign = true,foreignColumnName = "_id",columnName = "user_id")
    public UserBean user;

    public AddressBean() {
    }

    public AddressBean(String name, String sex, String phone, String receiptAddress, String detailAddress, String label, double longitude, double latitude) {
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.receiptAddress = receiptAddress;
        this.detailAddress = detailAddress;
        this.label = label;
        this.longitude=longitude;
        this.latitude=latitude;
        timeStamp=System.currentTimeMillis();
    }
}
