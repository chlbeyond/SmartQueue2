package com.smartqueue2.login.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartqueue2.R;
import com.smartqueue2.mainpage.Paidui;
import com.smartqueue2.mainpage.utils.SharePreferenceUtil;
import com.smartqueue2.mainpage.utils.SmartPosPrivateKey;

public class Login2Activity extends BaseActivity implements View.OnClickListener{

    private LinearLayout switch_ll;
    private TextView scan_tv;
    private TextView account_tv;

    private RelativeLayout scan_rl;
    private LinearLayout login_ll;
    private LinearLayout register_ll;

    private EditText account_et;//登录账号
    private EditText password_et;
    private EditText reAccount_et;//注册账号
    private EditText rePassword_et;
    private EditText reRePassword_et;

    private TextView has_tv;
    private TextView relogin_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.copy_login2);

        initView();
        initData();
    }

    private void initView() {
        switch_ll = (LinearLayout) findViewById(R.id.switch_ll);
        scan_tv = (TextView) findViewById(R.id.scan_tv);
        account_tv = (TextView) findViewById(R.id.account_tv);
        scan_rl = (RelativeLayout) findViewById(R.id.scan_rl);
        login_ll = (LinearLayout) findViewById(R.id.login_ll);
        register_ll = (LinearLayout) findViewById(R.id.register_ll);

        findViewById(R.id.toReLogin_ll).setOnClickListener(this);//直接注册/直接登录
        has_tv = (TextView) findViewById(R.id.has_tv);
        relogin_tv = (TextView) findViewById(R.id.relogin_tv);

        account_et = (EditText) findViewById(R.id.account_et);
        password_et = (EditText) findViewById(R.id.password_et);
        findViewById(R.id.login_tv).setOnClickListener(this);//登录
        reAccount_et = (EditText) findViewById(R.id.reAccount_et);
        rePassword_et = (EditText) findViewById(R.id.rePassword_et);
        reRePassword_et = (EditText) findViewById(R.id.reRePassword_et);
        findViewById(R.id.register_tv).setOnClickListener(this);//注册

        scan_tv.setOnClickListener(this);
        account_tv.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_tv://扫码登录
                scan_rl.setVisibility(View.VISIBLE);
                login_ll.setVisibility(View.GONE);
                scan_tv.setTextColor(Color.parseColor("#ffffff"));
                scan_tv.setBackgroundResource(R.drawable.shape_qrlogin_text_blue_bg);
                account_tv.setTextColor(Color.parseColor("#adadad"));
                account_tv.setBackgroundResource(R.drawable.shape_accountlogin_text_gray_bg);
                break;
            case R.id.account_tv://账号密码登录
                scan_rl.setVisibility(View.GONE);
                login_ll.setVisibility(View.VISIBLE);
                scan_tv.setTextColor(Color.parseColor("#adadad"));
                scan_tv.setBackgroundResource(R.drawable.shape_sqlogin_text_gray_bg);
                account_tv.setTextColor(Color.parseColor("#ffffff"));
                account_tv.setBackgroundResource(R.drawable.shape_accountlogin_text_blue_bg);
                break;
            case R.id.toReLogin_ll:
                int visib = switch_ll.getVisibility();
                if (visib == View.VISIBLE) {//如果切换按钮可见，表示在登录页面，则此时要去注册
                    switch_ll.setVisibility(View.GONE);
                    scan_rl.setVisibility(View.GONE);
                    login_ll.setVisibility(View.GONE);
                    register_ll.setVisibility(View.VISIBLE);
                    has_tv.setText("已有账号？");
                    relogin_tv.setText("直接登录");
                } else if (visib == View.GONE) {//如果切换按钮GONE状态，表示在注册页面，则此时要去登录页面
                    switch_ll.setVisibility(View.VISIBLE);
                    scan_rl.setVisibility(View.GONE);
                    login_ll.setVisibility(View.VISIBLE);
                    register_ll.setVisibility(View.GONE);
                    scan_tv.setTextColor(Color.parseColor("#adadad"));
                    scan_tv.setBackgroundResource(R.drawable.shape_sqlogin_text_gray_bg);
                    account_tv.setTextColor(Color.parseColor("#ffffff"));
                    account_tv.setBackgroundResource(R.drawable.shape_accountlogin_text_blue_bg);
                    has_tv.setText("还没账号？");
                    relogin_tv.setText("直接注册");
                }
                break;
            case R.id.login_tv://登录
                login();
                break;
            case R.id.register_tv://注册
                register();
                break;
        }
    }

    private void login() {
        String account_str = account_et.getText().toString().trim();
        String password_str = password_et.getText().toString().trim();
        String account = SharePreferenceUtil.getPreference(Login2Activity.this, SmartPosPrivateKey.ACCOUNT, "");
        String password = SharePreferenceUtil.getPreference(Login2Activity.this, SmartPosPrivateKey.PASSWORD, "");
        if (TextUtils.isEmpty(account_str)) {
            Paidui.toast("账号不能为空！");
        } else if (TextUtils.isEmpty(password_str)) {
            Paidui.toast("密码不能为空！");
        } else if (!account.equals(account_str)) {
            Paidui.toast("账号不存在！");
        } else if (!password.equals(password_str)) {
            Paidui.toast("密码输入错误！");
        } else {
            Paidui.toast("登录成功！");
            Intent intent = new Intent(Login2Activity.this, ShopInfoActivity.class);
            intent.putExtra(ShopInfoActivity.FROM, ShopInfoActivity.LONGINACTIVITY);
            startActivity(intent);
            finish();
        }
    }

    private void register() {
        String reAccount_str = reAccount_et.getText().toString().trim();
        String rePassword_str = rePassword_et.getText().toString().trim();
        String reRePassword_str = reRePassword_et.getText().toString().trim();

        if (TextUtils.isEmpty(reAccount_str)) {
            Paidui.toast("账号不能为空！");
        } else if (TextUtils.isEmpty(rePassword_str)) {
            Paidui.toast("密码不能为空！");
        } else if (TextUtils.isEmpty(reRePassword_str)) {
            Paidui.toast("再次输入密码不能为空！");
        } else if (!rePassword_str.equals(reRePassword_str)) {
            Paidui.toast("两次输入密码不相同！");
        } else {
            Paidui.toast("注册成功！");
            SharePreferenceUtil.saveStringPreference(Login2Activity.this, SmartPosPrivateKey.ACCOUNT, reAccount_str);
            SharePreferenceUtil.saveStringPreference(Login2Activity.this, SmartPosPrivateKey.PASSWORD, rePassword_str);
            Intent intent = new Intent(Login2Activity.this, ShopInfoActivity.class);
            intent.putExtra(ShopInfoActivity.FROM, ShopInfoActivity.LONGINACTIVITY);
            startActivity(intent);
            finish();
        }
    }

}
