package com.smartqueue2.login.Activity.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.smartqueue2.R;
import com.smartqueue2.login.Activity.Login3Activity;
import com.smartqueue2.login.Activity.ShopInfoActivity;
import com.smartqueue2.mainpage.Paidui;
import com.smartqueue2.mainpage.utils.SharePreferenceUtil;
import com.smartqueue2.mainpage.utils.SmartPosPrivateKey;

public class ShopFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context mContext;
    private Login3Activity activity;
    private EditText shopName_et;
    private EditText address_et;
    private EditText phone_et;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象用来设置发起定位的模式和相关参数。
    private AMapLocationClientOption locationOption = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop, container, false);
        mContext = getContext();
        activity = (Login3Activity) getActivity();
        initView();
        initLocation();
        return view;
    }

    private void initView() {
        shopName_et = (EditText) view.findViewById(R.id.shopName_et);
        address_et = (EditText) view.findViewById(R.id.address_et);
        phone_et = (EditText) view.findViewById(R.id.phone_et);
        view.findViewById(R.id.certain_tv).setOnClickListener(this);
        initData();
    }

    private void initData() {
        String shopName = SharePreferenceUtil.getPreference(activity, SmartPosPrivateKey.SHOP_NAME, "");
        String address = SharePreferenceUtil.getPreference(activity, SmartPosPrivateKey.ADDRESS, "");
        String phone = SharePreferenceUtil.getPreference(activity, SmartPosPrivateKey.PHONE, "");

        shopName_et.setText(shopName);
        shopName_et.setSelection(shopName.length());
        address_et.setText(address);
        phone_et.setText(phone);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.certain_tv:
                save();
                break;
        }
    }

    private void save() {
        String shopName = shopName_et.getText().toString().trim();
        String address = address_et.getText().toString().trim();
        String phone = phone_et.getText().toString().trim();

        if (TextUtils.isEmpty(shopName)) {
            Paidui.toast("门店名称不能为空！");
        } else if (TextUtils.isEmpty(address)) {
            Paidui.toast("门店地址不能为空！");
        } else {
            SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.SHOP_NAME, shopName);
            SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.ADDRESS, address);
            SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.PHONE, phone);
            activity.displayView(Login3Activity.QUEUE_FRAGMENT);
        }
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化client
        mLocationClient = new AMapLocationClient(mContext);
        locationOption = getDefaultOption();
        //设置定位参数
        mLocationClient.setLocationOption(locationOption);
        // 设置定位监听
        mLocationClient.setLocationListener(mLocationListener);
        mLocationClient.startLocation();//启动定位
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                //当地址为空且定位成功才赋值
                if (address_et.getText().toString().isEmpty() && (location.getErrorCode() == 0)) {
                    address_et.setText(location.getAddress());
                }
            }
        }
    };

    /**
     * 销毁定位
     */
    private void destroyLocation() {
        if (null != mLocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
            locationOption = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }
}
