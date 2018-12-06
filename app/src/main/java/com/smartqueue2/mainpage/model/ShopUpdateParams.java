package com.smartqueue2.mainpage.model;

import java.util.List;

/**
 * Created by Administrator on 2018/7/13.
 */

public class ShopUpdateParams {
    private long shopId;
    private long userId;
    private String shopName;
    private String address;
    private String mobile;
    private String remark;
    private List<Queues> queues;

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Queues> getQueues() {
        return queues;
    }

    public void setQueues(List<Queues> queues) {
        this.queues = queues;
    }
}
