package com.smartqueue2.database.ormsql;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/5/10.
 * 只要写好属性，自动生成构造和setget方法
 */

@Entity
public class QueueInfo implements Serializable {

    static final long serialVersionUID = 42L;

    @Id(autoincrement = true) //id自动增长
    private Long id;

    private Long queueId;//每个小票的id
    private Long shopQueueId;//队列id
    private String queueName;//队列名 A,B,C
    private int num;  //序号 1,2,3
    private int people;  //人数
    private String phone;  //电话
    private Date create; //取号时间
    private int state;  //状态  等待、就餐、过号
    private int count;  //叫号次数
    @Generated(hash = 1005603424)
    public QueueInfo(Long id, Long queueId, Long shopQueueId, String queueName,
            int num, int people, String phone, Date create, int state, int count) {
        this.id = id;
        this.queueId = queueId;
        this.shopQueueId = shopQueueId;
        this.queueName = queueName;
        this.num = num;
        this.people = people;
        this.phone = phone;
        this.create = create;
        this.state = state;
        this.count = count;
    }
    @Generated(hash = 129304240)
    public QueueInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getQueueName() {
        return this.queueName;
    }
    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
    public int getNum() {
        return this.num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public int getPeople() {
        return this.people;
    }
    public void setPeople(int people) {
        this.people = people;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Date getCreate() {
        return this.create;
    }
    public void setCreate(Date create) {
        this.create = create;
    }
    public int getState() {
        return this.state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public int getCount() {
        return this.count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public long getQueueId() {
        return this.queueId;
    }
    public void setQueueId(long queueId) {
        this.queueId = queueId;
    }
    public long getShopQueueId() {
        return this.shopQueueId;
    }
    public void setShopQueueId(long shopQueueId) {
        this.shopQueueId = shopQueueId;
    }
    public void setQueueId(Long queueId) {
        this.queueId = queueId;
    }
    public void setShopQueueId(Long shopQueueId) {
        this.shopQueueId = shopQueueId;
    }
}
