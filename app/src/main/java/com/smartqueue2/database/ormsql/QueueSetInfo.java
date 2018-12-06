package com.smartqueue2.database.ormsql;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/6/20.
 */
@Entity
public class QueueSetInfo  implements Serializable {

    static final long serialVersionUID = 42L;

    @Id(autoincrement = true) //id自动增长
    private Long id;

    private int A_1;
    private int A_2;
    private int B_1;
    private int B_2;
    private int C_1;
    private int C_2;
    private int D_1;
    private int D_2;
    private int E_1;
    private int E_2;
    @Generated(hash = 1847582335)
    public QueueSetInfo(Long id, int A_1, int A_2, int B_1, int B_2, int C_1,
            int C_2, int D_1, int D_2, int E_1, int E_2) {
        this.id = id;
        this.A_1 = A_1;
        this.A_2 = A_2;
        this.B_1 = B_1;
        this.B_2 = B_2;
        this.C_1 = C_1;
        this.C_2 = C_2;
        this.D_1 = D_1;
        this.D_2 = D_2;
        this.E_1 = E_1;
        this.E_2 = E_2;
    }
    @Generated(hash = 1972912444)
    public QueueSetInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getA_1() {
        return this.A_1;
    }
    public void setA_1(int A_1) {
        this.A_1 = A_1;
    }
    public int getA_2() {
        return this.A_2;
    }
    public void setA_2(int A_2) {
        this.A_2 = A_2;
    }
    public int getB_1() {
        return this.B_1;
    }
    public void setB_1(int B_1) {
        this.B_1 = B_1;
    }
    public int getB_2() {
        return this.B_2;
    }
    public void setB_2(int B_2) {
        this.B_2 = B_2;
    }
    public int getC_1() {
        return this.C_1;
    }
    public void setC_1(int C_1) {
        this.C_1 = C_1;
    }
    public int getC_2() {
        return this.C_2;
    }
    public void setC_2(int C_2) {
        this.C_2 = C_2;
    }
    public int getD_1() {
        return this.D_1;
    }
    public void setD_1(int D_1) {
        this.D_1 = D_1;
    }
    public int getD_2() {
        return this.D_2;
    }
    public void setD_2(int D_2) {
        this.D_2 = D_2;
    }
    public int getE_1() {
        return this.E_1;
    }
    public void setE_1(int E_1) {
        this.E_1 = E_1;
    }
    public int getE_2() {
        return this.E_2;
    }
    public void setE_2(int E_2) {
        this.E_2 = E_2;
    }
}
