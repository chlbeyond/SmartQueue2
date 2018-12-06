package com.smartqueue2.login.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.smartqueue2.R;
import com.smartqueue2.commonview.BigNewDoubleSpinner;
import com.smartqueue2.mainpage.Activity.QueueActivity;
import com.smartqueue2.mainpage.Api.PaiduiHttp;
import com.smartqueue2.mainpage.Paidui;
import com.smartqueue2.mainpage.model.Queue;
import com.smartqueue2.mainpage.model.Queues;
import com.smartqueue2.mainpage.model.ShopInfo;
import com.smartqueue2.mainpage.model.ShopInfoResult;
import com.smartqueue2.mainpage.model.ShopUpdateParams;
import com.smartqueue2.mainpage.model.VoidModel;
import com.smartqueue2.mainpage.utils.GlobeMethod;
import com.smartqueue2.mainpage.utils.SharePreferenceUtil;
import com.smartqueue2.mainpage.utils.SmartPosPrivateKey;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShopInfoActivity extends BaseActivity implements View.OnClickListener, BigNewDoubleSpinner.OnSpinnerListener {

    private static final String TAG = "ShopInfoActivity";
    public static final String LONGINACTIVITY = "LoginActivity";
    public static final String QUEUEACTIVITY = "QueueActivity";
    public static final String FROM = "from";
    private String from;

    private EditText shopName_et;
    private EditText address_et;
    private EditText phone_et;
    private EditText remark_et;

    private String shopName;
    private String address;
    private String phone;
    private String remark;

    private BigNewDoubleSpinner spinnerA;
    private BigNewDoubleSpinner spinnerB;
    private BigNewDoubleSpinner spinnerC;
    private BigNewDoubleSpinner spinnerD;
    private BigNewDoubleSpinner spinnerE;

    private List<Integer> queueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_shop_info_land, R.layout.activity_shop_info_port);

        from = getIntent().getStringExtra(FROM);//判断从何处来

        shopName = SharePreferenceUtil.getPreference(ShopInfoActivity.this, SmartPosPrivateKey.SHOP_NAME, "");
        address = SharePreferenceUtil.getPreference(ShopInfoActivity.this, SmartPosPrivateKey.ADDRESS, "");
        phone = SharePreferenceUtil.getPreference(ShopInfoActivity.this, SmartPosPrivateKey.PHONE, "");
        remark = SharePreferenceUtil.getPreference(ShopInfoActivity.this, SmartPosPrivateKey.REMARK, "");
        if (!shopName.isEmpty() && LONGINACTIVITY.equals(from)) {//店名不为空且来自登录界面
            startActivity(new Intent(ShopInfoActivity.this, QueueActivity.class));
            finish();
        } else if (shopName.isEmpty() && LONGINACTIVITY.equals(from)) {//第一次进入
            GlobeMethod.initQueue();//先初始化队列数据
        }

        queueList = GlobeMethod.getQueue();//取出文件中的数据
        initView();
        initSpinner();
//        initLocation();
        initData();
    }

    private void initView() {
        findViewById(R.id.back_ll).setOnClickListener(this);
        shopName_et = (EditText) findViewById(R.id.shopName_et);
        address_et = (EditText) findViewById(R.id.address_et);
        phone_et = (EditText) findViewById(R.id.phone_et);
        remark_et = (EditText) findViewById(R.id.remark_et);
        if (QUEUEACTIVITY.equals(from)) {//从设置中进来已经有店名了
            shopName_et.setText(shopName);
            shopName_et.setSelection(shopName.length());
            address_et.setText(address);
            phone_et.setText(phone);
            remark_et.setText(remark);
        }
    }

    private void initSpinner() {
        spinnerA = (BigNewDoubleSpinner) findViewById(R.id.spinnerA);
        spinnerB = (BigNewDoubleSpinner) findViewById(R.id.spinnerB);
        spinnerC = (BigNewDoubleSpinner) findViewById(R.id.spinnerC);
        spinnerD = (BigNewDoubleSpinner) findViewById(R.id.spinnerD);
        spinnerE = (BigNewDoubleSpinner) findViewById(R.id.spinnerE);

        spinnerA.setOnSpinnerListener(this);
        spinnerB.setOnSpinnerListener(this);
        spinnerC.setOnSpinnerListener(this);
        spinnerD.setOnSpinnerListener(this);
        spinnerE.setOnSpinnerListener(this);

    }

    @Override
    public boolean onSelect(BigNewDoubleSpinner spinner, TextView textView, int number, int index) {
        boolean isSuccess = true;
        switch (spinner.getId()) {
            case R.id.spinnerA:
                if (index == 1) {//A队 左边的值
                    //对选择的值做限制，满足条件才能保存
                    if ((number >= queueList.get(1) && queueList.get(1) != 0) ||
                            (number >= queueList.get(2) && queueList.get(2) != 0) ||
                            (number >= queueList.get(4) && queueList.get(4) != 0) ||
                            (number >= queueList.get(6) && queueList.get(6) != 0) ||
                            (number >= queueList.get(8) && queueList.get(8) != 0)) {
                        isSuccess = false;
                    } else {
//                        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.A_1, number);
                        queueList.set(0, number);
                    }
                } else {
                    //A队 右边的值
                    if ((number <= queueList.get(0) && queueList.get(0) != 0) ||
                            (number >= queueList.get(2) && queueList.get(2) != 0) ||
                            (number >= queueList.get(4) && queueList.get(4) != 0) ||
                            (number >= queueList.get(6) && queueList.get(6) != 0) ||
                            (number >= queueList.get(8) && queueList.get(8) != 0)) {
                        isSuccess = false;
                    } else {
//                        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.A_2, number);
                        queueList.set(1, number);
                    }
                }
                break;
            case R.id.spinnerB:
                if (index == 1) {
                    if ((number >= queueList.get(3) && queueList.get(3) != 0) ||
                            (number <= queueList.get(1) && queueList.get(1) != 0) ||
                            (number >= queueList.get(4) && queueList.get(4) != 0) ||
                            (number >= queueList.get(6) && queueList.get(6) != 0) ||
                            (number >= queueList.get(8) && queueList.get(8) != 0)) {
                        isSuccess = false;
                    } else {
//                        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.B_1, number);
                        queueList.set(2, number);
                    }
                } else {
                    if ((number <= queueList.get(2) && queueList.get(2) != 0) ||
                            (number <= queueList.get(1) && queueList.get(1) != 0) ||
                            (number >= queueList.get(4) && queueList.get(4) != 0) ||
                            (number >= queueList.get(6) && queueList.get(6) != 0) ||
                            (number >= queueList.get(8) && queueList.get(8) != 0)) {
                        isSuccess = false;
                    } else {
//                        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.B_2, number);
                        queueList.set(3, number);
                    }
                }
                break;
            case R.id.spinnerC:
                if (index == 1) {
                    if ((number >= queueList.get(5) && queueList.get(5) != 0) ||
                            (number <= queueList.get(1) && queueList.get(1) != 0) ||
                            (number <= queueList.get(3) && queueList.get(3) != 0) ||
                            (number >= queueList.get(6) && queueList.get(6) != 0) ||
                            (number >= queueList.get(8) && queueList.get(8) != 0)) {
                        isSuccess = false;
                    } else {
//                        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.C_1, number);
                        queueList.set(4, number);
                    }
                } else {
                    if ((number <= queueList.get(4) && queueList.get(4) != 0) ||
                            (number <= queueList.get(1) && queueList.get(1) != 0) ||
                            (number <= queueList.get(3) && queueList.get(3) != 0) ||
                            (number >= queueList.get(6) && queueList.get(6) != 0) ||
                            (number >= queueList.get(8) && queueList.get(8) != 0)) {
                        isSuccess = false;
                    } else {
//                        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.C_2, number);
                        queueList.set(5, number);
                    }
                }
                break;
            case R.id.spinnerD:
                if (index == 1) {
                    if ((number >= queueList.get(7) && queueList.get(7) != 0) ||
                            (number <= queueList.get(1) && queueList.get(1) != 0) ||
                            (number <= queueList.get(3) && queueList.get(3) != 0) ||
                            (number <= queueList.get(5) && queueList.get(5) != 0) ||
                            (number >= queueList.get(8) && queueList.get(8) != 0)) {
                        isSuccess = false;
                    } else {
//                        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.D_1, number);
                        queueList.set(6, number);
                    }
                } else {
                    if ((number <= queueList.get(6) && queueList.get(6) != 0) ||
                            (number <= queueList.get(1) && queueList.get(1) != 0) ||
                            (number <= queueList.get(3) && queueList.get(3) != 0) ||
                            (number <= queueList.get(5) && queueList.get(5) != 0) ||
                            (number >= queueList.get(8) && queueList.get(8) != 0)) {
                        isSuccess = false;
                    } else {
//                        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.D_2, number);
                        queueList.set(7, number);
                    }
                }
                break;
            case R.id.spinnerE:
                if (index == 1) {
                    if ((number >= queueList.get(9) && queueList.get(9) != 0) ||
                            (number <= queueList.get(1) && queueList.get(1) != 0) ||
                            (number <= queueList.get(3) && queueList.get(3) != 0) ||
                            (number <= queueList.get(5) && queueList.get(5) != 0) ||
                            (number <= queueList.get(7) && queueList.get(7) != 0)) {
                        isSuccess = false;
                    } else {
//                        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.E_1, number);
                        queueList.set(8, number);
                    }
                } else {
                    if ((number <= queueList.get(8) && queueList.get(8) != 0) ||
                            (number <= queueList.get(1) && queueList.get(1) != 0) ||
                            (number <= queueList.get(3) && queueList.get(3) != 0) ||
                            (number <= queueList.get(5) && queueList.get(5) != 0) ||
                            (number <= queueList.get(7) && queueList.get(7) != 0)) {
                        isSuccess = false;
                    } else {
//                        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.E_2, number);
                        queueList.set(9, number);
                    }
                }
                break;
            default:
                isSuccess = false;
        }
        return isSuccess;
    }

    @Override
    public void onDelete(BigNewDoubleSpinner spinner) {
        switch (spinner.getId()) {
            case R.id.spinnerA:
                //删除就置0
//                SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.A_1, 0);
//                SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.A_2, 0);
                queueList.set(0, 0);
                queueList.set(1, 0);
                break;
            case R.id.spinnerB:
//                SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.B_1, 0);
//                SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.B_2, 0);
                queueList.set(2, 0);
                queueList.set(3, 0);
                break;
            case R.id.spinnerC:
//                SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.C_1, 0);
//                SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.C_2, 0);
                queueList.set(4, 0);
                queueList.set(5, 0);
                break;
            case R.id.spinnerD:
//                SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.D_1, 0);
//                SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.D_2, 0);
                queueList.set(6, 0);
                queueList.set(7, 0);
                break;
            case R.id.spinnerE:
//                SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.E_1, 0);
//                SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.E_2, 0);
                queueList.set(8, 0);
                queueList.set(9, 0);
                break;
        }
    }

    /*将存储在文件中的排队分组数据读取并赋值给控件显示*/
    private void initData() {
        if (queueList.get(1) != 0) {
            spinnerA.getLeft_tv().setText(String.valueOf(queueList.get(0)));
            spinnerA.getRight_tv().setText(String.valueOf(queueList.get(1)));
        } else {
            spinnerA.getSpinner_ll().setVisibility(View.GONE);
            spinnerA.getCreateSpinner_ll().setVisibility(View.VISIBLE);
        }

        if (queueList.get(3) != 0) {
            spinnerB.getLeft_tv().setText(String.valueOf(queueList.get(2)));
            spinnerB.getRight_tv().setText(String.valueOf(queueList.get(3)));
        } else {
            spinnerB.getSpinner_ll().setVisibility(View.GONE);
            spinnerB.getCreateSpinner_ll().setVisibility(View.VISIBLE);
        }

        if (queueList.get(5) != 0) {
            spinnerC.getLeft_tv().setText(String.valueOf(queueList.get(4)));
            spinnerC.getRight_tv().setText(String.valueOf(queueList.get(5)));
        } else {
            spinnerC.getSpinner_ll().setVisibility(View.GONE);
            spinnerC.getCreateSpinner_ll().setVisibility(View.VISIBLE);
        }

        if (queueList.get(7) != 0) {
            spinnerD.getLeft_tv().setText(String.valueOf(queueList.get(6)));
            spinnerD.getRight_tv().setText(String.valueOf(queueList.get(7)));
        } else {
            spinnerD.getSpinner_ll().setVisibility(View.GONE);
            spinnerD.getCreateSpinner_ll().setVisibility(View.VISIBLE);
        }

        if (queueList.get(9) != 0) {
            spinnerE.getLeft_tv().setText(String.valueOf(queueList.get(8)));
            spinnerE.getRight_tv().setText(String.valueOf(queueList.get(9)));
        } else {
            spinnerE.getSpinner_ll().setVisibility(View.GONE);
            spinnerE.getCreateSpinner_ll().setVisibility(View.VISIBLE);
        }
    }

    public void clickBut(View view) {
        String shopName = shopName_et.getText().toString();
        String address = address_et.getText().toString();
        String phone = phone_et.getText().toString();
        String remark = remark_et.getText().toString();

        if (shopName.isEmpty() || address.isEmpty()) {
            Paidui.toast("门店名称或地址不能为空！");
            return;
        }

        StringBuilder builder = new StringBuilder();
        String[] queueNameArray = new String[]{"A", "B", "C", "D", "E"};
        int count = 0;
        for (int i = 0; i < queueNameArray.length; i++) {
            if (queueList.get(i * 2) != 0 && queueList.get(i * 2 + 1) != 0) {//判断有多少个队伍
                count++;
                builder.append(queueNameArray[i] + ",");//队列名字
            } else if (queueList.get(i * 2) != 0 || queueList.get(i * 2 + 1) != 0){
                //防止队列中一个为0，另一个不为0
//                queueList.set(i * 2, 0);
//                queueList.set(i * 2 + 1, 0);
                Paidui.toast(queueNameArray[i]+"分组错误");
                return;
            }
        }

        if (count == 0) {
            Paidui.toast("队伍数量不能为0");
            return;
        }

        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.QUEUE_NUM, count);//存储队伍数量
        SharePreferenceUtil.saveStringPreference(ShopInfoActivity.this, SmartPosPrivateKey.QUEUE_NAME, builder.toString());
        SharePreferenceUtil.saveStringPreference(ShopInfoActivity.this, SmartPosPrivateKey.SHOP_NAME, shopName);
        SharePreferenceUtil.saveStringPreference(ShopInfoActivity.this, SmartPosPrivateKey.ADDRESS, address);
        SharePreferenceUtil.saveStringPreference(ShopInfoActivity.this, SmartPosPrivateKey.PHONE, phone);
        SharePreferenceUtil.saveStringPreference(ShopInfoActivity.this, SmartPosPrivateKey.REMARK, remark);
        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.A_1, queueList.get(0));
        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.A_2, queueList.get(1));
        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.B_1, queueList.get(2));
        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.B_2, queueList.get(3));
        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.C_1, queueList.get(4));
        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.C_2, queueList.get(5));
        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.D_1, queueList.get(6));
        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.D_2, queueList.get(7));
        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.E_1, queueList.get(8));
        SharePreferenceUtil.saveIntPreference(ShopInfoActivity.this, SmartPosPrivateKey.E_2, queueList.get(9));


        ShopUpdateParams params = new ShopUpdateParams();//ShopUpdateParams
        List<Queues> queues = new ArrayList<>();
        for (int i = 0; i < queueNameArray.length; i++) {
            Queues queue = new Queues();
            queue.setShopQueueID(GlobeMethod.getQueueId().get(i));
            queue.setName(queueNameArray[i]);
            queue.setMinNum(queueList.get(2 * i));
            queue.setMaxNum(queueList.get(2 * i + 1));
            queues.add(queue);
        }
        params.setShopId(SharePreferenceUtil.getPreferenceLong(ShopInfoActivity.this, SmartPosPrivateKey.ID, 0));
        params.setUserId(SharePreferenceUtil.getPreferenceLong(ShopInfoActivity.this, SmartPosPrivateKey.USER, 0));
        params.setShopName(shopName);
        params.setAddress(address);
        params.setMobile(phone);
        params.setRemark(remark);
        params.setQueues(queues);

        PaiduiHttp.getInstance().getPaiduiService().shopUpdate(params)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(result -> result)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                        Log.e("TAG", "shopUpdateCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "shopUpdateError: " + e);
                        SharePreferenceUtil.saveBooleanPreference(ShopInfoActivity.this, SmartPosPrivateKey.ISCHANGESHOP, true);
                        Intent intent = new Intent(ShopInfoActivity.this, QueueActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onNext(Boolean result) {
                        SharePreferenceUtil.saveBooleanPreference(ShopInfoActivity.this, SmartPosPrivateKey.ISCHANGESHOP, false);
                        Intent intent = new Intent(ShopInfoActivity.this, QueueActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_ll:
                Intent intent = new Intent(ShopInfoActivity.this, QueueActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

}
