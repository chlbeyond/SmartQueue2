package com.smartqueue2.commonview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartqueue2.R;
import com.smartqueue2.mainpage.Paidui;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/18.
 */

public class NewDoubleSpinner extends AutoLinearLayout implements View.OnClickListener{

    private Context mContext;
    private NewDoubleSpinner.SpinerPopWindow<Integer> mSpinerPopWindow;
    private List<Integer> list;

    private RelativeLayout leftText_rl;
    private RelativeLayout rightText_rl;

    private TextView tv_beTouch;//拿到被点击的TextView的对象
    private TextView left_tv;
    private TextView right_tv;
    private ImageView left_iv;
    private ImageView right_iv;

    private int index = 0;

    private LinearLayout spinner_ll;
    private TextView createSpinner_tv;

    public NewDoubleSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.new_double_spinner, this);
        initData();
        initView();
    }

    /**
     * 初始View
     */
    private void initView() {
        findViewById(R.id.spinner_ll).setOnClickListener(this);

        leftText_rl = (RelativeLayout) findViewById(R.id.leftText_rl);
        leftText_rl.setOnClickListener(clickListener);
        rightText_rl = (RelativeLayout) findViewById(R.id.rightText_rl);
        rightText_rl.setOnClickListener(clickListener);

        left_tv = (TextView) findViewById(R.id.left_tv);
//        left_tv.setOnClickListener(clickListener);
        right_tv = (TextView) findViewById(R.id.right_tv);
//        right_tv.setOnClickListener(clickListener);

        left_iv = (ImageView) findViewById(R.id.left_iv);
        right_iv = (ImageView) findViewById(R.id.right_iv);

        spinner_ll = (LinearLayout) findViewById(R.id.spinner_ll);
        findViewById(R.id.delete_tv).setOnClickListener(this);

        createSpinner_tv = (TextView) findViewById(R.id.createSpinner_tv);
        createSpinner_tv.setOnClickListener(this);

        mSpinerPopWindow = new NewDoubleSpinner.SpinerPopWindow<>(mContext, list, itemClickListener);
        mSpinerPopWindow.setOnDismissListener(dismissListener);
    }

    /**
     * 监听popupwindow取消
     */
    private PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            setImage(R.mipmap.icon_down);
        }
    };

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            switch (tv_beTouch.getId()) {
                case R.id.left_tv:
                    index = 1;//左边的textView被选中
                    break;
                case R.id.right_tv:
                    index = 2;//右边的textView被选中
                    break;
            }
            if (onSpinnerListener != null) {
                if (onSpinnerListener.onSelect(NewDoubleSpinner.this, tv_beTouch, list.get(position), index)) {
                    tv_beTouch.setText(list.get(position).toString());//返回true才改变
                } else {
                    Paidui.toast("不符合要求");
                }
            }
        }
    };

    /**
     * 显示PopupWindow
     */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.leftText_rl:
                    index = 1;
                    tv_beTouch = left_tv; //将被点击的TextView对象传出
                    mSpinerPopWindow.setWidth(leftText_rl.getWidth());
                    mSpinerPopWindow.showAsDropDown(leftText_rl);
                    setImage(R.mipmap.icon_up);
                    break;
                case R.id.rightText_rl:
                    index = 2;
                    tv_beTouch = right_tv; //将被点击的TextView对象传出
                    mSpinerPopWindow.setWidth(rightText_rl.getWidth());
                    mSpinerPopWindow.showAsDropDown(rightText_rl);
                    setImage(R.mipmap.icon_up);
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {//点击事件，只需确保点击自己的时候，能够实现所需的界面即可
        switch (view.getId()) {
            case R.id.delete_tv:
                if (onSpinnerListener != null) {
                    onSpinnerListener.onDelete(NewDoubleSpinner.this);
                    spinner_ll.setVisibility(GONE);
                    createSpinner_tv.setVisibility(VISIBLE);
                }
                break;
            case R.id.createSpinner_tv:
                createSpinner_tv.setVisibility(GONE);
                spinner_ll.setVisibility(VISIBLE);
                left_tv.setText("0");
                right_tv.setText("0");
                break;
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        list = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            list.add(i);
        }
    }

    /**
     * 给TextView右边的image设置图片
     * @param resId
     */
    private void setImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        if (index == 1) {
            left_iv.setImageDrawable(drawable);
        } else {
            right_iv.setImageDrawable(drawable);
        }
    }


    /**
     * 自定义PopupWindow
     */
    class SpinerPopWindow<T> extends PopupWindow {

        private LayoutInflater inflater;
        private ListView mListView;
        private List<T> list;
        private NewDoubleSpinner.SpinerPopWindow.MyAdapter mAdapter;

        public SpinerPopWindow(Context context, List<T> list, AdapterView.OnItemClickListener clickListener) {
            super(context);
            inflater = LayoutInflater.from(context);
            this.list = list;
            init(clickListener);
        }

        private void init(AdapterView.OnItemClickListener clickListener) {
            View view = inflater.inflate(R.layout.spiner_window_layout, null);
            setContentView(view);
            setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setFocusable(true);
            ColorDrawable dw = new ColorDrawable(0x00);
            setBackgroundDrawable(dw);
            mListView = (ListView) view.findViewById(R.id.listview);
            mListView.setAdapter(mAdapter = new NewDoubleSpinner.SpinerPopWindow.MyAdapter());
            mListView.setOnItemClickListener(clickListener);
        }

        private class MyAdapter extends BaseAdapter {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                NewDoubleSpinner.SpinerPopWindow.ViewHolder holder = null;
                if (convertView == null) {
                    holder = new NewDoubleSpinner.SpinerPopWindow.ViewHolder();
                    convertView = inflater.inflate(R.layout.spiner_item_layout, null);
                    holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                    convertView.setTag(holder);
                } else {
                    holder = (NewDoubleSpinner.SpinerPopWindow.ViewHolder) convertView.getTag();
                }
                holder.tvName.setText(String.valueOf(getItem(position)));
                AutoUtils.autoSize(convertView);
                return convertView;
            }
        }

        private class ViewHolder {
            private TextView tvName;
        }
    }

    public TextView getLeft_tv() {
        return left_tv;
    }

    public TextView getRight_tv() {
        return right_tv;
    }

    public LinearLayout getSpinner_ll() {
        return spinner_ll;
    }

    public TextView getCreateSpinner_tv() {
        return createSpinner_tv;
    }

    //选中回调
    private NewDoubleSpinner.OnSpinnerListener onSpinnerListener;

    public interface OnSpinnerListener {
        /**
         * 将选择回调
         * @param spinner 控件对象
         * @param textView 选中的textView对象
         * @param number 选中的数字
         * @param index 标识左右哪一个textView被选中，1：左边；2：右边
         */
        boolean onSelect(NewDoubleSpinner spinner, TextView textView, int number, int index);

        /**
         * 删除回调
         * @param spinner 控件对象
         */
        void onDelete(NewDoubleSpinner spinner);
    }

    public void setOnSpinnerListener(NewDoubleSpinner.OnSpinnerListener onSpinnerListener) {
        this.onSpinnerListener = onSpinnerListener;
    }
}

