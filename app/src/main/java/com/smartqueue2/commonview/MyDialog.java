package com.smartqueue2.commonview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.WindowManager;

import com.smartqueue2.R;

/**
 * Created by Administrator on 2018/6/11.
 */

public class MyDialog extends Dialog {

    private Activity mContext;

    public MyDialog(@NonNull Activity context) {
        super(context);
    }

    public MyDialog(@NonNull Activity context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected MyDialog(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setCanceledOnTouchOutside(true);
    }
}
