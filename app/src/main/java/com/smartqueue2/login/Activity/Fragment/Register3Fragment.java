package com.smartqueue2.login.Activity.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartqueue2.R;
import com.smartqueue2.database.ormsql.AccountHelper;
import com.smartqueue2.database.ormsql.AccountInfo;
import com.smartqueue2.login.Activity.Login2Activity;
import com.smartqueue2.login.Activity.Login3Activity;
import com.smartqueue2.login.Activity.ShopInfoActivity;
import com.smartqueue2.mainpage.Api.PaiduiApi;
import com.smartqueue2.mainpage.Api.PaiduiHttp;
import com.smartqueue2.mainpage.Paidui;
import com.smartqueue2.mainpage.model.DataModel;
import com.smartqueue2.mainpage.model.User;
import com.smartqueue2.mainpage.model.UserRegisterParams;
import com.smartqueue2.mainpage.model.UserRegisterResult;
import com.smartqueue2.mainpage.model.VoidModel;
import com.smartqueue2.mainpage.utils.GlobeMethod;
import com.smartqueue2.mainpage.utils.SharePreferenceUtil;
import com.smartqueue2.mainpage.utils.SmartPosPrivateKey;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.sanyipos.sdk.api.SanyiSDK.uuid;

public class Register3Fragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context mContext;
    private Login3Activity activity;
    private EditText reAccount_et;
    private EditText rePassword_et;
    private EditText reRePassword_et;



    public Register3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register3, container, false);
        mContext = getContext();
        activity = (Login3Activity) getActivity();
        initView();
        return view;
    }

    private TextView register_tv;

    private void initView() {
        reAccount_et = (EditText) view.findViewById(R.id.reAccount_et);
        rePassword_et = (EditText) view.findViewById(R.id.rePassword_et);
        reRePassword_et = (EditText) view.findViewById(R.id.reRePassword_et);
        register_tv = (TextView) view.findViewById(R.id.register_tv);
        register_tv.setOnClickListener(this);
        view.findViewById(R.id.toLogin_ll).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toLogin_ll:
                activity.displayView(Login3Activity.LOGIN_FRAGMENT);
                break;
            case R.id.register_tv:
                //注册
                register();
                break;
        }
    }

    private void register() {
        register_tv.setEnabled(false);//防止多次点击

        String account = reAccount_et.getText().toString().trim();
        String password = rePassword_et.getText().toString().trim();
        String rePassword = reRePassword_et.getText().toString().trim();

        if (TextUtils.isEmpty(account)) {//如果注册的账号已经存在了呢？应该是后台返回账号已存在的信息
            Paidui.toast("账号不能为空！");
            register_tv.setEnabled(true);
        } else if (TextUtils.isEmpty(password)) {
            Paidui.toast("密码不能为空！");
            register_tv.setEnabled(true);
        } else if (TextUtils.isEmpty(rePassword)) {
            Paidui.toast("再次输入密码不能为空！");
            register_tv.setEnabled(true);
        } else if (!password.equals(rePassword)) {
            Paidui.toast("两次输入密码不相同！");
            register_tv.setEnabled(true);
        } else {
            UserRegisterParams params = new UserRegisterParams();
            params.setName(account);
            params.setPassword(password);

            PaiduiHttp.getInstance().getPaiduiService().register(params)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(result -> result)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onCompleted() {
                            Log.e("TAG", "registeronCompleted: ");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", "registeronError: " + e);
                            Paidui.toast("注册失败！");
                            register_tv.setEnabled(true);
                        }

                        @Override
                        public void onNext(Boolean bool) {
                            Log.e("TAG", "registeronNext: ");

                            if (!bool) {
                                Paidui.toast("注册账号已存在！");
                                register_tv.setEnabled(true);
                                return;
                            }

                            Paidui.toast("注册成功！");

                            SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.ACCOUNT, account);
                            SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.PASSWORD, password);

                            PaiduiHttp.getInstance().getPaiduiService().login(account, password)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.io())
                                    .map(result -> result)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<DataModel>() {
                                        @Override
                                        public void onCompleted() {
                                            Log.e("TAG", "loginonCompleted: ");
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.e("TAG", "loginonError: " + e);
                                            Paidui.toast("登录失败！");
                                            register_tv.setEnabled(true);
                                        }

                                        @Override
                                        public void onNext(DataModel result) {
                                            Log.e("TAG", "loginonNext: ");
                                            Paidui.toast("登录成功！");
                                            User user = result.getUser();
                                            SharePreferenceUtil.saveLongPreference(activity, SmartPosPrivateKey.USER, user.getId());
                                            GlobeMethod.emptyData();
                                            activity.displayView(Login3Activity.SHOP_FRAGMENT);
                                        }
                                    });
                        }
                    });
        }
    }

    private void login() {

    }
}
