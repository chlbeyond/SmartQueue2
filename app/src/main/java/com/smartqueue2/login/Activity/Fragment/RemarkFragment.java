package com.smartqueue2.login.Activity.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.smartqueue2.R;
import com.smartqueue2.login.Activity.Login3Activity;
import com.smartqueue2.mainpage.Activity.QueueActivity;
import com.smartqueue2.mainpage.Api.PaiduiHttp;
import com.smartqueue2.mainpage.Paidui;
import com.smartqueue2.mainpage.model.Queue;
import com.smartqueue2.mainpage.model.ShopInfo;
import com.smartqueue2.mainpage.model.ShopInfoResult;
import com.smartqueue2.mainpage.model.ShopUpdateResult;
import com.smartqueue2.mainpage.model.UserRegisterResult;
import com.smartqueue2.mainpage.utils.GlobeMethod;
import com.smartqueue2.mainpage.utils.SharePreferenceUtil;
import com.smartqueue2.mainpage.utils.SmartPosPrivateKey;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RemarkFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context mContext;
    private Login3Activity activity;
    private EditText remark_et;
    private TextView complete_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_remark, container, false);
        mContext = getContext();
        activity = (Login3Activity) getActivity();
        initView();
        return view;
    }

    private void initView() {
        remark_et = (EditText) view.findViewById(R.id.remark_et);
        view.findViewById(R.id.previous_tv).setOnClickListener(this);
        complete_tv = (TextView) view.findViewById(R.id.complete_tv);
        complete_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previous_tv:
                activity.displayView(Login3Activity.QUEUE_FRAGMENT);
                break;
            case R.id.complete_tv:
                complete();
                break;
        }
    }

    private void complete() {
        complete_tv.setEnabled(false);
        String remark = remark_et.getText().toString().trim();
        SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.REMARK, remark);

        List<Integer> queueList = GlobeMethod.getQueue();
        String queueNameArray[] = new String[]{"A", "B", "C", "D", "E"};

        List<Queue> queues = new ArrayList<>();
        for (int i = 0; i < queueNameArray.length; i++) {
            Queue queue = new Queue();
            queue.setName(queueNameArray[i]);
            queue.setMinNum(queueList.get(2 * i));
            queue.setMaxNum(queueList.get(2 * i + 1));
            queues.add(queue);
        }

        ShopInfo info = new ShopInfo();
        info.setUserId(SharePreferenceUtil.getPreferenceLong(activity, SmartPosPrivateKey.USER, 0));
        info.setShopName(SharePreferenceUtil.getPreference(activity, SmartPosPrivateKey.SHOP_NAME, ""));
        info.setAddress(SharePreferenceUtil.getPreference(activity, SmartPosPrivateKey.ADDRESS, ""));
        info.setMobile(SharePreferenceUtil.getPreference(activity, SmartPosPrivateKey.PHONE, ""));
        info.setRemark(SharePreferenceUtil.getPreference(activity, SmartPosPrivateKey.REMARK, ""));
        info.setQueues(queues);
        PaiduiHttp.getInstance().getPaiduiService().shopRegister(info)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(result -> result)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShopUpdateResult>() {
                    @Override
                    public void onCompleted() {
                        Log.e("TAG", "shopRegisterCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "shopRegisterError: " + e);
                        Paidui.toast("失败！");
                        complete_tv.setEnabled(true);
                    }

                    @Override
                    public void onNext(ShopUpdateResult result) {
                        Log.e("TAG", "shopRegisterNext: " + result.toString());
                        ShopInfoResult shop = result.getShop();
                        List<Queue> queues = result.getQueues();

                        if (shop.getId() != null && shop.getUser() != null) {
                            SharePreferenceUtil.saveLongPreference(activity, SmartPosPrivateKey.ID, shop.getId());
                            SharePreferenceUtil.saveLongPreference(activity, SmartPosPrivateKey.USER, shop.getUser());
                            SharePreferenceUtil.saveLongPreference(activity, SmartPosPrivateKey.A_ID, queues.get(0).getId());
                            SharePreferenceUtil.saveLongPreference(activity, SmartPosPrivateKey.B_ID, queues.get(1).getId());
                            SharePreferenceUtil.saveLongPreference(activity, SmartPosPrivateKey.C_ID, queues.get(2).getId());
                            SharePreferenceUtil.saveLongPreference(activity, SmartPosPrivateKey.D_ID, queues.get(3).getId());
                            SharePreferenceUtil.saveLongPreference(activity, SmartPosPrivateKey.E_ID, queues.get(4).getId());
                            Paidui.toast("成功！");
                            Intent intent = new Intent(activity, QueueActivity.class);
                            startActivity(intent);
                            activity.finish();
                        } else {
                            Paidui.toast("获取门店信息失败");
                            complete_tv.setEnabled(true);
                        }
                    }
                });
    }
}
