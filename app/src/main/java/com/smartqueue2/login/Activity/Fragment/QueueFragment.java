package com.smartqueue2.login.Activity.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartqueue2.R;
import com.smartqueue2.commonview.NewDoubleSpinner;
import com.smartqueue2.login.Activity.Login3Activity;
import com.smartqueue2.login.Activity.ShopInfoActivity;
import com.smartqueue2.mainpage.Paidui;
import com.smartqueue2.mainpage.utils.GlobeMethod;
import com.smartqueue2.mainpage.utils.SharePreferenceUtil;
import com.smartqueue2.mainpage.utils.SmartPosPrivateKey;

import java.util.List;

public class QueueFragment extends Fragment implements View.OnClickListener, NewDoubleSpinner.OnSpinnerListener{

    private View view;
    private Context mContext;
    private Login3Activity activity;

    private NewDoubleSpinner spinnerA;
    private NewDoubleSpinner spinnerB;
    private NewDoubleSpinner spinnerC;
    private NewDoubleSpinner spinnerD;
    private NewDoubleSpinner spinnerE;

    private List<Integer> queueList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_queue, container, false);
        mContext = getContext();
        activity = (Login3Activity)getActivity();
        initView();
        initDatas();
        return view;
    }

    private void initView() {
        view.findViewById(R.id.previous_tv).setOnClickListener(this);
        view.findViewById(R.id.next_tv).setOnClickListener(this);
        initSpinner();
    }

    private void initDatas() {
        if (GlobeMethod.getQueueNotZero().size() == 0) {//如果排队设置中没有数据就先初始化
            GlobeMethod.initQueue();//初始化队列数据
        }
        queueList = GlobeMethod.getQueue();//取出文件中的数据
        initData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previous_tv:
                activity.displayView(Login3Activity.SHOP_FRAGMENT);
                break;
            case R.id.next_tv:
                save();
                break;
        }
    }

    private void save() {
        StringBuilder builder = new StringBuilder();
        String[] queueNameArray = new String[]{"A", "B", "C", "D", "E"};
        int count = 0;//队伍数量
        for (int i = 0; i < 5; i++) {
            if (queueList.get(i * 2) != 0 && queueList.get(i * 2 + 1) != 0) {//判断有多少个队伍
                count++;
                builder.append(queueNameArray[i] + ",");//队列名字
            } else if (queueList.get(i * 2) != 0 || queueList.get(i * 2 + 1) != 0) {
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
        } else {
            SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.QUEUE_NUM, count);//存储队伍数量
            SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.QUEUE_NAME, builder.toString());//存储队列名字ABCDE
            SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.A_1, queueList.get(0));
            SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.A_2, queueList.get(1));
            SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.B_1, queueList.get(2));
            SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.B_2, queueList.get(3));
            SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.C_1, queueList.get(4));
            SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.C_2, queueList.get(5));
            SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.D_1, queueList.get(6));
            SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.D_2, queueList.get(7));
            SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.E_1, queueList.get(8));
            SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.E_2, queueList.get(9));
        }

        activity.displayView(Login3Activity.REMARK_FRAGMENT);
    }

    private void initSpinner() {
        spinnerA = (NewDoubleSpinner) view.findViewById(R.id.spinnerA);
        spinnerB = (NewDoubleSpinner) view.findViewById(R.id.spinnerB);
        spinnerC = (NewDoubleSpinner) view.findViewById(R.id.spinnerC);
        spinnerD = (NewDoubleSpinner) view.findViewById(R.id.spinnerD);
        spinnerE = (NewDoubleSpinner) view.findViewById(R.id.spinnerE);

        spinnerA.setOnSpinnerListener(this);
        spinnerB.setOnSpinnerListener(this);
        spinnerC.setOnSpinnerListener(this);
        spinnerD.setOnSpinnerListener(this);
        spinnerE.setOnSpinnerListener(this);
    }

    @Override
    public boolean onSelect(NewDoubleSpinner spinner, TextView textView, int number, int index) {
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
    public void onDelete(NewDoubleSpinner spinner) {
        switch (spinner.getId()) {
            case R.id.spinnerA:
                //删除就置0
                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.A_1, 0);
                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.A_2, 0);
                queueList.set(0, 0);
                queueList.set(1, 0);
                break;
            case R.id.spinnerB:
                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.B_1, 0);
                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.B_2, 0);
                queueList.set(2, 0);
                queueList.set(3, 0);
                break;
            case R.id.spinnerC:
                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.C_1, 0);
                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.C_2, 0);
                queueList.set(4, 0);
                queueList.set(5, 0);
                break;
            case R.id.spinnerD:
                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.D_1, 0);
                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.D_2, 0);
                queueList.set(6, 0);
                queueList.set(7, 0);
                break;
            case R.id.spinnerE:
                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.E_1, 0);
                SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.E_2, 0);
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
            spinnerA.getCreateSpinner_tv().setVisibility(View.VISIBLE);
        }

        if (queueList.get(3) != 0) {
            spinnerB.getLeft_tv().setText(String.valueOf(queueList.get(2)));
            spinnerB.getRight_tv().setText(String.valueOf(queueList.get(3)));
        } else {
            spinnerB.getSpinner_ll().setVisibility(View.GONE);
            spinnerB.getCreateSpinner_tv().setVisibility(View.VISIBLE);
        }

        if (queueList.get(5) != 0) {
            spinnerC.getLeft_tv().setText(String.valueOf(queueList.get(4)));
            spinnerC.getRight_tv().setText(String.valueOf(queueList.get(5)));
        } else {
            spinnerC.getSpinner_ll().setVisibility(View.GONE);
            spinnerC.getCreateSpinner_tv().setVisibility(View.VISIBLE);
        }

        if (queueList.get(7) != 0) {
            spinnerD.getLeft_tv().setText(String.valueOf(queueList.get(6)));
            spinnerD.getRight_tv().setText(String.valueOf(queueList.get(7)));
        } else {
            spinnerD.getSpinner_ll().setVisibility(View.GONE);
            spinnerD.getCreateSpinner_tv().setVisibility(View.VISIBLE);
        }

        if (queueList.get(9) != 0) {
            spinnerE.getLeft_tv().setText(String.valueOf(queueList.get(8)));
            spinnerE.getRight_tv().setText(String.valueOf(queueList.get(9)));
        } else {
            spinnerE.getSpinner_ll().setVisibility(View.GONE);
            spinnerE.getCreateSpinner_tv().setVisibility(View.VISIBLE);
        }
    }
}
