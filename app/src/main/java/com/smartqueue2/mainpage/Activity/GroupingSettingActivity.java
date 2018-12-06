package com.smartqueue2.mainpage.Activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import com.smartqueue2.R;
import com.smartqueue2.commonview.DoubleSpinner;
import com.smartqueue2.database.ormsql.BackupHelper;
import com.smartqueue2.database.ormsql.OrmHelper;
import com.smartqueue2.login.Activity.BaseActivity;
import com.smartqueue2.login.Activity.ShopInfoActivity;
import com.smartqueue2.mainpage.Paidui;
import com.smartqueue2.mainpage.utils.GlobeMethod;
import com.smartqueue2.mainpage.utils.SharePreferenceUtil;
import com.smartqueue2.mainpage.utils.SmartPosPrivateKey;

import java.util.List;

//如果点击了确定，那么将删除今天的数据
public class GroupingSettingActivity extends BaseActivity implements DoubleSpinner.OnSpinnerSaveListener,
        DoubleSpinner.OnSpinnerDeleteListener {

    private DoubleSpinner spinnerA;
    private DoubleSpinner spinnerB;
    private DoubleSpinner spinnerC;
    private DoubleSpinner spinnerD;
    private DoubleSpinner spinnerE;

    private List<Integer> queueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouping_setting);

        queueList = GlobeMethod.getQueue();//取出文件中的数据
        initSpinner();
        initData();
    }

    private void initSpinner() {
        spinnerA = (DoubleSpinner) findViewById(R.id.spinnerA);
        spinnerB = (DoubleSpinner) findViewById(R.id.spinnerB);
        spinnerC = (DoubleSpinner) findViewById(R.id.spinnerC);
        spinnerD = (DoubleSpinner) findViewById(R.id.spinnerD);
        spinnerE = (DoubleSpinner) findViewById(R.id.spinnerE);

        spinnerA.setOnSpinnerSaveListener(this);
        spinnerA.setOnSpinnerDeleteListener(this);
        spinnerB.setOnSpinnerSaveListener(this);
        spinnerB.setOnSpinnerDeleteListener(this);
        spinnerC.setOnSpinnerSaveListener(this);
        spinnerC.setOnSpinnerDeleteListener(this);
        spinnerD.setOnSpinnerSaveListener(this);
        spinnerD.setOnSpinnerDeleteListener(this);
        spinnerE.setOnSpinnerSaveListener(this);
        spinnerE.setOnSpinnerDeleteListener(this);
    }

    /*将存储在文件中的排队分组数据读取并赋值给控件显示*/
    private void initData() {
        if (queueList.get(1) != 0) {
            spinnerA.getTv_left1().setText(String.valueOf(queueList.get(0)));
            spinnerA.getTv_right1().setText(String.valueOf(queueList.get(1)));
        } else {
            spinnerA.getSpinner_ll().setVisibility(View.GONE);
            spinnerA.getCreateSpinner_tv().setVisibility(View.VISIBLE);
        }

        if (queueList.get(3) != 0) {
            spinnerB.getTv_left1().setText(String.valueOf(queueList.get(2)));
            spinnerB.getTv_right1().setText(String.valueOf(queueList.get(3)));
        } else {
            spinnerB.getSpinner_ll().setVisibility(View.GONE);
            spinnerB.getCreateSpinner_tv().setVisibility(View.VISIBLE);
        }

        if (queueList.get(5) != 0) {
            spinnerC.getTv_left1().setText(String.valueOf(queueList.get(4)));
            spinnerC.getTv_right1().setText(String.valueOf(queueList.get(5)));
        } else {
            spinnerC.getSpinner_ll().setVisibility(View.GONE);
            spinnerC.getCreateSpinner_tv().setVisibility(View.VISIBLE);
        }

        if (queueList.get(7) != 0) {
            spinnerD.getTv_left1().setText(String.valueOf(queueList.get(6)));
            spinnerD.getTv_right1().setText(String.valueOf(queueList.get(7)));
        } else {
            spinnerD.getSpinner_ll().setVisibility(View.GONE);
            spinnerD.getCreateSpinner_tv().setVisibility(View.VISIBLE);
        }

        if (queueList.get(9) != 0) {
            spinnerE.getTv_left1().setText(String.valueOf(queueList.get(8)));
            spinnerE.getTv_right1().setText(String.valueOf(queueList.get(9)));
        } else {
            spinnerE.getSpinner_ll().setVisibility(View.GONE);
            spinnerE.getCreateSpinner_tv().setVisibility(View.VISIBLE);
        }
    }

    public void clickButton(View v) {
        switch (v.getId()) {
            case R.id.cancel_tv:
                setResult(RESULT_CANCELED);
                break;
            case R.id.certain_tv:

                StringBuilder builder = new StringBuilder();
                String[] queueNameArray = new String[]{"A", "B", "C", "D", "E"};
                int count = 0;
                for (int i = 0; i < 10; i++) {
                    if (queueList.get(++i) != 0) {//取1,3,4,7,9不为0来判断有多少个队伍
                        count++;
                        builder.append(queueNameArray[i] + ",");//队列名字
                    }
                }

                if (count == 0) {
                    Paidui.toast("队伍数量不能为0");
                    return;
                } else {
                    SharePreferenceUtil.saveIntPreference(GroupingSettingActivity.this, SmartPosPrivateKey.QUEUE_NUM, count);//存储队伍数量
                    SharePreferenceUtil.saveStringPreference(GroupingSettingActivity.this, SmartPosPrivateKey.QUEUE_NAME, builder.toString());
                }

                String table = SharePreferenceUtil.getPreference(GroupingSettingActivity.this, SmartPosPrivateKey.TABEL_BEUSED, SmartPosPrivateKey.TABEL_QUEUE);
                if (table.equals(SmartPosPrivateKey.TABEL_QUEUE)) {//将当前正在使用的表的数据删除
                    OrmHelper.getInstance(GroupingSettingActivity.this).deleteAll();
                } else {
                    BackupHelper.getInstance(GroupingSettingActivity.this).deleteAll();
                }
                SharePreferenceUtil.saveIntPreference(GroupingSettingActivity.this, SmartPosPrivateKey.A_1, queueList.get(0));
                SharePreferenceUtil.saveIntPreference(GroupingSettingActivity.this, SmartPosPrivateKey.A_2, queueList.get(1));
                SharePreferenceUtil.saveIntPreference(GroupingSettingActivity.this, SmartPosPrivateKey.B_1, queueList.get(2));
                SharePreferenceUtil.saveIntPreference(GroupingSettingActivity.this, SmartPosPrivateKey.B_2, queueList.get(3));
                SharePreferenceUtil.saveIntPreference(GroupingSettingActivity.this, SmartPosPrivateKey.C_1, queueList.get(4));
                SharePreferenceUtil.saveIntPreference(GroupingSettingActivity.this, SmartPosPrivateKey.C_2, queueList.get(5));
                SharePreferenceUtil.saveIntPreference(GroupingSettingActivity.this, SmartPosPrivateKey.D_1, queueList.get(6));
                SharePreferenceUtil.saveIntPreference(GroupingSettingActivity.this, SmartPosPrivateKey.D_2, queueList.get(7));
                SharePreferenceUtil.saveIntPreference(GroupingSettingActivity.this, SmartPosPrivateKey.E_1, queueList.get(8));
                SharePreferenceUtil.saveIntPreference(GroupingSettingActivity.this, SmartPosPrivateKey.E_2, queueList.get(9));

                setResult(RESULT_OK);
                break;
        }
        finish();
    }

    @Override
    public boolean onSave(DoubleSpinner spinner, int numLeft, int numRight) {
        boolean isSuccess = true;
        if (numLeft >= numRight) {
            return false;
        }
        switch (spinner.getId()) {
            case R.id.spinnerA:
                if ((numRight >= queueList.get(2) && queueList.get(2) != 0) ||
                        (numRight >= queueList.get(4) && queueList.get(4) != 0) ||
                        (numRight >= queueList.get(6) && queueList.get(6) != 0) ||
                        (numRight >= queueList.get(8) && queueList.get(8) != 0)) {
                    isSuccess = false;
                } else {
                    queueList.set(0, numLeft);
                    queueList.set(1, numRight);
                }
                break;
            case R.id.spinnerB:
                if (numLeft <= queueList.get(1) ||
                        (numRight >= queueList.get(4) && queueList.get(4) != 0) ||
                        (numRight >= queueList.get(6) && queueList.get(6) != 0) ||
                        (numRight >= queueList.get(8) && queueList.get(8) != 0)) {
                    isSuccess = false;
                } else {
                    queueList.set(2, numLeft);
                    queueList.set(3, numRight);
                }
                break;
            case R.id.spinnerC:
                if (numLeft <= queueList.get(1) || numLeft <= queueList.get(3) ||
                        (numRight >= queueList.get(6) && queueList.get(6) != 0) ||
                        (numRight >= queueList.get(8) && queueList.get(8) != 0)) {
                    isSuccess = false;
                } else {
                    queueList.set(4, numLeft);
                    queueList.set(5, numRight);
                }
                break;
            case R.id.spinnerD:
                if (numLeft <= queueList.get(1) || numLeft <= queueList.get(3) || numLeft <= queueList.get(5) ||
                        (numRight >= queueList.get(8) && queueList.get(8) != 0)) {
                    isSuccess = false;
                } else {
                    queueList.set(6, numLeft);
                    queueList.set(7, numRight);
                }
                break;
            case R.id.spinnerE:
                if (numLeft <= queueList.get(1) || numLeft <= queueList.get(3) ||
                        numLeft <= queueList.get(5) || numLeft <= queueList.get(7)) {
                    isSuccess = false;
                } else {
                    queueList.set(8, numLeft);
                    queueList.set(9, numRight);
                }
                break;
        }
        return isSuccess;
    }

    @Override
    public void onDelete(DoubleSpinner spinner) {
        switch (spinner.getId()) {
            case R.id.spinnerA:
                queueList.set(0, 0);
                queueList.set(1, 0);
                break;
            case R.id.spinnerB:
                queueList.set(2, 0);
                queueList.set(3, 0);
                break;
            case R.id.spinnerC:
                queueList.set(4, 0);
                queueList.set(5, 0);
                break;
            case R.id.spinnerD:
                queueList.set(6, 0);
                queueList.set(7, 0);
                break;
            case R.id.spinnerE:
                queueList.set(8, 0);
                queueList.set(9, 0);
                break;
        }
    }

}
