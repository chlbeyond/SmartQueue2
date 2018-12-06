package com.smartqueue2.mainpage.model;

import java.util.List;

/**
 * Created by Administrator on 2018/6/26.
 */

public class DataModel {

    private User user;
    private ShopInfoResult shop;
    private List<Queue> queues;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ShopInfoResult getShop() {
        return shop;
    }

    public void setShop(ShopInfoResult shop) {
        this.shop = shop;
    }

    public List<Queue> getQueues() {
        return queues;
    }

    public void setQueues(List<Queue> queues) {
        this.queues = queues;
    }
}
