package com.smartqueue2.mainpage.model;

/**
 * Created by Administrator on 2018/7/13.
 */

public class FetchTicketParams {
    private long shopQueueId;
    private long shopId;
    private String shopName;
    private int queueNum;
    private String queueName;
    private int state;

    public long getShopQueueId() {
        return shopQueueId;
    }

    public void setShopQueueId(long shopQueueId) {
        this.shopQueueId = shopQueueId;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getQueueNum() {
        return queueNum;
    }

    public void setQueueNum(int queueNum) {
        this.queueNum = queueNum;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
