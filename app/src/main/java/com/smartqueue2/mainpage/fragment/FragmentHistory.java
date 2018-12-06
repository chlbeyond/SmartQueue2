package com.smartqueue2.mainpage.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.smartqueue2.R;
import com.smartqueue2.database.ormsql.QueueInfo;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.Collections;
import java.util.List;

public class FragmentHistory extends Fragment {
    public static final String TAG = " Fragment History";
    private SharedPreferences spfs;
    private ListView listView;
    private View view;
    private List<QueueInfo> list;
    private HistoryAdapter mAdapter;

    public void setTickets(List<QueueInfo> list) {
        this.list = list;
        if (mAdapter != null) {
            mAdapter.setTickets(list);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment__history, null);
        listView = (ListView) view.findViewById(R.id.listView_history);
        mAdapter = new HistoryAdapter(getActivity());
        listView.setAdapter(mAdapter);
        AutoUtils.autoSize(view);
        return view;
    }

    public void refresh() {
        if (mAdapter != null) {
            Collections.sort(list, (queueInfo, t1) -> queueInfo.getCreate().compareTo(t1.getCreate()) * -1);
            mAdapter.notifyDataSetChanged();
        }
    }


}
