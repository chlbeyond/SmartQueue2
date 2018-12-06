package com.smartqueue2.mainpage.model;

import java.io.Serializable;

/**
 * Created by ss on 2016/7/14.
 */
public class ErrorStatus implements Serializable{
//    {
//        "error_code": 8025,
//            "message": "餐桌还没有开台",
//            "extra_message": null
//    }

    public int error_code = 0;

    public String message;

    public String extra_message;
}
