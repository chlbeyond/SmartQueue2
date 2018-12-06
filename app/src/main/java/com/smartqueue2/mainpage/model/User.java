package com.smartqueue2.mainpage.model;

/**
 * Created by Administrator on 2018/6/26.
 */

public class User {
    private Long id;
    private String name;
    private String password;
    private int state;
    private String createon;
    private String updateon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
