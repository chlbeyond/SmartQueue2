package com.smartqueue2.login.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.smartqueue2.R;
import com.smartqueue2.mainpage.Activity.QueueActivity;
import com.smartqueue2.mainpage.utils.SharePreferenceUtil;
import com.smartqueue2.mainpage.utils.SmartPosPrivateKey;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置为横屏
        setContentView(R.layout.activity_splash);

        String account = SharePreferenceUtil.getPreference(SplashActivity.this, SmartPosPrivateKey.ACCOUNT, "");
        String password = SharePreferenceUtil.getPreference(SplashActivity.this, SmartPosPrivateKey.PASSWORD, "");
        long id = SharePreferenceUtil.getPreferenceLong(SplashActivity.this, SmartPosPrivateKey.ID, 0);

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password) && id != 0) {
            Intent intent = new Intent(SplashActivity.this, QueueActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, Login3Activity.class);
            startActivity(intent);
            finish();
        }
    }

    //启动页禁用返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
