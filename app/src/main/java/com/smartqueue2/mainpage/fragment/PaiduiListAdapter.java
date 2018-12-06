package com.smartqueue2.mainpage.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartqueue2.R;
import com.smartqueue2.commonview.TipDialog;
import com.smartqueue2.database.ormsql.BackupHelper;
import com.smartqueue2.database.ormsql.OrmHelper;
import com.smartqueue2.database.ormsql.QueueInfo;
import com.smartqueue2.login.Activity.BaseActivity;
import com.smartqueue2.mainpage.Activity.QueueActivity;
import com.smartqueue2.mainpage.Api.PaiduiHttp;
import com.smartqueue2.mainpage.Paidui;
import com.smartqueue2.mainpage.model.QueueStateParams;
import com.smartqueue2.mainpage.model.UpdateState;
import com.smartqueue2.mainpage.model.VoidModel;
import com.smartqueue2.mainpage.utils.SharePreferenceUtil;
import com.smartqueue2.mainpage.utils.SmartPosPrivateKey;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 此处就餐或过号有一个规则：就餐或过号后，如果这个数据在前五，则把处于他后边的也在前五内的数据发送给后台
 * 并且将去掉该数据后处于数据列表中的下标为5的倍数的数据也发送给后台
 */
public class PaiduiListAdapter extends BaseAdapter {
    private List<QueueInfo> list;
    private Context context;
    public final static int SETONCLICK = 1;
    private QueueActivity activity = null;
    public String TAG = "PaiduiListAdapter";
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch ((Integer) msg.obj) {
                case SETONCLICK:
                    break;
            }
        }
    };

    public PaiduiListAdapter(Context context, List<QueueInfo> list) {
        this.context = context;
        this.activity = (QueueActivity) context;
        this.list = list;
    }

    public void setTickets(List<QueueInfo> list) {
        this.list = list;
    }

    public void addTicket(QueueInfo info) {
        this.list.add(info);
    }

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
    public View getView(final int position, View view, ViewGroup group) {
        // TODO Auto-generated method stub+
        LayoutInflater inflater = LayoutInflater.from(context);

        QueueInfo info = list.get(position);
        switch (info.getState()) {
            case UpdateState.WAITING:
                if (activity.screen_Orientation.equals(BaseActivity.SCREEN_ORIENTATION_LANDSCAPE) && !activity.isQueryTicket) {//横屏且不是查询弹窗
                    view = inflater.inflate(R.layout.listviewitem, null);
                } else {//竖屏
                    view = inflater.inflate(R.layout.listviewitem_port, null);
                }
//                if (position % 2 == 0) {
//                    view.setBackgroundColor(Color.parseColor("#ededed"));//item的背景灰白相间
//                }
                LinearLayout LinearLayout_beused = (LinearLayout) view.findViewById(R.id.LinearLayout_beused);
                LinearLayout LinearLayout_meal = (LinearLayout) view.findViewById(R.id.LinearLayout_meal);
                final LinearLayout LinearLayout_call = (LinearLayout) view.findViewById(R.id.LinearLayout_call);
                TextView textView_phonenumber = (TextView) view.findViewById(R.id.textView_phonenumber);
                TextView textView_peplecount = (TextView) view.findViewById(R.id.textView_peplecount);
                TextView textView_number = (TextView) view.findViewById(R.id.textView_number);
                textView_peplecount.setText(info.getPeople() + "人");
                textView_number.setText(info.getQueueName() + info.getNum());
                final TextView textView_time = (TextView) view.findViewById(R.id.textView_time);
                if (info.getPhone().isEmpty()) {
                    textView_phonenumber.setVisibility(View.GONE);
                } else {
                    textView_phonenumber.setText(info.getPhone());
                }
                final long currentTime = System.currentTimeMillis();
                final long arrTime = info.getCreate().getTime();
                textView_time.setText(Math.round((currentTime - arrTime) / (1000 * 60)) + "分钟");

                LinearLayout_call.setOnClickListener(new OnClickListener() {//叫号

                    @Override
                    public void onClick(View v) {
                        activity.addSound(info.getQueueName() + info.getNum());
                        info.setCount(info.getCount() + 1);//叫号次数增加
                        if (activity.table.equals(SmartPosPrivateKey.TABEL_QUEUE)) {
                            OrmHelper.getInstance(context).update(info);
                        } else {
                            BackupHelper.getInstance(context).update(info);
                        }
                        notifyDataSetChanged();//数据改变，更新界面
                    }
                });

                LinearLayout_meal.setOnClickListener(v -> {//就餐
                    new TipDialog(activity, R.style.dialog, info.getQueueName() + info.getNum() + " 确认就餐？", new TipDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {

                                List<QueueStateParams> paramsList = new ArrayList<>();
                                for (int i = 0; i < list.size(); i++) {
                                    QueueStateParams params = new QueueStateParams();
                                    params.setQueueId(list.get(i).getQueueId());
                                    params.setShopQueueId(list.get(i).getShopQueueId());
                                    params.setShopId(SharePreferenceUtil.getPreferenceLong(activity, SmartPosPrivateKey.ID, 0));
                                    params.setShopName(SharePreferenceUtil.getPreference(activity, SmartPosPrivateKey.SHOP_NAME, ""));
                                    params.setQueueNum(i + 1);
                                    params.setQueueName(list.get(i).getQueueName() + list.get(i).getNum());
                                    params.setState(0);
                                    if (i == position) {//改变当前点击的item的状态，让后台知道是哪个被点击了
                                        params.setState(1);
                                    }
                                    paramsList.add(params);
                                }
                                PaiduiHttp.getInstance().getPaiduiService().updateQueueState(paramsList)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(Schedulers.io())
                                        .map(result -> result)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<Boolean>() {
                                            @Override
                                            public void onCompleted() {
                                                Log.e("TAG", "updateQueueStateCompleted: ");
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                Log.e("TAG", "updateQueueStateError: " + e);
//                                                Paidui.toast("就餐失败！");
                                            }

                                            @Override
                                            public void onNext(Boolean result) {
//                                                Paidui.toast("就餐成功！");
                                            }
                                        });


                                info.setState(UpdateState.REPAST);//改变状态
                                if (activity.table.equals(SmartPosPrivateKey.TABEL_QUEUE)) {
                                    OrmHelper.getInstance(context).update(info);
                                } else {
                                    BackupHelper.getInstance(context).update(info);
                                }
//                                int index = position;//获取到当前所点击的数据处于数据列表的下标index
//                                if ((4 - index) > 0) {//如果index在前5位内
//                                    for (int i = index + 1; i < 5; i++) {//则可以把处于他后边的也在前五内的数据发送给后台
//                                        if (i < list.size()) {
//                                            //将这些数据发送到后台 list.get(i)
//                                            Log.e(TAG, "将这些数据发送到后台: " + i);
//                                        }
//                                    }
//                                }
////                                list.remove(info);//从数据列表中删除数据
//                                //此时应将该数据移除到历史记录中
//                                //将去掉该数据后处于数据列表中的下标为5的倍数的数据也发送给后台
//                                int count = list.size() / 5;//有多少个数据要发送到后台
//                                for (int i = 1; i <= count; i++) {
//                                    //将这些数据发送到后台 list.get(5*i-1)
//                                    Log.e(TAG, "将这些数据发送到后台count: " + (5 * i - 1));
//                                }
                                dialog.dismiss();
//                                activity.groupList.get(activity.position).remove(info);
                                activity.refreshTip();
                                notifyDataSetChanged();//数据改变，更新界面
                            }
                        }
                    }).show();
                });
                LinearLayout_beused.setOnClickListener(new OnClickListener() {//过号

                    @Override
                    public void onClick(View v) {
                        new TipDialog(activity, R.style.dialog, info.getQueueName() + info.getNum() + " 确认过号？", new TipDialog.OnCloseListener() {
                            @Override
                            public void onClick(Dialog dialog, boolean confirm) {
                                if (confirm) {

                                    List<QueueStateParams> paramsList = new ArrayList<>();
                                    for (int i = 0; i < list.size(); i++) {
                                        QueueStateParams params = new QueueStateParams();
                                        params.setQueueId(list.get(i).getQueueId());
                                        params.setShopQueueId(list.get(i).getShopQueueId());
                                        params.setShopId(SharePreferenceUtil.getPreferenceLong(activity, SmartPosPrivateKey.ID, 0));
                                        params.setShopName(SharePreferenceUtil.getPreference(activity, SmartPosPrivateKey.SHOP_NAME, ""));
                                        params.setQueueNum(i + 1);
                                        params.setQueueName(list.get(i).getQueueName() + list.get(i).getNum());
                                        params.setState(0);
                                        if (i == position) {//改变当前点击的item的状态，让后台知道是哪个被点击了
                                            params.setState(2);
                                        }
                                        paramsList.add(params);
                                    }
                                    PaiduiHttp.getInstance().getPaiduiService().updateQueueState(paramsList)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(Schedulers.io())
                                            .map(result -> result)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Observer<Boolean>() {
                                                @Override
                                                public void onCompleted() {
                                                    Log.e("TAG", "updateQueueStateCompleted: ");
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    Log.e("TAG", "updateQueueStateError: " + e);
//                                                    Paidui.toast("过号失败！");
                                                }

                                                @Override
                                                public void onNext(Boolean result) {
//                                                    Paidui.toast("过号成功！");
                                                }
                                            });


                                    info.setState(UpdateState.PASS);//改变状态
                                    if (activity.table.equals(SmartPosPrivateKey.TABEL_QUEUE)) {
                                        OrmHelper.getInstance(context).update(info);
                                    } else {
                                        BackupHelper.getInstance(context).update(info);
                                    }
                                    int index = position;//获取到当前所点击的数据处于数据列表的下标index
                                    if ((4 - index) > 0) {//如果index在前5位内
                                        for (int i = index + 1; i < 5; i++) {//则可以把处于他后边的也在前五内的数据发送给后台
                                            if (i < list.size()) {//判断数据是否有这么多
                                                //将这些数据发送到后台 list.get(i)
                                                Log.e(TAG, "将这些数据发送到后台: " + i);
                                            }
                                        }
                                    }
//                                    list.remove(info);//从数据列表中删除数据
                                    //此时应将该数据移除到历史记录中
                                    //将去掉该数据后处于数据列表中的下标为5的倍数的数据也发送给后台
                                    int count = list.size() / 5;//有多少个数据要发送到后台
                                    for (int i = 1; i <= count; i++) {
                                        //将这些数据发送到后台 list.get(5*i-1)
                                        Log.e(TAG, "将这些数据发送到后台count: " + (5 * i - 1));
                                    }
//                                    activity.groupList.get(activity.position).remove(info);
                                    activity.refreshTip();
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            }
                        }).show();
                    }
                });
                AutoUtils.autoSize(view);
                return view;
            case UpdateState.DELETE:
            case UpdateState.PASS:
            case UpdateState.REPAST://在查询的时候，是有两种布局的
                if (activity.screen_Orientation.equals(BaseActivity.SCREEN_ORIENTATION_LANDSCAPE) && !activity.isQueryTicket) {//横屏
                    view = inflater.inflate(R.layout.historylistitem, null);
                } else {//竖屏
                    view = inflater.inflate(R.layout.historylistitem_port, null);
                }
//                if (position % 2 == 0) {
//                    view.setBackgroundColor(Color.parseColor("#ededed"));
//                }
                TextView textView_history_past_number = (TextView) view.findViewById(R.id.textView_history_past_number);
                TextView textView_history_past_peoplecount = (TextView) view.findViewById(R.id.textView_history_past_peoplecount);
                TextView textView_history_currentstate = (TextView) view.findViewById(R.id.textView_history_currentstate);
                switch (info.getState()) {
                    case UpdateState.PASS:
                        textView_history_currentstate.setText("已过号");
                        break;
                    case UpdateState.REPAST:
                        textView_history_currentstate.setText("已就餐");
                        break;
                }
                TextView textView_history_calltimes = (TextView) view.findViewById(R.id.textView_history_calltimes);
                TextView textView_history_phonenumber = (TextView) view.findViewById(R.id.textView_history_phonenumber);
                ImageView imageView_history_callicon = (ImageView) view.findViewById(R.id.imageView_history_callicon);
                TextView textView_history_arrivetime = (TextView) view.findViewById(R.id.textView_history_arrivetime);
                textView_history_past_number.setText(info.getQueueName() + info.getNum());
                textView_history_past_peoplecount.setText(info.getPeople() + "人");
                textView_history_calltimes.setText(list.get(position).getCount() + "次");
                if (info.getPhone().isEmpty()) {
                    textView_history_phonenumber.setVisibility(View.GONE);
                    imageView_history_callicon.setVisibility(View.GONE);
                } else {
                    textView_history_phonenumber.setText(String.valueOf(info.getPhone()));
                }
                textView_history_arrivetime.setText(getDate(String.valueOf(info.getCreate().getTime())));
        }
        AutoUtils.autoSize(view);
        return view;
    }

    public String getDate(String unixDate) {
        SimpleDateFormat fm = new SimpleDateFormat("HH:mm");
        return fm.format(new Date(Long.parseLong(unixDate))).toString();
    }
}