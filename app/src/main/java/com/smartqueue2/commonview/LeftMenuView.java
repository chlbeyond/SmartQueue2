package com.smartqueue2.commonview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.smartqueue2.R;
import com.smartqueue2.database.ormsql.BackupHelper;
import com.smartqueue2.database.ormsql.OrmHelper;
import com.smartqueue2.login.Activity.ChooseActivity;
import com.smartqueue2.login.Activity.Login2Activity;
import com.smartqueue2.login.Activity.Login3Activity;
import com.smartqueue2.login.Activity.ShopInfoActivity;
import com.smartqueue2.mainpage.Activity.QueueActivity;
import com.smartqueue2.mainpage.Api.PaiduiHttp;
import com.smartqueue2.mainpage.Paidui;
import com.smartqueue2.mainpage.model.ChangePwParams;
import com.smartqueue2.mainpage.model.ShopInfoResult;
import com.smartqueue2.mainpage.model.UpdateState;
import com.smartqueue2.mainpage.model.UserRegisterResult;
import com.smartqueue2.mainpage.model.VoidModel;
import com.smartqueue2.mainpage.utils.AidlUtil;
import com.smartqueue2.mainpage.utils.GlobeMethod;
import com.smartqueue2.mainpage.utils.SharePreferenceUtil;
import com.smartqueue2.mainpage.utils.SmartPosPrivateKey;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ss on 2016/1/19.
 */
public class LeftMenuView implements View.OnClickListener {
    private static final String CHANGEPASSWORD = "changePassword";//修改密码保存
    private static final String LOCKPASSWORD = "lockPassword";//解锁密码保存
    private static final String UNLOCK = "unlock";//解锁
    private static final String PRINTSET = "printSet";//打印设置

    private String flag = CHANGEPASSWORD;//区别弹窗

    private static final String TAG = "LeftMenuView";
    private Context mContext;
    private QueueActivity activity;

    private MyDialog myDialog;
    private EditText oldPw_et;
    private EditText newPw_et;
    private EditText reNewPw_et;

    private TextView printState_tv;
    private TextView linkType_tv;
    private TextView printSize_tv;

    private FullScreenDialog lockStateDialog;//锁屏状态弹窗

    public LeftMenuView(Context context, QueueActivity activity) {
        this.mContext = context;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.changePassword_ll:
                changePasswordDialog();//修改密码弹窗
                break;
            case R.id.shopSet_ll://门店设置
                intent = new Intent(activity, ShopInfoActivity.class);
                intent.putExtra(ShopInfoActivity.FROM, ShopInfoActivity.QUEUEACTIVITY);
                activity.startActivity(intent);
                break;
            case R.id.printSet_ll://打印机设置弹窗
                printSet();
                break;
            case R.id.emptyQueue_ll:
                emptyQueue();//清空排队数据
                break;
            case R.id.logout_ll://注销
                logout();
                break;
            case R.id.switch_ll://横竖屏切换
                switch_ll();
                break;
            case R.id.save_tv:
                save();//弹窗中的保存按钮
                break;
            case R.id.lock_ll:
                lockDialog();//锁屏弹窗
                break;
            case R.id.lock_state_layout:
                unLockDialog();//解锁弹窗
                break;
            case R.id.printTest_tv:
                printTest();//打印测试
                break;
        }
    }

    //打印机设置弹窗
    private void printSet() {
        flag = PRINTSET;
        myDialog = new MyDialog(activity, R.style.dialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_printset, null);
        printState_tv = (TextView) view.findViewById(R.id.printState_tv);
        linkType_tv = (TextView) view.findViewById(R.id.linkType_tv);
        printSize_tv = (TextView) view.findViewById(R.id.printSize_tv);
        getPrinterState();
        view.findViewById(R.id.printTest_tv).setOnClickListener(this);
        view.findViewById(R.id.save_tv).setOnClickListener(this);
        myDialog.setContentView(view);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }

    //获取打印机状态
    private void getPrinterState() {
        int state = AidlUtil.getInstance().getPrinterState();
        switch (state) {
            case 1:
                printState_tv.setText("正常");
                break;
            case 2:
                printState_tv.setText("更新状态");
                break;
            case 3:
                printState_tv.setText("获取状态异常");
                break;
            case 4:
                printState_tv.setText("缺纸");
                break;
            case 5:
                printState_tv.setText("过热");
                break;
            case 6:
                printState_tv.setText("开盖");
                break;
            case 7:
                printState_tv.setText("切刀异常");
                break;
            case 8:
                printState_tv.setText("切刀恢复");
                break;
            case 9:
                printState_tv.setText("未检测到黑标");
                break;
            default:
                Log.e("state", state + "");
                printState_tv.setText("未知");
        }
    }

    //打印机测试
    private void printTest() {
        if (AidlUtil.getInstance().isUsable()) {
            AidlUtil.getInstance().printShopName("打印测试")
                    .printTicketNum("A1")
                    .printSeparator()
                    .printTableType("A", 1, 4)
                    .printWaitNum(5)
                    .printPeopleNum(4)
                    .printTextLeft("排队时间：" + "2018-06-01 12:00:00")
                    .printTextLeft("温馨提示：过号需要重新取号，敬请谅解")
                    .printTextLeft("备注：" + "这里是备注信息")
                    .printSeparator()
                    .printQr("http://weixin.qq.com/q/02DmZtVNsiddl10000M03y")
                    .end();
        }
    }

    //打印机设置保存
    private void printSetSave() {
        String linkType = linkType_tv.getText().toString().trim();
        String printSize = printSize_tv.getText().toString().trim();
        myDialog.dismiss();
    }

    //清空排队数据
    private void emptyQueue() {
        new TipDialog(activity, R.style.dialog, "是否清空所有排队数据？(此操作不可撤回)", new TipDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    //执行清空排队数据操作
                    if (activity.table.equals(SmartPosPrivateKey.TABEL_QUEUE)) {
                        BackupHelper.getInstance(activity).deleteAll();
                        SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.TABEL_BEUSED, SmartPosPrivateKey.TABEL_BACKUP);
                        activity.table = SmartPosPrivateKey.TABEL_BACKUP;//改变使用的表
                    } else {
                        OrmHelper.getInstance(activity).deleteAll();
                        SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.TABEL_BEUSED, SmartPosPrivateKey.TABEL_QUEUE);
                        activity.table = SmartPosPrivateKey.TABEL_QUEUE;
                    }
                    SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.A_NUM, 0);
                    SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.B_NUM, 0);
                    SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.C_NUM, 0);
                    SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.D_NUM, 0);
                    SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.E_NUM, 0);
                    activity.queueInfoList.clear();//删除数据
                    activity.refreshTip();
                }
            }
        }).show();
    }

    //修改密码弹窗
    private void changePasswordDialog() {
        flag = CHANGEPASSWORD;
        myDialog = new MyDialog(activity, R.style.dialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_changepassword, null);
        oldPw_et = (EditText) view.findViewById(R.id.oldPw_et);
        newPw_et = (EditText) view.findViewById(R.id.newPw_et);
        reNewPw_et = (EditText) view.findViewById(R.id.reNewPw_et);
        view.findViewById(R.id.save_tv).setOnClickListener(this);
        myDialog.setContentView(view);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }

    //修改密码保存
    private void changePassword() {
        String oldPw_str = oldPw_et.getText().toString().trim();
        String newPw_str = newPw_et.getText().toString().trim();
        String reNewPw_str = reNewPw_et.getText().toString().trim();
        String password = SharePreferenceUtil.getPreference(activity, SmartPosPrivateKey.PASSWORD, "");
        if (TextUtils.isEmpty(oldPw_str)) {
            Paidui.toast("输入的原密码不能为空！");
        } else if (!oldPw_str.equals(password)) {
            Paidui.toast("输入的原密码不符合！");
        } else if (TextUtils.isEmpty(newPw_str)) {
            Paidui.toast("新密码不能为空！");
        } else if (TextUtils.isEmpty(reNewPw_str)) {
            Paidui.toast("再次输入新密码不能为空！");
        } else if (!newPw_str.equals(reNewPw_str)) {
            Paidui.toast("两次输入新密码不相同！");
        } else {
            ChangePwParams params = new ChangePwParams();
            params.setId(SharePreferenceUtil.getPreferenceLong(activity, SmartPosPrivateKey.USER, 0));
            params.setPassword(newPw_str);
            PaiduiHttp.getInstance().getPaiduiService().changePw(params)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(result -> result)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onCompleted() {
                            Log.e("TAG", "changePwCompleted: ");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", "changePwError: " + e);
                            Paidui.toast("密码修改失败！");
                        }

                        @Override
                        public void onNext(Boolean result) {
                            Paidui.toast("密码修改成功！");
                            //进行新密码保存
                            SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.PASSWORD, newPw_str);
                            myDialog.dismiss();
                        }
                    });
        }
    }

    //锁屏弹窗
    private void lockDialog() {
        String lockPassword = SharePreferenceUtil.getPreference(activity, SmartPosPrivateKey.LOCKPASSWORD, "");
        if (!TextUtils.isEmpty(lockPassword)) {//已有锁屏密码
            lockState();//进入锁屏状态
        } else {
            flag = LOCKPASSWORD;
            myDialog = new MyDialog(activity, R.style.dialog);
            View view = LayoutInflater.from(activity).inflate(R.layout.dialog_changepassword, null);
            ((TextView) view.findViewById(R.id.title_tv)).setText("请设置解锁密码");
            oldPw_et = (EditText) view.findViewById(R.id.oldPw_et);
            newPw_et = (EditText) view.findViewById(R.id.newPw_et);
            reNewPw_et = (EditText) view.findViewById(R.id.reNewPw_et);
            oldPw_et.setVisibility(View.GONE);
            newPw_et.setHint("请输入解锁密码");
            reNewPw_et.setHint("请再次输入解锁密码");
            view.findViewById(R.id.save_tv).setOnClickListener(this);
            myDialog.setContentView(view);
            myDialog.setCanceledOnTouchOutside(true);
            myDialog.show();
        }
    }

    //解锁密码保存
    private void lockPasswordSave() {
        String newPw_str = newPw_et.getText().toString().trim();
        String reNewPw_str = reNewPw_et.getText().toString().trim();
        if (TextUtils.isEmpty(newPw_str)) {
            Paidui.toast("输入的解锁密码不能为空！");
        } else if (TextUtils.isEmpty(reNewPw_str)) {
            Paidui.toast("再次输入的解锁密码不能为空！");
        } else if (!newPw_str.equals(reNewPw_str)) {
            Paidui.toast("两次输入的解锁密码不相同！");
        } else {
            Paidui.toast("解锁密码设置成功！");
            //进行解锁密码保存
            SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.LOCKPASSWORD, newPw_str);
            myDialog.dismiss();
            lockState();//设置解锁密码成功后进入锁屏状态
        }
    }

    //锁屏状态
    private void lockState() {
        lockStateDialog = new FullScreenDialog(activity, R.style.dialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.lock_state_layout, null);
        view.findViewById(R.id.lock_state_layout).setOnClickListener(this);
        lockStateDialog.setContentView(view);
        lockStateDialog.setCanceledOnTouchOutside(false);
        lockStateDialog.setCancelable(false);//按返回键不能dismiss
        lockStateDialog.show();
    }

    //解锁弹窗
    private void unLockDialog() {
        flag = UNLOCK;
        myDialog = new MyDialog(activity, R.style.dialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_changepassword, null);
        ((TextView) view.findViewById(R.id.title_tv)).setText("请输入解锁密码或登录密码");
        oldPw_et = (EditText) view.findViewById(R.id.oldPw_et);
        newPw_et = (EditText) view.findViewById(R.id.newPw_et);
        reNewPw_et = (EditText) view.findViewById(R.id.reNewPw_et);
        oldPw_et.setHint("请输入解锁密码或登录密码");
        newPw_et.setVisibility(View.GONE);
        reNewPw_et.setVisibility(View.GONE);
        TextView save_tv = (TextView) view.findViewById(R.id.save_tv);
        save_tv.setText("解锁");
        save_tv.setOnClickListener(this);
        myDialog.setContentView(view);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }

    //解锁
    private void unLock() {
        String lockPassword = SharePreferenceUtil.getPreference(activity, SmartPosPrivateKey.LOCKPASSWORD, "");
        String password = SharePreferenceUtil.getPreference(activity, SmartPosPrivateKey.PASSWORD, "");

        String newPw_str = oldPw_et.getText().toString().trim();
        if (TextUtils.isEmpty(newPw_str)) {
            Paidui.toast("输入的密码不能为空！");
        } else if (newPw_str.equals(lockPassword) || newPw_str.equals(password)) {//两个都不等于才无法解锁
            Paidui.toast("解锁成功！");
            myDialog.dismiss();
            lockStateDialog.dismiss();
        } else {
            Paidui.toast("输入的解锁密码或登录密码不正确！");
        }
    }

    //弹窗中的保存按钮
    private void save() {
        switch (flag) {
            case CHANGEPASSWORD:
                changePassword();//修改密码保存
                break;
            case LOCKPASSWORD:
                lockPasswordSave();//解锁密码保存
                break;
            case UNLOCK:
                unLock();//解锁
                break;
            case PRINTSET://打印设置
                printSetSave();
                break;
        }
    }

    //注销登录
    private void logout() {
        new TipDialog(activity, R.style.dialog, "是否注销登录？(此操作不可撤回)", new TipDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
//                    SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.ACCOUNT, "");
//                    SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.PASSWORD, "");
//                    SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.LOCKPASSWORD, "");//锁屏密码
//                    SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.SHOP_NAME, "");
//                    SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.ADDRESS, "");
//                    SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.PHONE, "");
//                    SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.REMARK, "");
//
//                    //执行清空排队数据操作
//                    if (activity.table.equals(SmartPosPrivateKey.TABEL_QUEUE)) {
//                        BackupHelper.getInstance(activity).deleteAll();
//                        SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.TABEL_BEUSED, SmartPosPrivateKey.TABEL_BACKUP);
//                    } else {
//                        OrmHelper.getInstance(activity).deleteAll();
//                        SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.TABEL_BEUSED, SmartPosPrivateKey.TABEL_QUEUE);
//                    }
//                    SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.A_NUM, 0);
//                    SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.B_NUM, 0);
//                    SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.C_NUM, 0);
//                    SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.D_NUM, 0);
//                    SharePreferenceUtil.saveIntPreference(activity, SmartPosPrivateKey.E_NUM, 0);
                    SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.PASSWORD, "");
                    Intent intent = new Intent(activity, Login3Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    activity.finish();

//                    PaiduiHttp.getInstance().getPaiduiService()
//                            .logout(SharePreferenceUtil.getPreferenceLong(activity, SmartPosPrivateKey.USER, 0))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(Schedulers.io())
//                            .map(result -> result)
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new Observer<Boolean>() {
//                                @Override
//                                public void onCompleted() {
//                                    Log.e("TAG", "logoutCompleted: ");
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                    Log.e("TAG", "logoutError: " + e);
//                                    Paidui.toast("注销失败！");
//                                }
//
//                                @Override
//                                public void onNext(Boolean result) {
//                                    Paidui.toast("注销成功！");
//                                    GlobeMethod.emptyData();
////                                    SharePreferenceUtil.saveStringPreference(activity, SmartPosPrivateKey.ACCOUNT, "");
//                                }
//                            });
                }
            }
        }).show();
    }

    //横竖屏切换
    private void switch_ll() {
        //switch_state=true表示当前为横屏
        boolean switch_state = SharePreferenceUtil.getBooleanPreference(activity, SmartPosPrivateKey.SWITCHSCREEN, true);
        String screen = "横屏";
        if (switch_state) {
            screen = "竖屏";
        } else {
            screen = "横屏";
        }

        new TipDialog(activity, R.style.dialog, "是否要切换成" + screen, new TipDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    SharePreferenceUtil.saveBooleanPreference(activity, SmartPosPrivateKey.SWITCHSCREEN, !switch_state);

                    Intent intent = new Intent(activity, QueueActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    activity.finish();
//                    activity.screenChange();
                }
            }
        }).show();
    }
}
