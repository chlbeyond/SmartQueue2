package com.smartqueue2.commonview;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import com.smartqueue2.R;

/**
 * 全屏弹窗  可以作为取票的弹窗、锁屏的弹窗
 */
public class FullScreenDialog extends Dialog {

    private Activity mContext;

    public FullScreenDialog(Activity context) {
        super(context);
        this.mContext = context;
    }


    public FullScreenDialog(Activity context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected FullScreenDialog(Activity context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog_fetch_ticket);
//        setCanceledOnTouchOutside(true);
//        initView();
//        getWindow().setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画

        //全屏处理
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        WindowManager wm = mContext.getWindowManager();

        lp.width = wm.getDefaultDisplay().getWidth(); //设置宽度
        getWindow().setAttributes(lp);
    }
}
