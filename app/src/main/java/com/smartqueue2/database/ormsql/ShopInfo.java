package com.smartqueue2.database.ormsql;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/6/20.
 */
@Entity
public class ShopInfo implements Serializable {

    static final long serialVersionUID = 42L;

    @Id(autoincrement = true) //id自动增长
    private Long id;

    private String shopName;//名称
    private String address;//地址
    private String phone;//电话
    private String remark;//备注
    private Long queueSetInfoId;//队列设置信息的id
    @Generated(hash = 2067709870)
    public ShopInfo(Long id, String shopName, String address, String phone,
            String remark, Long queueSetInfoId) {
        this.id = id;
        this.shopName = shopName;
        this.address = address;
        this.phone = phone;
        this.remark = remark;
        this.queueSetInfoId = queueSetInfoId;
    }
    @Generated(hash = 1227838148)
    public ShopInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getShopName() {
        return this.shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Long getQueueSetInfoId() {
        return this.queueSetInfoId;
    }
    public void setQueueSetInfoId(Long queueSetInfoId) {
        this.queueSetInfoId = queueSetInfoId;
    }
}
