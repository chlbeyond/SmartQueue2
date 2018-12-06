package com.smartqueue2.mainpage.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartqueue2.mainpage.gson.CollectionDeserializer;
import com.smartqueue2.mainpage.gson.DateTimeDeserializer;
import com.smartqueue2.mainpage.gson.QueueConverterFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ss on 2016/7/12.
 */
public class PaiduiHttp {

    private static PaiduiHttp ourInstance;

    private static PaiduiService paiduiService;

    public static PaiduiHttp getInstance() {
        if (ourInstance == null) ourInstance = new PaiduiHttp();
        return ourInstance;
    }

    public static void clearInstance() {
        ourInstance = null;
        paiduiService = null;
    }

    private PaiduiHttp() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().readTimeout(10, TimeUnit.SECONDS).build();
//        okHttpClient.interceptors().add(new HttpHeaderInterceptor());
         /*
         * 查看网络请求发送状况
         */
//        if (Paidui.getInstance().log) {
//            okHttpClient.interceptors().add(chain -> {
//                Response response = chain.proceed(chain.request());
//                return response;
//            });
//        }
        if (PaiduiApi.BASE_URL == "")
            return;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PaiduiApi.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//网络请求适配器 支持RxJava平台
                .addConverterFactory(new QueueConverterFactory())//设置数据解析器
//                .addConverterFactory(GsonConverterFactory.create(gson))//设置数据解析器
                .client(okHttpClient)
                .build();
        this.paiduiService = retrofit.create(PaiduiService.class);
    }

    Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(Collection.class, new CollectionDeserializer())
            .registerTypeAdapter(Date.class, new DateTimeDeserializer()).create();

    public PaiduiService getPaiduiService() {
        return paiduiService;
    }

    class HttpHeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request r;
//            if (Restaurant.registerInfo != null) {
//
//                r = request.newBuilder()
//                        .addHeader("sanyi-device-id", Restaurant.DEVICE_ID)
//                        .addHeader("sanyi-staff-id", Long.toString(Restaurant.STAFF_ID))
//                        .addHeader("sanyi-shop-id", Restaurant.SHOP_ID)
////                        .addHeader("sanyi-device-name", Restaurant.DEVICE_NAME)
//                        .addHeader("sanyi-version-name", Restaurant.VERSION_NAME)
//                        .addHeader("sanyi-client-ip", NetUtil.getLocalIpAddress())
//                        .addHeader("sanyi-version-code", Restaurant.VERSION_CODE)
//                        .addHeader("sanyi-product-code", Restaurant.PRODUCT_CODE)
//                        .build();
//            } else {
                r = request.newBuilder()
//                        .addHeader("sanyi-device-id", Restaurant.DEVICE_ID)
//                        .addHeader("sanyi-staff-id", Integer.toString(Restaurant.STAFF_ID))
//                        .addHeader("sanyi-shop-id", Restaurant.SHOP_ID)
//                        .addHeader("sanyi-device-name", Restaurant.DEVICE_NAME)
//                        .addHeader("sanyi-version-name", Restaurant.VERSION_NAME)
//                        .addHeader("sanyi-client-ip", NetUtil.getLocalIpAddress())
//                        .addHeader("sanyi-version-code", Restaurant.VERSION_CODE)
//                        .addHeader("sanyi-product-code", Restaurant.PRODUCT_CODE)
                        .build();
//            }


            return chain.proceed(r);
        }
    }
}
