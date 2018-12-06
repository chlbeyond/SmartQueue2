package com.smartqueue2.mainpage.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.smartqueue2.R;
import com.smartqueue2.database.ormsql.QueueInfo;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

public class FragmentTable extends Fragment {
    private View view;
    private ListView mListView;
    public PaiduiListAdapter mAdapter;
    private List<QueueInfo> list = new ArrayList<>();
    public int queueId;

    public FragmentTable() {}

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment__atable, null);
        init();
        AutoUtils.autoSize(view);
        return view;
    }

    public void init() {
        mListView = (ListView) view.findViewById(R.id.listView_table);
        mAdapter = new PaiduiListAdapter(getActivity(), list);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setTickets(List<QueueInfo> list) {
        this.list = list;
        if (mAdapter != null) {
            mAdapter.setTickets(list);
        }
    }

    public void addTicket(QueueInfo info) {
        if (this.mAdapter != null) {
            this.mAdapter.addTicket(info);
        }
    }

    public void refresh() {
        if (this.mAdapter != null) {
            this.mAdapter.notifyDataSetChanged();
        }
    }
}