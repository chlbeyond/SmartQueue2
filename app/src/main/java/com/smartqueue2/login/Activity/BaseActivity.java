package com.smartqueue2.login.Activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.Window;
import android.view.WindowManager;

import com.smartqueue2.mainpage.utils.SharePreferenceUtil;
import com.smartqueue2.mainpage.utils.SmartPosPrivateKey;
import com.zhy.autolayout.AutoLayoutActivity;
import com.zhy.autolayout.config.AutoLayoutConifg;

public class BaseActivity extends AutoLayoutActivity {

//    private MyOrientoinListener myOrientoinListener;
    public static final String SCREEN_ORIENTATION_LANDSCAPE = "landscape";
    public static final String SCREEN_ORIENTATION_PORTRAIT = "portrait";
    public String screen_Orientation = SCREEN_ORIENTATION_LANDSCAPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕常亮
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//保持横屏
//        myOrientoinListener = new MyOrientoinListener(this);
    }

    //参数：横屏的布局，竖屏的布局
    public void setContentLayout(int land_layoutResId, int port_layoutResId) {
        boolean switch_state = SharePreferenceUtil.getBooleanPreference(BaseActivity.this, SmartPosPrivateKey.SWITCHSCREEN, true);
        if (switch_state) {//true就设置为横屏
            //设置为横屏
            AutoLayoutConifg.getInstance().useDynamicDesignSize(1920, 1080).init(this);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置为横屏
            screen_Orientation = SCREEN_ORIENTATION_LANDSCAPE;
            setContentView(land_layoutResId);
        } else {
            //设置为竖屏
            AutoLayoutConifg.getInstance().useDynamicDesignSize(1080, 1920).init(this);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置为竖屏
            screen_Orientation = SCREEN_ORIENTATION_PORTRAIT;
            setContentView(port_layoutResId);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        boolean autoRotateOn = (android.provider.Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1);
//        //检查系统是否开启自动旋转
//        if (autoRotateOn) {
//            myOrientoinListener.enable();
//        } else {
//            myOrientoinListener.disable();
//        }
//    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig){
//        super.onConfigurationChanged(newConfig);
//
//    }


    /**
     * 触发 onConfigurationChanged方法，实现横竖屏切换
     */
    class MyOrientoinListener extends OrientationEventListener {

        public MyOrientoinListener(Context context) {
            super(context);
        }

        public MyOrientoinListener(Context context, int rate) {
            super(context, rate);
        }

        @Override
        public void onOrientationChanged(int orientation) {
//            Log.d(TAG, "orention" + orientation);
            int screenOrientation = getResources().getConfiguration().orientation;
            if (((orientation >= 0) && (orientation < 45)) || (orientation > 315)) {//设置竖屏
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT && orientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
//                    Log.d(TAG, "设置竖屏");
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    screen_Orientation = SCREEN_ORIENTATION_PORTRAIT;
//                    oriBtn.setText("竖屏");
                }
            } else if (orientation > 225 && orientation < 315) { //设置横屏
//                Log.d(TAG, "设置横屏");
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    screen_Orientation = SCREEN_ORIENTATION_LANDSCAPE;
//                    oriBtn.setText("横屏");
                }
            } else if (orientation > 45 && orientation < 135) {// 设置反向横屏
//                Log.d(TAG, "反向横屏");
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    screen_Orientation = SCREEN_ORIENTATION_LANDSCAPE;
//                    oriBtn.setText("反向横屏");
                }
            } else if (orientation > 135 && orientation < 225) {
//                Log.d(TAG, "反向竖屏");
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                    screen_Orientation = SCREEN_ORIENTATION_PORTRAIT;
//                    oriBtn.setText("反向竖屏");
                }
            }
        }
    }
}
