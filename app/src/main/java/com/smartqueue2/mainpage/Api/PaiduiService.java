package com.smartqueue2.mainpage.Api;

import com.smartqueue2.mainpage.model.ChangePwParams;
import com.smartqueue2.mainpage.model.DataModel;
import com.smartqueue2.mainpage.model.FetchTicketParams;
import com.smartqueue2.mainpage.model.QrCode;
import com.smartqueue2.mainpage.model.QueueStateParams;
import com.smartqueue2.mainpage.model.ShopInfo;
import com.smartqueue2.mainpage.model.ShopInfoResult;
import com.smartqueue2.mainpage.model.ShopUpdateParams;
import com.smartqueue2.mainpage.model.ShopUpdateResult;
import com.smartqueue2.mainpage.model.UserRegisterParams;
import com.smartqueue2.mainpage.model.VoidModel;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ss on 2016/7/12.
 * 接口中的方法必须有返回值
 */
public interface PaiduiService {

    //注册
    @POST("user/regist")
    Observable<Boolean> register(@Body UserRegisterParams params);

    //登录
    @GET("user/login")
    Observable<DataModel> login(@Query("name") String name, @Query("password") String password);

    //门店注册
    @POST("shop/regist")
    Observable<ShopUpdateResult> shopRegister(@Body ShopInfo shopInfo);

    //修改密码
    @POST("user/update")
    Observable<Boolean> changePw(@Body ChangePwParams params);

    //更改门店信息
    @POST("shop/update")
    Observable<Boolean> shopUpdate(@Body ShopUpdateParams params);

    //更改队列状态：就餐、过号
    @POST("shop/send")
    Observable<Boolean> updateQueueState(@Body List<QueueStateParams> params);

    //取号
    @POST("shop/number")
    Observable<QrCode> fetchTicket(@Body FetchTicketParams params);

    //注销
    @POST("shop/cancel/{id}")
    Observable<Boolean> logout(@Path("id") Long id);
}
