package com.smartqueue2.login.Activity.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.smartqueue2.R;
import com.smartqueue2.login.Activity.LoginActivity;
import com.smartqueue2.mainpage.Paidui;

public class LoginFragment extends Fragment implements View.OnClickListener{

    private LoginActivity loginActivity;
    private Context mContext;
    private EditText account_et;
    private EditText password_et;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginActivity = (LoginActivity) getActivity();
        mContext = getContext();
        initView(view);
        return view;
    }

    private void initView(View view) {
        account_et = (EditText) view.findViewById(R.id.account_et);
        password_et = (EditText) view.findViewById(R.id.password_et);
        view.findViewById(R.id.login_tv).setOnClickListener(this);
        view.findViewById(R.id.forget_tv).setOnClickListener(this);
        view.findViewById(R.id.register_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_tv:
                Paidui.toast("登录");
                break;
            case R.id.forget_tv:
                Paidui.toast("忘记密码");
                break;
            case R.id.register_tv:
                loginActivity.currentFragment = loginActivity.LOGINFRAGMENT2;
                loginActivity.switchFragment();
                break;
        }
    }
}
