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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.smartqueue2.R;
import com.smartqueue2.mainpage.Paidui;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class DoubleSpinner extends LinearLayout implements View.OnClickListener{

    private Context mContext;
    private SpinerPopWindow<Integer> mSpinerPopWindow;
    private List<Integer> list;
    private TextView tv_beTouch;//拿到被点击的TextView的对象
    private TextView tv_left;
    private TextView tv_left1;
    private TextView tv_right;
    private TextView tv_right1;

    private LinearLayout spinner_ll;
    private LinearLayout operation_ll;
    private TextView delete_tv;

    private TextView createSpinner_tv;

    private boolean isSave = true;//数据是否保存，判断是否显示删除按钮，点击取消按钮有两种显示方式

    public DoubleSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.double_spinner, this);
        initData();
        initView();
    }

    /**
     * 初始View
     */
    private void initView() {
        findViewById(R.id.spinner_ll).setOnClickListener(this);

        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_left.setOnClickListener(clickListener);
        tv_left1 = (TextView)findViewById(R.id.tv_left1);

        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setOnClickListener(clickListener);
        tv_right1 = (TextView) findViewById(R.id.tv_right1);

        spinner_ll = (LinearLayout) findViewById(R.id.spinner_ll);
        operation_ll = (LinearLayout) findViewById(R.id.operation_ll);
        findViewById(R.id.save_tv).setOnClickListener(this);
        findViewById(R.id.cancel_tv).setOnClickListener(this);
        delete_tv = (TextView) findViewById(R.id.delete_tv);
        delete_tv.setOnClickListener(this);

        createSpinner_tv = (TextView) findViewById(R.id.createSpinner_tv);
        createSpinner_tv.setOnClickListener(this);

        mSpinerPopWindow = new SpinerPopWindow<>(mContext, list, itemClickListener);
        mSpinerPopWindow.setOnDismissListener(dismissListener);
    }

    /**
     * 监听popupwindow取消
     */
    private PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            setTextImage(R.mipmap.icon_down);
        }
    };

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            tv_beTouch.setText(list.get(position).toString());
            int index = 0;
            switch (tv_beTouch.getId()) {
                case R.id.tv_left:
                    index = 1;//左边的textView被选中
                    break;
                case R.id.tv_right:
                    index = 2;//右边的textView被选中
                    break;
            }
            if (onSpinnerSelectListener != null) {
                onSpinnerSelectListener.onSpinnerSelect(DoubleSpinner.this, tv_beTouch, list.get(position), index);
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
                case R.id.tv_left:
                    tv_beTouch = tv_left; //将被点击的TextView对象传出
                    mSpinerPopWindow.setWidth(tv_left.getWidth());
                    mSpinerPopWindow.showAsDropDown(tv_left);
                    setTextImage(R.mipmap.icon_up);
                    break;
                case R.id.tv_right:
                    tv_beTouch = tv_right; //将被点击的TextView对象传出
                    mSpinerPopWindow.setWidth(tv_right.getWidth());
                    mSpinerPopWindow.showAsDropDown(tv_right);
                    setTextImage(R.mipmap.icon_up);
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {//点击事件，只需确保点击自己的时候，能够实现所需的界面即可
        switch (view.getId()) {
            case R.id.spinner_ll:
                tv_left.setVisibility(VISIBLE);
                tv_left1.setVisibility(GONE);
                tv_right.setVisibility(VISIBLE);
                tv_right1.setVisibility(GONE);
                operation_ll.setVisibility(VISIBLE);
                if (isSave) {
                    delete_tv.setVisibility(VISIBLE);
                    tv_left.setText(tv_left1.getText());
                    tv_right.setText(tv_right1.getText());
                }
                break;
            case R.id.save_tv:
                if (onSpinnerSaveListener != null) {
                    boolean isSuccess = onSpinnerSaveListener.onSave(DoubleSpinner.this,
                            Integer.parseInt(tv_left.getText().toString()),
                            Integer.parseInt(tv_right.getText().toString()));
                    if (isSuccess) {
                        operation_ll.setVisibility(INVISIBLE);
                        tv_left1.setText(tv_left.getText());
                        tv_left1.setVisibility(VISIBLE);
                        tv_left.setVisibility(GONE);

                        tv_right1.setText(tv_right.getText());
                        tv_right1.setVisibility(VISIBLE);
                        tv_right.setVisibility(GONE);
                        isSave = true;//点击保存
                    } else {
                        Paidui.toast("队列不符合要求");
                    }
                }
                break;
            case R.id.cancel_tv:
                if (isSave) {
                    operation_ll.setVisibility(INVISIBLE);
                    tv_left1.setVisibility(VISIBLE);
                    tv_left.setVisibility(GONE);
                    tv_right1.setVisibility(VISIBLE);
                    tv_right.setVisibility(GONE);
                } else {
                    spinner_ll.setVisibility(GONE);
                    createSpinner_tv.setVisibility(VISIBLE);
                }
                break;
            case R.id.delete_tv:
                onSpinnerDeleteListener.onDelete(DoubleSpinner.this);
                spinner_ll.setVisibility(GONE);
                createSpinner_tv.setVisibility(VISIBLE);
                break;
            case R.id.createSpinner_tv:
                isSave = false;//新创建所以数据肯定未保存
                createSpinner_tv.setVisibility(GONE);
                spinner_ll.setVisibility(VISIBLE);
                tv_left.setVisibility(VISIBLE);
                tv_left.setText("0");
                tv_left1.setVisibility(GONE);
                tv_right.setVisibility(VISIBLE);
                tv_right.setText("0");
                tv_right1.setVisibility(GONE);
                operation_ll.setVisibility(VISIBLE);
                delete_tv.setVisibility(INVISIBLE);
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
     * 给TextView右边设置图片
     *
     * @param resId
     */
    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        tv_beTouch.setCompoundDrawables(null, null, drawable, null);
    }


    /**
     * 自定义PopupWindow
     */
    class SpinerPopWindow<T> extends PopupWindow {

        private LayoutInflater inflater;
        private ListView mListView;
        private List<T> list;
        private SpinerPopWindow.MyAdapter mAdapter;

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
            mListView.setAdapter(mAdapter = new MyAdapter());
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
                ViewHolder holder = null;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.spiner_item_layout, null);
                    holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.tvName.setText(String.valueOf(getItem(position)));
                return convertView;
            }
        }

        private class ViewHolder {
            private TextView tvName;
        }
    }

    public TextView getTv_left() {
        return tv_left;
    }

    public TextView getTv_right() {
        return tv_right;
    }

    public TextView getTv_left1() {
        return tv_left1;
    }

    public TextView getTv_right1() {
        return tv_right1;
    }

    public LinearLayout getSpinner_ll() {
        return spinner_ll;
    }

    public LinearLayout getOperation_ll() {
        return operation_ll;
    }

    public TextView getCreateSpinner_tv() {
        return createSpinner_tv;
    }

    /**
     * 保存回调
     */
    private OnSpinnerSaveListener onSpinnerSaveListener;

    public interface OnSpinnerSaveListener {
        /**
         * 保存回调
         * @param spinner 控件对象
         * @param numLeft 左边控件的数字
         * @param numRight 右边控件的数字
         */
        boolean onSave(DoubleSpinner spinner, int numLeft, int numRight);
    }

    public void setOnSpinnerSaveListener(OnSpinnerSaveListener onSpinnerSaveListener) {
        this.onSpinnerSaveListener = onSpinnerSaveListener;
    }

    /**
     * 删除回调接口
     */
    private OnSpinnerDeleteListener onSpinnerDeleteListener;

    public interface OnSpinnerDeleteListener {
        /**
         * 删除回调
         * @param spinner 控件对象
         */
        void onDelete(DoubleSpinner spinner);
    }

    public void setOnSpinnerDeleteListener(OnSpinnerDeleteListener onSpinnerDeleteListener) {
        this.onSpinnerDeleteListener = onSpinnerDeleteListener;
    }

    //选中回调
    private OnSpinnerSelectListener onSpinnerSelectListener;

    //接口回调选中的控件，文本
    public interface OnSpinnerSelectListener {
        /**
         * 将选择回调
         * @param spinner 控件对象
         * @param textView 选中的textView对象
         * @param number 选中的数字
         * @param index 标识左右哪一个textView被选中，1：左边；2：右边
         */
        void onSpinnerSelect(DoubleSpinner spinner, TextView textView, int number, int index);
    }

    public void setOnSpinnerSelectListener(OnSpinnerSelectListener onSpinnerSelectListener) {
        this.onSpinnerSelectListener = onSpinnerSelectListener;
    }

}
