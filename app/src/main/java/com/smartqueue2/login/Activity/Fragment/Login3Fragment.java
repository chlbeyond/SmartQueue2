package com.smartqueue2.login.Activity.Fragment;

import android.app.Activity;
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
import com.smartqueue2.login.Activity.Login3Activity;
import com.smartqueue2.login.Activity.ShopInfoActivity;
import com.smartqueue2.mainpage.Activity.QueueActivity;
import com.smartqueue2.mainpage.Api.PaiduiHttp;
import com.smartqueue2.mainpage.Paidui;
import com.smartqueue2.mainpage.model.DataModel;
import com.smartqueue2.mainpage.model.Queue;
import com.smartqueue2.mainpage.model.ShopInfoResult;
import com.smartqueue2.mainpage.model.User;
import com.smartqueue2.mainpage.model.UserRegisterResult;
import com.smartqueue2.mainpage.utils.GlobeMethod;
import com.smartqueue2.mainpage.utils.SharePreferenceUtil;
import com.smartqueue2.mainpage.utils.SmartPosPrivateKey;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Login3Fragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context mContext;
    private Login3Activity activity;
    private EditText account_et;
    private EditText password_et;
    private TextView login_tv;
    private String account_s;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login3, container, false);
        mContext = getContext();
        activity = (Login3Activity) getActivity();

        initView();
        return view;
    }

    private void initView() {
        account_et = (EditText) view.findViewById(R.id.account_et);
        password_et = (EditText) view.findViewById(R.id.password_et);

        account_s = SharePreferenceUtil.getPreference(activity, SmartPosPrivateKey.ACCOUNT, "");
        account_et.setText(account_s.toString().trim());
        account_et.setSelection(account_s.length());
        view.findViewById(R.id.login_tv).setOnClickListener(this);
        view.findViewById(R.id.toRegister_ll).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toRegister_ll:
                activity.displayView(Login3Activity.REGISTER_FRAGMENT);
                break;
            case R.id.login_tv:
                login();
                break;
        }
    }

    private void login() {
        String account = account_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();

        if (TextUtils.isEmpty(account)) {
            Paidui.toast("账号不能为空！");
        } else if (TextUtils.isEmpty(password)) {
            Paidui.toast("密码不能为空！");
        } else {
            loginRequest(account, password);
        }
    }

    //进行登录网络请求
    private void loginRequest(String account, String password) {
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
                    }

                    @Override
                    public void onNext(DataModel result) {
                        Log.e("TAG", "loginonNext: ");
                        Paidui.toast("登录成功！");
//                        String account_s = SharePreferenceUtil.getPreference(activity, SmartPosPrivateKey.ACCOUNT, "");
//                        String password_s = SharePreferenceUtil.getPreference(activity, SmartPosPrivateKey.PASSWORD, "");
                        SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.ACCOUNT, account);//保存登录账号密码
                        SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.PASSWORD, password);

                        User user = result.getUser();
                        ShopInfoResult shop = result.getShop();
                        List<Queue> queues = result.getQueues();
//
                        if (account_s.equals(account) && queues != null && queues.size() > 0) {//如果账号和保存的相同,且已经完善门店信息，则可以直接进去

                        }


                        if (queues != null && queues.size() > 0) {//如果已经完善了门店信息
                            if (account_s.equals(account)) {//如果还是上次登录的账号，直接进入
                                Intent intent = new Intent(activity, QueueActivity.class);
                                startActivity(intent);
                                activity.finish();
                            } else {//账号不同，则清空数据，接收后台的数据
                                GlobeMethod.emptyData();

                                StringBuilder builder = new StringBuilder();
                                String[] queueNameArray = new String[]{"A", "B", "C", "D", "E"};
                                int count = 0;//队伍数量
                                for (int i = 0; i < 5; i++) {
                                    if (queues.get(i).getMinNum() != 0 && queues.get(i).getMaxNum() != 0) {//判断有多少个队伍
                                        count++;
                                        builder.append(queueNameArray[i] + ",");//队列名字
                                    }
                                }
                                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.QUEUE_NUM, count);//存储队伍数量
                                SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.QUEUE_NAME, builder.toString());//存储队列名字ABCDE
                                SharePreferenceUtil.saveLongPreference(activity, SmartPosPrivateKey.ID, shop.getId());
                                SharePreferenceUtil.saveLongPreference(activity, SmartPosPrivateKey.USER, user.getId());
                                SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.SHOP_NAME, shop.getName());
                                SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.ADDRESS, shop.getAddress());
                                SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.PHONE, shop.getMobile());
                                SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.REMARK, shop.getRemark());
                                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.A_1, queues.get(0).getMinNum());
                                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.A_2, queues.get(0).getMaxNum());
                                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.B_1, queues.get(1).getMinNum());
                                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.B_2, queues.get(1).getMaxNum());
                                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.C_1, queues.get(2).getMinNum());
                                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.C_2, queues.get(2).getMaxNum());
                                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.D_1, queues.get(3).getMinNum());
                                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.D_2, queues.get(3).getMaxNum());
                                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.E_1, queues.get(4).getMinNum());
                                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.E_2, queues.get(4).getMaxNum());
                                SharePreferenceUtil.saveLongPreference(activity, SmartPosPrivateKey.A_ID, queues.get(0).getId());
                                SharePreferenceUtil.saveLongPreference(activity, SmartPosPrivateKey.B_ID, queues.get(1).getId());
                                SharePreferenceUtil.saveLongPreference(activity, SmartPosPrivateKey.C_ID, queues.get(2).getId());
                                SharePreferenceUtil.saveLongPreference(activity, SmartPosPrivateKey.D_ID, queues.get(3).getId());
                                SharePreferenceUtil.saveLongPreference(activity, SmartPosPrivateKey.E_ID, queues.get(4).getId());
                                Intent intent = new Intent(activity, QueueActivity.class);
                                startActivity(intent);
                                activity.finish();
                            }
                        } else {
                            //如果还未完善门店信息，则要去完善门店信息
                            GlobeMethod.emptyData();
                            //跳转门店设置界面
                            activity.displayView(Login3Activity.SHOP_FRAGMENT);
                        }
                    }
                });
    }
}
