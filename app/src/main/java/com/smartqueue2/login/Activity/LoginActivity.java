package com.smartqueue2.login.Activity;

import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartqueue2.R;
import com.smartqueue2.login.Activity.Fragment.LoginFragment;
import com.smartqueue2.login.Activity.Fragment.QRCodeFragment;
import com.smartqueue2.login.Activity.Fragment.RegisterFragment;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    public static final String QRCODEFRAGMENT = "QRCodeFragment";
    public static final String LOGINFRAGMENT = "LoginFragment";
    public static final String REGISTERFRAGMENT = "RegisterFragment";
    public static final String LOGINFRAGMENT2 = "LoginFragment2";//这个标识是为了在账号登录页面点击"新用户注册"时，能切换到注册页面
    public String currentFragment = QRCODEFRAGMENT;//标识当前fragment的变量

    private TextView switch_tv;
    private TextView textView1;
    private TextView textView2;
    private TextView version_tv;
    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        switchFragment();
    }

    private void initView() {
        findViewById(R.id.switch_ll).setOnClickListener(this);
        switch_tv = (TextView) findViewById(R.id.switch_tv);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        version_tv = (TextView) findViewById(R.id.version_tv);
    }

    public void switchFragment(){
        fragmentManager = getSupportFragmentManager();
        switch (currentFragment) {
            case QRCODEFRAGMENT://切换到账号登录
                currentFragment = LOGINFRAGMENT;
                switch_tv.setText("切换至扫码登录");
                fragmentManager.beginTransaction().replace(R.id.login_fl, new LoginFragment()).commit();
                break;
            case LOGINFRAGMENT://切换到扫码登录
                currentFragment = QRCODEFRAGMENT;
                switch_tv.setText("点此使用账号密码登录");
                fragmentManager.beginTransaction().replace(R.id.login_fl, new QRCodeFragment()).commit();
                break;
            case REGISTERFRAGMENT://切换到账号登录
                currentFragment = LOGINFRAGMENT;
                switch_tv.setText("切换至扫码登录");
                fragmentManager.beginTransaction().replace(R.id.login_fl, new LoginFragment()).commit();
                break;
            case LOGINFRAGMENT2://切换到注册页面
                currentFragment = REGISTERFRAGMENT;
                switch_tv.setText("切换至密码登录");
                fragmentManager.beginTransaction().replace(R.id.login_fl, new RegisterFragment()).commit();
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_ll:
                switchFragment();
                break;
        }
    }

}
