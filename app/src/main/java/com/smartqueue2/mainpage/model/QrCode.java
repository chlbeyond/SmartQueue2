package com.smartqueue2.mainpage.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/7/8.
 */

public class QrCode {

    @SerializedName("queue")
    private FetchQueue queue;

    @SerializedName("weiXinMsgReply")
    private WeiXinMsg weiXinMsg;

    public FetchQueue getQueue() {
        return queue;
    }

    public void setQueue(FetchQueue queue) {
        this.queue = queue;
    }

    public WeiXinMsg getWeiXinMsg() {
        return weiXinMsg;
    }

    public void setWeiXinMsg(WeiXinMsg weiXinMsg) {
        this.weiXinMsg = weiXinMsg;
    }
}
