package com.smartqueue2.mainpage.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/7/14.
 */

public class ShopUpdateResult {

    @SerializedName("shop")
    private ShopInfoResult shop;

    private List<Queue> queues;

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
