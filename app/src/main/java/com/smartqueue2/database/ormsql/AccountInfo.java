package com.smartqueue2.database.ormsql;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/6/20.
 */
@Entity
public class AccountInfo  implements Serializable {

    static final long serialVersionUID = 42L;

    @Id(autoincrement = true) //id自动增长
    private Long id;

    private String account;
    private String password;
    private Long shopInfoId; //门店信息的id
    @Generated(hash = 1966466092)
    public AccountInfo(Long id, String account, String password, Long shopInfoId) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.shopInfoId = shopInfoId;
    }
    @Generated(hash = 1230968834)
    public AccountInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Long getShopInfoId() {
        return this.shopInfoId;
    }
    public void setShopInfoId(Long shopInfoId) {
        this.shopInfoId = shopInfoId;
    }
}
