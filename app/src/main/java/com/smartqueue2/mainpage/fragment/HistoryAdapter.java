package com.smartqueue2.mainpage.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartqueue2.R;
import com.smartqueue2.database.ormsql.QueueInfo;
import com.smartqueue2.login.Activity.BaseActivity;
import com.smartqueue2.mainpage.Activity.QueueActivity;
import com.smartqueue2.mainpage.model.UpdateState;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ss on 2016/7/27.
 */
public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private List<QueueInfo> list;
    private QueueActivity activity = null;

    public HistoryAdapter(Context context) {
        this.context = context;
        this.activity = (QueueActivity) context;
    }

    public void setTickets(List<QueueInfo> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(context);
        if (activity.screen_Orientation.equals(BaseActivity.SCREEN_ORIENTATION_LANDSCAPE) && !activity.isQueryTicket) {//横屏
            view = inflater.inflate(R.layout.historylistitem, null);
        } else {//竖屏
            view = inflater.inflate(R.layout.historylistitem_port, null);
        }
//        if (position % 2 == 0) {
//            view.setBackgroundColor(Color.parseColor("#ededed"));
//        }
        TextView textView_history_past_number = (TextView) view.findViewById(R.id.textView_history_past_number);
        TextView textView_history_past_peoplecount = (TextView) view.findViewById(R.id.textView_history_past_peoplecount);
        TextView textView_history_currentstate = (TextView) view.findViewById(R.id.textView_history_currentstate);
        switch (list.get(position).getState()) {
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

        textView_history_calltimes.setText(list.get(position).getCount() + "次");

        textView_history_past_number.setText(list.get(position).getQueueName()+list.get(position).getNum());
        textView_history_past_peoplecount.setText(list.get(position).getPeople() + "人");
//        spfs = context.getSharedPreferences("Paidui", Context.MODE_PRIVATE + Context.MODE_APPEND);
//        final SharedPreferences.Editor editor = spfs.edit();

        if (list.get(position).getPhone().isEmpty()) {
            textView_history_phonenumber.setVisibility(View.GONE);
            imageView_history_callicon.setVisibility(View.GONE);
        } else {

            textView_history_phonenumber.setText(String.valueOf(list.get(position).getPhone()));
        }
        textView_history_arrivetime.setText(getDate(String.valueOf(list.get(position).getCreate().getTime())));
        AutoUtils.autoSize(view);
        return view;
    }
    public String getDate(String unixDate) {
        SimpleDateFormat fm = new SimpleDateFormat("HH:mm");
        return fm.format(new Date(Long.parseLong(unixDate))).toString();
    }

    public String getArrDate(Long unixDate) {
        SimpleDateFormat fm = new SimpleDateFormat("HH:mm");
        int i = (Integer.valueOf(fm.format(new Date(unixDate)).substring(0, 2)) - 8) * 60
                + Integer.valueOf(fm.format(new Date(unixDate)).substring(3, 5));
        return i + "";
    }
}
