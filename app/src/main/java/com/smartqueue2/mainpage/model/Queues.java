package com.smartqueue2.mainpage.model;

/**
 * Created by Administrator on 2018/7/13.
 */

public class Queues {
    private long shopQueueID;
    private String name;
    private int minNum;
    private int maxNum;

    public long getShopQueueID() {
        return shopQueueID;
    }

    public void setShopQueueID(long shopQueueID) {
        this.shopQueueID = shopQueueID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinNum() {
        return minNum;
    }

    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }
}
