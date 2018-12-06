package com.smartqueue2.login.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.smartqueue2.R;
import com.smartqueue2.mainpage.Api.PaiduiHttp;
import com.smartqueue2.mainpage.Paidui;
import com.smartqueue2.mainpage.model.VoidModel;
import com.smartqueue2.mainpage.utils.SharePreferenceUtil;
import com.smartqueue2.mainpage.utils.SmartPosPrivateKey;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChooseActivity extends BaseActivity {

    private static final String TAG = "ChooseActivity";
    public final int PERMISSIONS_WRITE_REQUEST = 6;
    private AlertDialog dialog;

    private TextView enterLogin;
    private TextView enterLogin2;
    private TextView enterLogin3;
    private TextView enterShopInfo;

    private final String BASE_URL = "http://" + "192.168.1.135" + ":9090/smartpos/";
    private final String register = "229822011000";
    private final String use = "00018000";

    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
//            showHostFragment();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        enterLogin = (TextView) findViewById(R.id.enterLogin);
        enterLogin2 = (TextView) findViewById(R.id.enterLogin2);
        enterLogin3 = (TextView) findViewById(R.id.enterLogin3);
        enterShopInfo = (TextView) findViewById(R.id.enterShopInfo);

        getPermission();

        enterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseActivity.this, LoginActivity.class));
            }
        });

//        enterLogin2.setVisibility(View.VISIBLE);
        enterLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseActivity.this, Login2Activity.class));
            }
        });

        enterLogin3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseActivity.this, Login3Activity.class));
            }
        });

        enterShopInfo.setVisibility(View.VISIBLE);
        enterShopInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseActivity.this, ShopInfoActivity.class);
                intent.putExtra(ShopInfoActivity.FROM, ShopInfoActivity.LONGINACTIVITY);
                startActivity(intent);
            }
        });

        String account = SharePreferenceUtil.getPreference(ChooseActivity.this, SmartPosPrivateKey.ACCOUNT, "");
        String password = SharePreferenceUtil.getPreference(ChooseActivity.this, SmartPosPrivateKey.PASSWORD, "");

//        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
//            Intent intent = new Intent(ChooseActivity.this, Login3Activity.class);
//            intent.putExtra(ShopInfoActivity.FROM, ShopInfoActivity.LONGINACTIVITY);
//            startActivity(intent);
//        } else {
            startActivity(new Intent(ChooseActivity.this, Login3Activity.class));
//        }

    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 检测到没有读写权限,向用户请求
                ActivityCompat.requestPermissions(ChooseActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_WRITE_REQUEST);
            } else {
                //已有读写权限,进行下一步操作
//                showHostFragment();
            }
        } else {
            //系统版本低于6.0 不需要动态申请权限
//            showHostFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                if (b) {
                    // 没有点击"不再提醒",可提示用户跳转去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting(requestCode, false);
                } else
                    // 用户选择了"不再提醒",提示用户可以手动去开启权限
                    showDialogTipUserGoToAppSettting(requestCode, true);
//                    finish();
            } else if (requestCode == PERMISSIONS_WRITE_REQUEST) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(this, "权限请求成功", Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessage(0);
            } else {
                Toast.makeText(ChooseActivity.this, "权限请求失败,请重试", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSettting(int requestCode, boolean never) {

        if (requestCode == PERMISSIONS_WRITE_REQUEST && !never) {
            //不给存储权限
            dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.no_write_permission)
                    .setMessage(R.string.request_write_permission)
                    .setPositiveButton(R.string.open_permission_now, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 跳转到应用设置界面
                            goToAppSetting(PERMISSIONS_WRITE_REQUEST);
                        }
                    })
                    .setNegativeButton(R.string.cancel_permission_now, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setCancelable(false).show();
        } else if (requestCode == PERMISSIONS_WRITE_REQUEST && never) {
            //不给存储权限,且不再提醒
            dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.no_write_permission)
                    .setMessage(R.string.request_write_never_permission)
                    .setPositiveButton(R.string.open_permission_now, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 跳转到应用设置界面
                            goToAppSetting(PERMISSIONS_WRITE_REQUEST);
                        }
                    })
                    .setNegativeButton(R.string.cancel_permission_now, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setCancelable(false).show();
        } else {
            Toast.makeText(ChooseActivity.this, R.string.get_permission_failed, Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    // 跳转到当前应用的设置界面
    private void goToAppSetting(int requestCode) {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSIONS_WRITE_REQUEST) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting(PERMISSIONS_WRITE_REQUEST, false);
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(this, R.string.get_permission_successed, Toast.LENGTH_SHORT).show();
//                    showHostFragment();
                }
            }
        }
    }

}
