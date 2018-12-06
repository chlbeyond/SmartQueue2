package com.smartqueue2.login.Activity;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.smartqueue2.R;
import com.smartqueue2.login.Activity.Fragment.Login3Fragment;
import com.smartqueue2.login.Activity.Fragment.QueueFragment;
import com.smartqueue2.login.Activity.Fragment.Register3Fragment;
import com.smartqueue2.login.Activity.Fragment.RemarkFragment;
import com.smartqueue2.login.Activity.Fragment.ShopFragment;
import com.smartqueue2.mainpage.utils.DeviceUtils;
import com.smartqueue2.mainpage.utils.SharePreferenceUtil;
import com.smartqueue2.mainpage.utils.SmartPosPrivateKey;
import com.zhy.autolayout.config.AutoLayoutConifg;

public class Login3Activity extends CheckPermissionsActivity {

    public final static int LOGIN_FRAGMENT = 0;//登录页面
    public final static int REGISTER_FRAGMENT = 1;//注册页面
    public final static int SHOP_FRAGMENT = 2;//门店信息页面
    public final static int QUEUE_FRAGMENT = 3;//排队信息页面
    public final static int REMARK_FRAGMENT = 4;//备注页面
    public static int CURRENT_FRAGMENT = LOGIN_FRAGMENT;//当前页面

    public Login3Fragment login3Fragment;
    public Register3Fragment register3Fragment;
    public ShopFragment shopFragment;
    public QueueFragment queueFragment;
    public RemarkFragment remarkFragment;

    private TextView version_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentLayout(R.layout.activity_login_land, R.layout.activity_login_port);
        AutoLayoutConifg.getInstance().useDynamicDesignSize(1920, 1080).init(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置为横屏
        setContentView(R.layout.activity_login_land);

        initView();
        displayView(LOGIN_FRAGMENT);
    }

    private void initView() {
        version_tv = (TextView) findViewById(R.id.version_tv);
        version_tv.setText(DeviceUtils.getVersionName(this) + "." + DeviceUtils.getVersionCode(this));
    }

    public void displayView(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideView(ft);
        switch (position) {
            case LOGIN_FRAGMENT:
                if (login3Fragment != null) {
                    ft.show(login3Fragment);
                } else {
                    login3Fragment = new Login3Fragment();
                    ft.add(R.id.frame_container, login3Fragment);
                }
                CURRENT_FRAGMENT = LOGIN_FRAGMENT;
                break;
            case REGISTER_FRAGMENT:
                if (register3Fragment != null) {
                    ft.show(register3Fragment);
                } else {
                    register3Fragment = new Register3Fragment();
                    ft.add(R.id.frame_container, register3Fragment);
                }
                CURRENT_FRAGMENT = REGISTER_FRAGMENT;
                break;
            case SHOP_FRAGMENT:
                if (shopFragment != null) {
                    ft.show(shopFragment);
                } else {
                    shopFragment = new ShopFragment();
                    ft.add(R.id.frame_container, shopFragment);
                }
                CURRENT_FRAGMENT = SHOP_FRAGMENT;
                break;
            case QUEUE_FRAGMENT:
                if (queueFragment != null) {
                    ft.show(queueFragment);
                } else {
                    queueFragment = new QueueFragment();
                    ft.add(R.id.frame_container, queueFragment);
                }
                CURRENT_FRAGMENT = QUEUE_FRAGMENT;
                break;
            case REMARK_FRAGMENT:
                if (remarkFragment != null) {
                    ft.show(remarkFragment);
                } else {
                    remarkFragment = new RemarkFragment();
                    ft.add(R.id.frame_container, remarkFragment);
                }
                CURRENT_FRAGMENT = REMARK_FRAGMENT;
                break;
        }
        ft.commitAllowingStateLoss();
    }

    public void hideView(FragmentTransaction ft) {
        if (login3Fragment != null) {
            ft.hide(login3Fragment);
        }
        if (register3Fragment != null) {
            ft.hide(register3Fragment);
        }
        if (shopFragment != null) {
            ft.hide(shopFragment);
        }
        if (queueFragment != null) {
            ft.hide(queueFragment);
        }
        if (remarkFragment != null) {
            ft.hide(remarkFragment);
        }
    }

    //因为屏幕旋转只能够重新创建Fragment对象了
    public void displayViewForRotate(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case LOGIN_FRAGMENT:
                if (login3Fragment != null) {
                    login3Fragment = null;
                }
                login3Fragment = new Login3Fragment();
                ft.replace(R.id.frame_container, login3Fragment);
                CURRENT_FRAGMENT = LOGIN_FRAGMENT;
                break;
            case REGISTER_FRAGMENT:
                if (register3Fragment != null) {
                    register3Fragment = null;
                }
                register3Fragment = new Register3Fragment();
                ft.replace(R.id.frame_container, register3Fragment);
                CURRENT_FRAGMENT = REGISTER_FRAGMENT;
                break;
            case SHOP_FRAGMENT:
                if (shopFragment != null) {
                    shopFragment = null;
                }
                shopFragment = new ShopFragment();
                ft.replace(R.id.frame_container, shopFragment);
                CURRENT_FRAGMENT = SHOP_FRAGMENT;
                break;
            case QUEUE_FRAGMENT:
                if (queueFragment != null) {
                    queueFragment = null;
                }
                queueFragment = new QueueFragment();
                ft.replace(R.id.frame_container, queueFragment);
                CURRENT_FRAGMENT = QUEUE_FRAGMENT;
                break;
            case REMARK_FRAGMENT:
                if (remarkFragment != null) {
                    remarkFragment = null;
                }
                remarkFragment = new RemarkFragment();
                ft.replace(R.id.frame_container, remarkFragment);
                CURRENT_FRAGMENT = REMARK_FRAGMENT;
                break;
        }
        ft.commitAllowingStateLoss();
    }

}
