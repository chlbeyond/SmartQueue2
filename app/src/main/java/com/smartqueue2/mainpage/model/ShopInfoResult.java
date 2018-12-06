package com.smartqueue2.mainpage.model;

/**
 * Created by Administrator on 2018/6/26.
 */
//
public class ShopInfoResult {
    private Long id;
    private Long user;
    private String name;
    private String address;
    private String mobile;
    private String remark;
    private int state;
    private String createon;
    private String updateon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCreateon() {
        return createon;
    }

    public void setCreateon(String createon) {
        this.createon = createon;
    }

    public String getUpdateon() {
        return updateon;
    }

    public void setUpdateon(String updateon) {
        this.updateon = updateon;
    }
}
