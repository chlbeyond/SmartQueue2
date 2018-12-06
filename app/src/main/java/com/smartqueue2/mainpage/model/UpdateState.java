package com.smartqueue2.mainpage.model;

/**
 * Created by Administrator on 2018/5/11.
 */

public class UpdateState {

    public enum State {
        WAITING, REPAST, PASS, DELETE       //等待；就餐；过号；删除
    }

    public static final int WAITING = 0;    //等待
    public static final int REPAST = 1;     //就餐
    public static final int PASS = 2;       //过号
    public static final int DELETE = 3;     //删除

    public UpdateState(UpdateState.State state) {
        switch (state) {
            case WAITING:
                this.state = UpdateState.WAITING;
                break;
            case REPAST:
                this.state = UpdateState.REPAST;
                break;
            case PASS:
                this.state = UpdateState.PASS;
                break;
            case DELETE:
                this.state = UpdateState.DELETE;
        }
    }

    public int state;
}
