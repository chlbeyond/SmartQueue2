package com.smartqueue2.mainpage.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lhh.apst.library.CustomPagerSlidingTabStrip;
import com.lhh.apst.library.ViewHolder;
import com.smartqueue2.R;
import com.smartqueue2.commonview.FullScreenDialog;
import com.smartqueue2.commonview.HackyDrawerLayout;
import com.smartqueue2.commonview.LeftMenuView;
import com.smartqueue2.commonview.MyDialog;
import com.smartqueue2.database.ormsql.BackupHelper;
import com.smartqueue2.database.ormsql.OrmHelper;
import com.smartqueue2.database.ormsql.QueueInfo;
import com.smartqueue2.login.Activity.BaseActivity;
import com.smartqueue2.login.Activity.ChooseActivity;
import com.smartqueue2.mainpage.Api.PaiduiHttp;
import com.smartqueue2.mainpage.Paidui;
import com.smartqueue2.mainpage.fragment.FragmentHistory;
import com.smartqueue2.mainpage.fragment.FragmentTable;
import com.smartqueue2.mainpage.fragment.PaiduiListAdapter;
import com.smartqueue2.mainpage.model.FetchQueue;
import com.smartqueue2.mainpage.model.FetchTicketParams;
import com.smartqueue2.mainpage.model.QrCode;
import com.smartqueue2.mainpage.model.Queue;
import com.smartqueue2.mainpage.model.QueueStateParams;
import com.smartqueue2.mainpage.model.Queues;
import com.smartqueue2.mainpage.model.ShopInfo;
import com.smartqueue2.mainpage.model.ShopUpdateParams;
import com.smartqueue2.mainpage.model.UpdateState;
import com.smartqueue2.mainpage.model.VoidModel;
import com.smartqueue2.mainpage.model.WeiXinMsg;
import com.smartqueue2.mainpage.utils.AidlUtil;
import com.smartqueue2.mainpage.utils.GlobeMethod;
import com.smartqueue2.mainpage.utils.SharePreferenceUtil;
import com.smartqueue2.mainpage.utils.SmartPosPrivateKey;
import com.zhy.autolayout.config.AutoLayoutConifg;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//关于备份表：现在是有两张表，一张表保存今天的排队数据，一张表保存昨天的排队数据。
//关于使用两张表的规则：当表1用来存储今天数据时，表2用来查询昨天的数据。当表2用来存储今天数据时，表1用来查询昨天的数据。也就是两张表轮流使用
//用文件来保存今天使用的是哪张表，当日结时，清空存储昨天数据的表，用来明天使用。而今天的表，用来查询作用。
public class QueueActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    public boolean isQueryTicket = false;//是否查询号码

    private Context mContext;
    private String shopName;//商店名字
    private String address;//商店名字
    private int queueNum = 0;//如果队列数量为0,不让其进入

    private HackyDrawerLayout drawerLayout;
    public LeftMenuView mLeftMenuView;
//    //侧滑的商店logo、 名字
//    private ImageView logo_iv;
//    private TextView shopName_tv;

    //initRight_ll
    private LinearLayout query_ll;
    private TextView query_tv;
    private LinearLayout peoplecount_ll;
    private TextView peoplecount_tv;
    private LinearLayout phone_ll;
    private TextView phone_tv;
    private TextView fetch_tv;

    //initTab
    private CustomPagerSlidingTabStrip tabStrip;
    private ViewPager mViewPager;
    private List<Fragment> mFragments;
    private FragmentHistory historyFragment;
    private PagerAdapter myPagerAdapter;
    public int position = 0;//标志当前队列，横竖屏切换时，需要知道显示哪个队列

    private int edit = 0;//标记区别 就餐人数0、手机号码1、查询号码2
    private String editString = "";
    public String table;//标志使用哪张表
    public List<QueueInfo> queueInfoList;//所有排队数据
    public List<List<QueueInfo>> groupList = new ArrayList<List<QueueInfo>>();//每一队列的排队数据
    private List<Integer> list;
    private List<Integer> listNotZero;
    private String queueName;
    public String[] queueNameArray;
    public static final int REQUESTCODE = 1000;//请求码
    private LinearLayout fetch_ll;
    private FullScreenDialog ticketDialog;
    private TextView shopName_tv_dialog;
    private LinearLayout query_ll_dialog;
    private TextView query_tv_dialog;
    private LinearLayout peoplecount_ll_dialog;
    private TextView peoplecount_tv_dialog;
    private LinearLayout phone_ll_dialog;
    private TextView phone_tv_dialog;
    private TextView fetch_tv_dialog;
    LinearLayout changePassword_ll;
    LinearLayout shopSet_ll;
    LinearLayout printSet_ll;
    LinearLayout emptyQueue_ll;
    LinearLayout logout_ll;
    LinearLayout switch_ll;

    private BlockingQueue<String> broadcastTexts = new LinkedBlockingQueue<String>();

    Thread playSouncTherad = new Thread(new Runnable() {
        @Override
        public void run() {
            boolean isFinish = true;
            while (true) {
                try {
                    if (isFinish) {
                        String text = broadcastTexts.take();
                        isFinish = false;
                        for (int i = 0; i < 2; i++) {
                            switch (text.substring(0, 1).hashCode()) {
                                case 65:
                                    createMediaPlayForSound(R.raw.a).start();
                                    break;
                                case 66:
                                    createMediaPlayForSound(R.raw.b).start();
                                    break;
                                case 67:
                                    createMediaPlayForSound(R.raw.c).start();
                                    break;
                                case 68:
                                    createMediaPlayForSound(R.raw.d).start();
                                    break;
                                case 69:
                                    createMediaPlayForSound(R.raw.e).start();
                                    break;
                                case 70:
                                    createMediaPlayForSound(R.raw.f).start();
                                    break;
                                case 71:
                                    createMediaPlayForSound(R.raw.g).start();
                                    break;
                                case 72:
                                    createMediaPlayForSound(R.raw.h).start();
                                    break;
                                case 73:
                                    createMediaPlayForSound(R.raw.i).start();
                                    break;
                                default:
                                    break;
                            }
                            Thread.sleep(300);
                            for (char b : text.substring(1, text.length()).toCharArray()) {
                                switch (b) {
                                    case 48:
                                        createMediaPlayForSound(R.raw.n0).start();
                                        break;
                                    case 49:
                                        createMediaPlayForSound(R.raw.n1).start();
                                        break;
                                    case 50:
                                        createMediaPlayForSound(R.raw.n2).start();
                                        break;
                                    case 51:
                                        createMediaPlayForSound(R.raw.n3).start();
                                        break;
                                    case 52:
                                        createMediaPlayForSound(R.raw.n4).start();
                                        break;
                                    case 53:
                                        createMediaPlayForSound(R.raw.n5).start();
                                        break;
                                    case 54:
                                        createMediaPlayForSound(R.raw.n6).start();
                                        break;
                                    case 55:
                                        createMediaPlayForSound(R.raw.n7).start();
                                        break;
                                    case 56:
                                        createMediaPlayForSound(R.raw.n8).start();
                                        break;
                                    case 57:
                                        createMediaPlayForSound(R.raw.n9).start();
                                        break;
                                }
                                Thread.sleep(300);
                            }
                            createMediaPlayForSound(R.raw.end).start();
                            Thread.sleep(1000);
                        }
                        isFinish = true;
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_queue_land, R.layout.activity_queue_port);
        mContext = getApplicationContext();
        playSouncTherad.start();
        AidlUtil.getInstance().initPrinter();//初始化打印机
        shopName = SharePreferenceUtil.getPreference(QueueActivity.this, SmartPosPrivateKey.SHOP_NAME, "");//商店名字
        address = SharePreferenceUtil.getPreference(QueueActivity.this, SmartPosPrivateKey.ADDRESS, "");//商店名字
        queueNum = SharePreferenceUtil.getPreferenceInteger(QueueActivity.this, SmartPosPrivateKey.QUEUE_NUM, 0);//获取队列数量
        queueName = SharePreferenceUtil.getPreference(QueueActivity.this, SmartPosPrivateKey.QUEUE_NAME, "");
        queueNameArray = queueName.split(",");
        list = GlobeMethod.getQueue();//将每个队列的大小值都保存在list中
        listNotZero = GlobeMethod.getQueueNotZero();
        table = SharePreferenceUtil.getPreference(QueueActivity.this, SmartPosPrivateKey.TABEL_BEUSED, SmartPosPrivateKey.TABEL_QUEUE);
        if (table.equals(SmartPosPrivateKey.TABEL_QUEUE)) {
            queueInfoList = OrmHelper.getInstance(QueueActivity.this).queryAll();//查询排队表全部数据
        } else {
            queueInfoList = BackupHelper.getInstance(QueueActivity.this).queryAll();//查询备份表全部数据
        }
        initNav();
        initDrawerLayout();
        initRight_ll();

        initFetch();
        initTab();
        initData();
//        initPrint();
//        initPollListener();
//        queryTicket();
    }

    //竖屏取票
    private void initFetch() {
        fetch_ll = (LinearLayout) findViewById(R.id.fetch_ll);
        if (fetch_ll != null) {
            fetch_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ticketDialog = new FullScreenDialog(QueueActivity.this, R.style.dialog);
                    View dialogView = LayoutInflater.from(QueueActivity.this).inflate(R.layout.dialog_fetch_ticket, null);
                    shopName_tv_dialog = (TextView) dialogView.findViewById(R.id.shopName_tv);
                    shopName_tv_dialog.setText(shopName);
                    query_ll_dialog = (LinearLayout) dialogView.findViewById(R.id.query_ll);
                    query_tv_dialog = (TextView) dialogView.findViewById(R.id.query_tv);
                    peoplecount_ll_dialog = (LinearLayout) dialogView.findViewById(R.id.peoplecount_ll);
                    peoplecount_tv_dialog = (TextView) dialogView.findViewById(R.id.peoplecount_tv);
                    phone_ll_dialog = (LinearLayout) dialogView.findViewById(R.id.phone_ll);
                    phone_tv_dialog = (TextView) dialogView.findViewById(R.id.phone_tv);
                    fetch_tv_dialog = (TextView) dialogView.findViewById(R.id.fetch_tv);
                    ticketDialog.setContentView(dialogView);
                    ticketDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
                    ticketDialog.show();
                }
            });
        }
    }

    private void initNav() {
        ((TextView) findViewById(R.id.textView_main_title)).setText(shopName);
    }

    private void initTab() {
        initTip();//先初始化tip上的数据
        tabStrip = (CustomPagerSlidingTabStrip) findViewById(R.id.tabStrip);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mFragments = new ArrayList<>();
        for (int i = 0; i < queueNum; i++) {
            FragmentTable fragmentTable = new FragmentTable();
            fragmentTable.setQueueId(i);//这句没用吧
            mFragments.add(fragmentTable);
        }
        historyFragment = new FragmentHistory();
        mFragments.add(historyFragment);
        myPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(myPagerAdapter);
        mViewPager.setCurrentItem(position);//设置当前显示队列，为横竖屏切换
        mViewPager.setOffscreenPageLimit(queueNum); //SanyiSDK.rest.queues.size()这个的值不包含历史队伍
        tabStrip.setViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            //每次滑动viewpager都要请求一次数据
            @Override
            public void onPageSelected(int position) {
//                queryTicket();
                initData();
                QueueActivity.this.position = position;//将当前position传出，为横竖屏切换选择队列，不过现在似乎已经没作用了？
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initRight_ll() {
        query_ll = (LinearLayout) findViewById(R.id.query_ll);
        query_tv = (TextView) findViewById(R.id.query_tv);
        peoplecount_ll = (LinearLayout) findViewById(R.id.peoplecount_ll);
        peoplecount_tv = (TextView) findViewById(R.id.peoplecount_tv);
        phone_ll = (LinearLayout) findViewById(R.id.phone_ll);
        phone_tv = (TextView) findViewById(R.id.phone_tv);
        fetch_tv = (TextView) findViewById(R.id.fetch_tv);
    }

    private void initDrawerLayout() {
        ((TextView) findViewById(R.id.shopName_tv)).setText(shopName);

        mLeftMenuView = new LeftMenuView(mContext, this);//这个类处理DrawerLayout中的点击事件
        drawerLayout = (HackyDrawerLayout) findViewById(R.id.drawerlayout);
        if (drawerLayout != null) {
            changePassword_ll = (LinearLayout) drawerLayout.findViewById(R.id.changePassword_ll);
            shopSet_ll = (LinearLayout) drawerLayout.findViewById(R.id.shopSet_ll);
            printSet_ll = (LinearLayout) drawerLayout.findViewById(R.id.printSet_ll);
            emptyQueue_ll = (LinearLayout) drawerLayout.findViewById(R.id.emptyQueue_ll);
            logout_ll = (LinearLayout) drawerLayout.findViewById(R.id.logout_ll);
            switch_ll = (LinearLayout) drawerLayout.findViewById(R.id.switch_ll);

            if (changePassword_ll != null) {
                changePassword_ll.setOnClickListener(this);
                shopSet_ll.setOnClickListener(this);
                printSet_ll.setOnClickListener(this);
                emptyQueue_ll.setOnClickListener(this);
                logout_ll.setOnClickListener(this);
                switch_ll.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.changePassword_ll:
            case R.id.shopSet_ll:
            case R.id.printSet_ll:
            case R.id.emptyQueue_ll:
            case R.id.logout_ll:
            case R.id.switch_ll:
                if (mLeftMenuView != null) {
                    mLeftMenuView.onClick(view);
                    drawerLayout.closeDrawers();
                }
                break;
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter implements CustomPagerSlidingTabStrip.CustomTabProvider {

        protected LayoutInflater mInflater;
        private List<Fragment> mFragmentList;

        public PagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            mInflater = LayoutInflater.from(mContext);
            this.mFragmentList = list;
//            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            if (mFragmentList != null) {
                return mFragmentList.get(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            if (mFragmentList != null) {
                return mFragmentList.size();
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < queueNum) {
                return listNotZero.get(position * 2) + "-" + listNotZero.get(position * 2 + 1) + "位";
            } else {
                return "历史";
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public View getSelectTabView(int position, View convertView) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_select_tab, null);
            }

            TextView tv = ViewHolder.get(convertView, R.id.tvTab);
            TextView tip_tv = ViewHolder.get(convertView, R.id.tip_tv);

            tv.setText(getPageTitle(position));
            int size = 0;
            if (groupList.size() > position) {
                size = groupList.get(position).size();
            } else {
                tip_tv.setVisibility(View.GONE);
            }
            tip_tv.setText(size + "");

            AutoUtils.autoSize(convertView);
            return convertView;
        }

        @Override
        public View getDisSelectTabView(int position, View convertView) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_disselect_tab, null);
            }

            TextView tv = ViewHolder.get(convertView, R.id.tvTab);
            TextView tip_tv = ViewHolder.get(convertView, R.id.tip_tv);
            tv.setText(getPageTitle(position));
            int size = 0;
            if (groupList.size() > position) {
                size = groupList.get(position).size();
            } else {
                tip_tv.setVisibility(View.GONE);
            }
            tip_tv.setText(size + "");
            AutoUtils.autoSize(convertView);
            return convertView;
        }
    }

    //按钮点击事件
    public void clickButton(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.s_button1:
                appendNumber("1");
                break;
            case R.id.s_button2:
                appendNumber("2");
                break;
            case R.id.s_button3:
                appendNumber("3");
                break;
            case R.id.s_button4:
                appendNumber("4");
                break;
            case R.id.s_button5:
                appendNumber("5");
                break;
            case R.id.s_button6:
                appendNumber("6");
                break;
            case R.id.s_button7:
                appendNumber("7");
                break;
            case R.id.s_button8:
                appendNumber("8");
                break;
            case R.id.s_button9:
                appendNumber("9");
                break;
            case R.id.s_button0:
                appendNumber("0");
                break;
            case R.id.button_delete:
                editString = "";
                setText();
                break;
            case R.id.button_back:
                if (!editString.isEmpty()) {
                    editString = editString.substring(0, editString.length() - 1);
                    setText();
                }
                break;
            case R.id.peoplecount_ll:
                if (peoplecount_ll != null) {
                    peoplecount_ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_selector));
                    phone_ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_noselector));
                    query_ll.setBackground(getResources().getDrawable(R.drawable.text_editnumber_noselector_search));
                    edit = 0;
                    editString = peoplecount_tv.getText().toString();
                    fetch_tv.setText("取  号");
                    query_tv.setText("");
                } else {
                    peoplecount_ll_dialog.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_selector));
                    phone_ll_dialog.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_noselector));
                    query_ll_dialog.setBackground(getResources().getDrawable(R.drawable.text_editnumber_noselector_search));
                    edit = 0;
                    editString = peoplecount_tv_dialog.getText().toString();
                    fetch_tv_dialog.setText("取  号");
                    query_tv_dialog.setText("");
                }
                break;
            case R.id.phone_ll:
                if (peoplecount_ll != null) {
                    peoplecount_ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_noselector));
                    phone_ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_selector));
                    query_ll.setBackground(getResources().getDrawable(R.drawable.text_editnumber_noselector_search));
                    edit = 1;
                    editString = phone_tv.getText().toString();
                    fetch_tv.setText("取  号");
                    query_tv.setText("");
                } else {
                    peoplecount_ll_dialog.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_noselector));
                    phone_ll_dialog.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_selector));
                    query_ll_dialog.setBackground(getResources().getDrawable(R.drawable.text_editnumber_noselector_search));
                    edit = 1;
                    editString = phone_tv_dialog.getText().toString();
                    fetch_tv_dialog.setText("取  号");
                    query_tv_dialog.setText("");
                }
                break;
            case R.id.query_ll:
                if (peoplecount_ll != null) {
                    peoplecount_ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_noselector));
                    phone_ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_noselector));
                    query_ll.setBackground(getResources().getDrawable(R.drawable.text_editnumber_selector_search));
                    edit = 2;
                    fetch_tv.setText("查  询");
                    peoplecount_tv.setText("");
                    phone_tv.setText("");
                    query_tv.setText("");
                    editString = query_tv.getText().toString();
                } else {
                    peoplecount_ll_dialog.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_noselector));
                    phone_ll_dialog.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_noselector));
                    query_ll_dialog.setBackground(getResources().getDrawable(R.drawable.text_editnumber_selector_search));
                    edit = 2;
                    fetch_tv_dialog.setText("查  询");
                    peoplecount_tv_dialog.setText("");
                    phone_tv_dialog.setText("");
                    query_tv_dialog.setText("");
                    editString = query_tv_dialog.getText().toString();
                }
                setText();
                break;
            case R.id.fetch_tv:
                if (drawerLayout != null) {//竖屏才可以关闭
                    drawerLayout.closeDrawers();
                }

                if (edit != 2) {//取号
                    fetchTicket();
                } else {//查询号码
                    //因为在查询号码弹窗时PaiduiListAdapter的item是竖屏的item，所以要标记一下
                    isQueryTicket = true;//查询号码

                    List<QueueInfo> infoList;
                    if (query_tv != null) {
                        infoList = findTick(query_tv.getText().toString());
                    } else {
                        infoList = findTick(query_tv_dialog.getText().toString());
                    }
                    if (infoList.size() == 0) {
                        Toast.makeText(this, "找不到该号码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    MyDialog myDialog = new MyDialog(this, R.style.dialog);
                    View myDialogView = LayoutInflater.from(this).inflate(R.layout.printdialog, null);
                    ListView listView = (ListView) myDialogView.findViewById(R.id.listView_content_dialog);
                    TextView noData_tv = (TextView) myDialogView.findViewById(R.id.noData_tv);
                    if (infoList.size() == 0) {
                        noData_tv.setVisibility(View.VISIBLE);
                    }
                    PaiduiListAdapter adapter = new PaiduiListAdapter(this, infoList);
//                adapter.setTickets(infoList);
                    listView.setAdapter(adapter);
                    myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    myDialog.setContentView(myDialogView);
                    myDialog.show();

                    myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            isQueryTicket = false;//查询号码弹窗已经消失
                        }
                    });
                }
                break;
            case R.id.logout_tv:

                break;
            case R.id.setting_ll:
                if (drawerLayout != null) {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
                break;
            case R.id.delete_table:
                if (table.equals(SmartPosPrivateKey.TABEL_QUEUE)) {
                    BackupHelper.getInstance(QueueActivity.this).deleteAll();
                    SharePreferenceUtil.saveStringPreference(QueueActivity.this, SmartPosPrivateKey.TABEL_BEUSED, SmartPosPrivateKey.TABEL_BACKUP);
                    table = SmartPosPrivateKey.TABEL_BACKUP;//改变使用的表
                } else {
                    OrmHelper.getInstance(QueueActivity.this).deleteAll();
                    SharePreferenceUtil.saveStringPreference(QueueActivity.this, SmartPosPrivateKey.TABEL_BEUSED, SmartPosPrivateKey.TABEL_QUEUE);
                    table = SmartPosPrivateKey.TABEL_QUEUE;
                }
                SharePreferenceUtil.saveIntPreference(QueueActivity.this, SmartPosPrivateKey.A_NUM, 0);
                SharePreferenceUtil.saveIntPreference(QueueActivity.this, SmartPosPrivateKey.B_NUM, 0);
                SharePreferenceUtil.saveIntPreference(QueueActivity.this, SmartPosPrivateKey.C_NUM, 0);
                SharePreferenceUtil.saveIntPreference(QueueActivity.this, SmartPosPrivateKey.D_NUM, 0);
                SharePreferenceUtil.saveIntPreference(QueueActivity.this, SmartPosPrivateKey.E_NUM, 0);
                queueInfoList.clear();//删除数据
                groupList.clear();
                initData();
                break;
            case R.id.reQueue:
//                queueSetting();
                startActivityForResult(new Intent(QueueActivity.this, GroupingSettingActivity.class), REQUESTCODE);
                break;
            case R.id.dismissView:
                ticketDialog.dismiss();//dismiss竖屏取票弹窗
                break;
            case R.id.lock_ll:
                //锁屏
                if (mLeftMenuView != null) {
                    mLeftMenuView.onClick(view);
                }
                break;
            default:
                break;
        }
    }

//    private void initPollListener() {
//        //轮询后台,得知餐厅数据更新后,刷新界面
//        SanyiSDK.getSDK().installPollingListener(getClass().getName(), new IPollingListener() {
//            @Override
//            public void OperationDataUpdate() {
//                Log.e("````", "OperationDataUpdate()");
//            }
//
//            @Override
//            public void RestaurantDataUpdate() {
//                Subscription subscription = PaiduiHttp.getInstance().getPaiduiService().downLoadRest()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(Schedulers.io())
//                        .map(rest -> rest)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Observer<Rest>() {
//                            @Override
//                            public void onCompleted() {
//
//                                Log.d(TAG, "onCompleted");
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Toast.makeText(mContext, "餐厅数据更新失败" + ErrorHandler.handle(e), Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onNext(Rest r) {
//                                SanyiSDK.rest = r;
//                                Toast.makeText(mContext, "餐厅数据已更新", Toast.LENGTH_SHORT).show();
//                                if (mFragments != null)
//                                    mFragments.clear();
//                                else
//                                    mFragments = new ArrayList<>();
//                                for (int i = 0; i < SanyiSDK.rest.queues.size(); i++) {
//                                    FragmentTable fragmentTable = new FragmentTable();
//                                    fragmentTable.setQueueId(i);
//                                    mFragments.add(fragmentTable);
//                                }
//                                historyFragment = new FragmentHistory();
//                                mFragments.add(historyFragment);
//                                myPagerAdapter.notifyDataSetChanged();
//                                tabStrip.setViewPager(mViewPager);
//                                queryTicket();
//                            }
//                        });
//            }
//
//            @Override
//            public void RestaurantDataUpdateComplete() {
//                Log.e("````", "RestaurantDataUpdateComplete()");
//            }
//
//            @Override
//            public void SoldOutUpdate(OperationData.Notification notification) {
//                Log.e("````", "SoldOutUpdate()");
//            }
//
//            @Override
//            public void ClearSoldOutUpdate(OperationData.Notification notification) {
//                Log.e("````", "ClearSoldOutUpdate()");
//            }
//
//            @Override
//            public void ForceUnlockTable(OperationData.Notification notification) {
//                Log.e("````", "ForceUnlockTable()");
//            }
//
//            @Override
//            public void VersionUpdate(OperationData.Notification notification) {
//                Log.e("````", "VersionUpdate()");
//                //退出到登录页面进行下载安装
////                new AlertDialog.Builder(MainActivity.this)
////                        .setTitle("更新")
////                        .setMessage("检测到有新版本,请点击确定开始下载")
////                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////                                Restaurant.STAFF_ID = 0;
////                                Intent loginintent = new Intent(MainActivity.this, ChooseActivity.class);
////                                loginintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                                startActivity(loginintent);
////                                MainActivity.this.finish();
////                            }
////                        }).create().show();
//
//            }
//
//            @Override
//            public void ShopClosed(OperationData.Notification notification) {
//                Log.e("````", "ShopClosed()");
//            }
//
//            @Override
//            public void ShopOpen(OperationData.Notification notification) {
//                Log.e("````", "ShopOpen()");
//            }
//
//            @Override
//            public void ShopMaintain(OperationData.Notification notification) {
//                Log.e("````", "ShopMaintain()");
//            }
//
//            @Override
//            public void PasswordChanged(OperationData.Notification notification) {
//                Log.e("````", "PasswordChanged()");
//            }
//
//            @Override
//            public void WeChatPaySuccessed(OperationData.Notification notification) {
//                Log.e("````", "WeChatPaySuccessed()");
//            }
//
//            @Override
//            public void UpdateSoldoutDish() {
//                Log.e("````", "UpdateSoldoutDish()");
//            }
//
//            @Override
//            public void net_normal() {
//                Log.e("````", "net_normal()");
//            }
//
//            @Override
//            public void QueueDataUpdate() {
//                Log.e("````", "QueueDataUpdate()");
//                queryTicket();
//            }
//
//            @Override
//            public void kdsOrderUpdate(KdsOrders kdsOrders) {
//            }
//
//            @Override
//            public void onFail(String s) {
//                Log.e("````", "onFail()");
//            }
//        });
//    }

//    public void queryTicket() {//网络请求所有队伍数据Ticket
//        Subscription subscribe = PaiduiHttp.getInstance().getPaiduiService().queryTick()
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .map(new Func1<Ticket, Ticket>() {
//                    @Override
//                    public Ticket call(Ticket ticket) {
//                        return ticket;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Ticket>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
////                        ToastUtils.show(SanyiMainActivity.this, "取号失败" + ErrorHandler.handle(e), Toast.LENGTH_SHORT);
//                    }
//
//                    @Override
//                    public void onNext(Ticket ticket) {
////                        ToastUtils.show(SanyiMainActivity.this, "取号成功", ToastUtils.LENGTH_SHORT);
//                        parseTicket(ticket);
//                    }
//                });
//    }

//    public void parseTicket(Ticket ticket) {
//        this.ticket = ticket;//将队伍数据保存在全局，查询时需要
//        List<FetchTicketResult> passLists = new ArrayList<>();//历史队伍数组
//        for (int i = 0; i < SanyiSDK.rest.queues.size(); i++) { //SanyiSDK.rest.queues.size()这个的值不包含历史队伍
//            List<FetchTicketResult> mList = new ArrayList<>();
//            for (FetchTicketResult tick : ticket.details) {
//                if (tick.queue == SanyiSDK.rest.queues.get(i).id) {//筛选出一个队伍的数据
//                    if (tick.state == UpdateStateParam.BEGIN) {
//                        mList.add(tick);
//                    } else {
//                        passLists.add(tick);  //历史队伍的值是由其他队伍数据生成的
//                    }
//                }
//            }
//            tabStrip.showDot(i, Integer.toString(mList.size()));//设置并显示队伍小红点显示的数据
////            ((FragmentTable) mFragments.get(i)).init();
//            ((FragmentTable) mFragments.get(i)).setTickets(mList);//更新队伍数据
//        }
//        ((FragmentHistory) mFragments.get(mFragments.size() - 1)).setTickets(passLists);//更新历史队伍数据
//
//        if (mViewPager.getCurrentItem() < mFragments.size() - 1) {//更新界面
//            ((FragmentTable) mFragments.get(mViewPager.getCurrentItem())).refresh();
//        } else {
//            ((FragmentHistory) mFragments.get(mViewPager.getCurrentItem())).refresh();
//        }
////        int emptyTable = 0;
////        for (SeatEntity seat : SanyiSDK.rest.operationData.shopTables) {//空台数
////            if (seat.state == 1) {
////                emptyTable++;
////            }
////        }
////        textViewFreeTable.setText(Integer.toString(emptyTable));
//    }

    /**
     * initTip初始化groupList数组，其实就是为了得到tab上tip的数字
     */
//    public void initTip() {
//        groupList.clear();
//        for (int i = 0; i < queueNum; i++) {
//            List<QueueInfo> mList = new ArrayList<>();
//            for (QueueInfo info : queueInfoList) {
//                if (info.getQueueName().equals(queueNameArray[i])) {//筛选出一个队伍的数据
//                    if (info.getState() == UpdateState.WAITING) {
//                        mList.add(info);
//                    }
//                }
//            }
//            groupList.add(mList);
//        }
//    }

    private List<QueueInfo> initTip() {
        groupList.clear();
        List<QueueInfo> passLists = new ArrayList<>();//历史队伍数组
        List<QueueInfo> mListA = new ArrayList<>();
        List<QueueInfo> mListB = new ArrayList<>();
        List<QueueInfo> mListC = new ArrayList<>();
        List<QueueInfo> mListD = new ArrayList<>();
        List<QueueInfo> mListE = new ArrayList<>();
        for (QueueInfo info : queueInfoList) {
//                if (info.getQueueName().equals(queueNameArray[i])) {//筛选出一个队伍的数据listNotZero.get(position * 2) + "-" + listNotZero.get(position * 2 + 1)
            if (list.get(0) <= info.getPeople() && info.getPeople() <= list.get(1)) {//筛选出一个队伍的数据
                if (info.getState() == UpdateState.WAITING) {
                    mListA.add(info);
                } else if (info.getState() == UpdateState.PASS) {
                    passLists.add(info);
                }
            } else if (info.getPeople() <= list.get(3)) {
                if (info.getState() == UpdateState.WAITING) {
                    mListB.add(info);
                } else if (info.getState() == UpdateState.PASS) {
                    passLists.add(info);
                }
            } else if (info.getPeople() <= list.get(5)) {
                if (info.getState() == UpdateState.WAITING) {
                    mListC.add(info);
                } else if (info.getState() == UpdateState.PASS) {
                    passLists.add(info);
                }
            } else if (info.getPeople() <= list.get(7)) {
                if (info.getState() == UpdateState.WAITING) {
                    mListD.add(info);
                } else if (info.getState() == UpdateState.PASS) {
                    passLists.add(info);
                }
            } else if (info.getPeople() <= list.get(9)) {
                if (info.getState() == UpdateState.WAITING) {
                    mListE.add(info);
                } else if (info.getState() == UpdateState.PASS) {
                    passLists.add(info);
                }
            }
        }
        if (queueName.contains("A")) {
            groupList.add(mListA);
        }
        if (queueName.contains("B")) {
            groupList.add(mListB);
        }
        if (queueName.contains("C")) {
            groupList.add(mListC);
        }
        if (queueName.contains("D")) {
            groupList.add(mListD);
        }
        if (queueName.contains("E")) {
            groupList.add(mListE);
        }

        return passLists;
    }

    public void initData() {//初始化数据

        List<QueueInfo> passLists = initTip();

        for (int i = 0; i < groupList.size(); i++) {
            ((FragmentTable) mFragments.get(i)).setTickets(groupList.get(i));//更新队伍数据
        }

        ((FragmentHistory) mFragments.get(mFragments.size() - 1)).setTickets(passLists);//更新历史队伍数据

        if (mViewPager.getCurrentItem() < mFragments.size() - 1) {//更新界面
            ((FragmentTable) mFragments.get(mViewPager.getCurrentItem())).refresh();
        } else {
            ((FragmentHistory) mFragments.get(mViewPager.getCurrentItem())).refresh();
        }
    }

    public List<QueueInfo> findTick(String find) {//查询号码
        List<QueueInfo> mList = new ArrayList<>();
        for (QueueInfo info : queueInfoList) {
            if (String.valueOf(info.getNum()).equals(find) || info.getPhone().equals(find)) {
                mList.add(info);
            }
        }
        return mList;
    }

    public void addSound(String t) {//叫号
        try {
            broadcastTexts.put(t);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void switchFragment(int position) {
        //只有滑动界面，tab上的tip才会改变，所以这句主要就是让viewPager滚动
        //如果取票要滚动到的页面就是当前页面，且当前页面不是历史页面，则向后滚动
        if (mViewPager.getCurrentItem() == position && position != (mFragments.size() - 1)) {
            mViewPager.setCurrentItem(position + 1);//先向后滑动
        }
        mViewPager.setCurrentItem(position);//再滑动回来
        ((FragmentTable) mFragments.get(position)).refresh();
    }

    public void refreshTip() {//由于有来回滑动，所以会触发viewpager的滚动监听调用initData
        if (position != (mFragments.size() - 1)) {
            mViewPager.setCurrentItem(position + 1);//先向后滑动
        }
        mViewPager.setCurrentItem(position - 1);//再滑动回来
    }

    public void appendNumber(String inNumb) {
        if (edit == 0) {//0 表示输入就餐人数
            if (editString.length() < 2) { //就餐人数不能超过两位数
                editString = editString + inNumb;
                setText();
            }
        } else {//输入手机号或查询号
            editString = editString + inNumb;
            setText();
        }
    }

    public void setText() {
        if (edit == 0) {//选中输入就餐人数
            if (fetch_tv != null) {
                fetch_tv.setEnabled(false);
                if (editString.length() < 3) {
                    peoplecount_tv.setText(editString);
                    if (editString.length() != 0
                            && Integer.valueOf(editString) != 0
//                            && Integer.valueOf(editString) >= listNotZero.get(0)
                            && Integer.valueOf(editString) <= listNotZero.get(listNotZero.size() - 1)) {//取号人数不能大于最大数
                        fetch_tv.setEnabled(true);//输入的就餐人数符合时才能取号
                        if (!phone_tv.getText().toString().isEmpty()) {//如果电话号码一栏不为空
                            if (!isPhoneNumber(phone_tv.getText().toString())) {//但是电话号码格式不对
                                fetch_tv.setEnabled(false);//则不能点击取号
                            }
                        }
                    }
                }
            } else {//这个是竖屏时
                fetch_tv_dialog.setEnabled(false);
                if (editString.length() < 3) {
                    peoplecount_tv_dialog.setText(editString);
                    if (editString.length() != 0
                            && Integer.valueOf(editString) != 0
//                            && Integer.valueOf(editString) >= listNotZero.get(0)
                            && Integer.valueOf(editString) <= listNotZero.get(listNotZero.size() - 1)) {//取号人数不能大于最大数
                        fetch_tv_dialog.setEnabled(true);//输入的就餐人数符合时才能取号
                        if (!phone_tv_dialog.getText().toString().isEmpty()) {//如果电话号码一栏不为空
                            if (!isPhoneNumber(phone_tv_dialog.getText().toString())) {//但是电话号码格式不对
                                fetch_tv_dialog.setEnabled(false);//则不能点击取号
                            }
                        }
                    }
                }
            }
        } else if (edit == 1) {//选中手机号码
            if (phone_tv != null) {
                phone_tv.setText(editString);
                if ((isPhoneNumber(editString) || "".equals(editString)) && !peoplecount_tv.getText().toString().isEmpty()) {
                    fetch_tv.setEnabled(true);
                } else {
                    fetch_tv.setEnabled(false);
                }
            } else {
                phone_tv_dialog.setText(editString);
                if ((isPhoneNumber(editString) || "".equals(editString)) && !peoplecount_tv_dialog.getText().toString().isEmpty()) {
                    fetch_tv_dialog.setEnabled(true);
                } else {
                    fetch_tv_dialog.setEnabled(false);
                }
            }
        } else if (edit == 2) {//选中查询框
            if (query_tv != null) {
                query_tv.setText(editString);
                if (!query_tv.getText().toString().isEmpty()) {
                    fetch_tv.setEnabled(true);
                } else {
                    fetch_tv.setEnabled(false);
                }
            } else {
                query_tv_dialog.setText(editString);
                if (!query_tv_dialog.getText().toString().isEmpty()) {
                    fetch_tv_dialog.setEnabled(true);
                } else {
                    fetch_tv_dialog.setEnabled(false);
                }
            }
        }
    }

    //点击取号的逻辑操作
    private void fetchTicket() {
        QueueInfo info = new QueueInfo();//构造排队数据对象
        int people;
        String phone;
        if (peoplecount_tv != null) {
            people = Integer.valueOf(peoplecount_tv.getText().toString());
            phone = phone_tv.getText().toString();
        } else {
            people = Integer.valueOf(peoplecount_tv_dialog.getText().toString());
            phone = phone_tv_dialog.getText().toString();
        }
        info.setQueueId(0);
        info.setPeople(people);
        info.setPhone(phone);
        info.setCreate(new Date());
        info.setState(UpdateState.WAITING);
        info.setCount(0);
        int position = 0; //标志取号队伍
        long shopQueueId = 0;

        if (list.get(0) <= people && people <= list.get(1)) {//根据人数设置队列名及获取序号
            info.setQueueName("A");
            int num = SharePreferenceUtil.getPreferenceInteger(QueueActivity.this, SmartPosPrivateKey.A_NUM, 0);//读取队列A的最大序号
            SharePreferenceUtil.saveIntPreference(QueueActivity.this, SmartPosPrivateKey.A_NUM, num + 1);//序号+1再保存
            info.setNum(num + 1);
            shopQueueId = SharePreferenceUtil.getPreferenceLong(QueueActivity.this, SmartPosPrivateKey.A_ID, 0);
            info.setShopQueueId(SharePreferenceUtil.getPreferenceLong(QueueActivity.this, SmartPosPrivateKey.A_ID, 0));
        } else if (people <= list.get(3)) {
            info.setQueueName("B");
            int num = SharePreferenceUtil.getPreferenceInteger(QueueActivity.this, SmartPosPrivateKey.B_NUM, 0);
            SharePreferenceUtil.saveIntPreference(QueueActivity.this, SmartPosPrivateKey.B_NUM, num + 1);
            info.setNum(num + 1);
            shopQueueId = SharePreferenceUtil.getPreferenceLong(QueueActivity.this, SmartPosPrivateKey.B_ID, 0);
            info.setShopQueueId(SharePreferenceUtil.getPreferenceLong(QueueActivity.this, SmartPosPrivateKey.B_ID, 0));
        } else if (people <= list.get(5)) {
            info.setQueueName("C");
            int num = SharePreferenceUtil.getPreferenceInteger(QueueActivity.this, SmartPosPrivateKey.C_NUM, 0);
            SharePreferenceUtil.saveIntPreference(QueueActivity.this, SmartPosPrivateKey.C_NUM, num + 1);
            info.setNum(num + 1);
            shopQueueId = SharePreferenceUtil.getPreferenceLong(QueueActivity.this, SmartPosPrivateKey.C_ID, 0);
            info.setShopQueueId(SharePreferenceUtil.getPreferenceLong(QueueActivity.this, SmartPosPrivateKey.C_ID, 0));
        } else if (people <= list.get(7)) {
            info.setQueueName("D");
            int num = SharePreferenceUtil.getPreferenceInteger(QueueActivity.this, SmartPosPrivateKey.D_NUM, 0);
            SharePreferenceUtil.saveIntPreference(QueueActivity.this, SmartPosPrivateKey.D_NUM, num + 1);
            info.setNum(num + 1);
            shopQueueId = SharePreferenceUtil.getPreferenceLong(QueueActivity.this, SmartPosPrivateKey.D_ID, 0);
            info.setShopQueueId(SharePreferenceUtil.getPreferenceLong(QueueActivity.this, SmartPosPrivateKey.D_ID, 0));
        } else if (people <= list.get(9)) {
            info.setQueueName("E");
            int num = SharePreferenceUtil.getPreferenceInteger(QueueActivity.this, SmartPosPrivateKey.E_NUM, 0);
            SharePreferenceUtil.saveIntPreference(QueueActivity.this, SmartPosPrivateKey.E_NUM, num + 1);
            info.setNum(num + 1);
            shopQueueId = SharePreferenceUtil.getPreferenceLong(QueueActivity.this, SmartPosPrivateKey.E_ID, 0);
            info.setShopQueueId(SharePreferenceUtil.getPreferenceLong(QueueActivity.this, SmartPosPrivateKey.E_ID, 0));
        }

        if (people <= listNotZero.get(1)) {
            position = 0;
        } else if (people <= listNotZero.get(3)) {
            position = 1;
        } else if (people <= listNotZero.get(5)) {
            position = 2;
        } else if (people <= listNotZero.get(7)) {
            position = 3;
        } else if (people <= listNotZero.get(9)) {
            position = 4;
        }

        if (table.equals(SmartPosPrivateKey.TABEL_QUEUE)) {
            OrmHelper.getInstance(QueueActivity.this).insert(info);//插入一条数据
        } else {
            BackupHelper.getInstance(QueueActivity.this).insert(info);//插入一条数据
        }
        //因为id是自增的，所以只能从数据库中取了
        if (table.equals(SmartPosPrivateKey.TABEL_QUEUE)) {
            info = OrmHelper.getInstance(QueueActivity.this).queryByQueueNameAndNum(info.getQueueName(), info.getNum());
        } else {
            info = BackupHelper.getInstance(QueueActivity.this).queryByQueueNameAndNum(info.getQueueName(), info.getNum());
        }
        queueInfoList.add(info);
//        groupList.get(position).add(info);
        ((FragmentTable) mFragments.get(position)).addTicket(info);
        switchFragment(position);

        changeSHopInfo();//检测之前修改门店信息是否上传后台

        FetchTicketParams params = new FetchTicketParams();
        params.setShopQueueId(shopQueueId);
        params.setShopId(SharePreferenceUtil.getPreferenceLong(QueueActivity.this, SmartPosPrivateKey.ID, 0));
//        params.setUserId(SharePreferenceUtil.getPreferenceLong(QueueActivity.this, SmartPosPrivateKey.USER, 0));
        params.setShopName(SharePreferenceUtil.getPreference(QueueActivity.this, SmartPosPrivateKey.SHOP_NAME, ""));
        params.setQueueNum(groupList.get(position).size());
        params.setQueueName(info.getQueueName() + info.getNum());
        params.setState(0);
        QueueInfo finalInfo = info;
        int finalPosition = position;
        PaiduiHttp.getInstance().getPaiduiService().fetchTicket(params)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(result -> result)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QrCode>() {
                    @Override
                    public void onCompleted() {
                        Log.e("TAG", "fetchTicketCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "fetchTicketError: " + e);
                        //如果请求不成功，则不打印二维码
                        if (AidlUtil.getInstance().isUsable()) {
                            AidlUtil.getInstance().printShopName(shopName)
                                    .printTicketNum(finalInfo.getQueueName() + finalInfo.getNum())
                                    .printSeparator()
                                    .printTableType(finalInfo.getQueueName(), list.get(finalPosition * 2), list.get(finalPosition * 2 + 1))
                                    .printWaitNum(groupList.get(finalPosition).size() - 1)//等待人数不包括自己
                                    .printPeopleNum(finalInfo.getPeople())
                                    .printTextLeft("排队时间：" + getStringDate(finalInfo.getCreate()))
                                    .printTextLeft("温馨提示：过号需要重新取号，敬请谅解")
                                    .printTextLeft("备注：" + SharePreferenceUtil.getPreference(QueueActivity.this, SmartPosPrivateKey.REMARK, ""))
                                    .printSeparator()
                                    .end();
                        }
                    }

                    @Override
                    public void onNext(QrCode result) {
                        Log.e("TAG", "fetchTicketNext: ");

//                        String qrCode = result.getUrl();
                        FetchQueue queue = result.getQueue();
                        WeiXinMsg weiXinMsg = result.getWeiXinMsg();
                        long id = queue.getId();
                        String url = weiXinMsg.getUrl();

                        finalInfo.setQueueId(id);

                        if (table.equals(SmartPosPrivateKey.TABEL_QUEUE)) {
                            OrmHelper.getInstance(QueueActivity.this).update(finalInfo);//插入一条数据
                        } else {
                            BackupHelper.getInstance(QueueActivity.this).update(finalInfo);//插入一条数据
                        }

                        if (AidlUtil.getInstance().isUsable()) {
                            AidlUtil.getInstance().printShopName(shopName)
                                    .printTicketNum(finalInfo.getQueueName() + finalInfo.getNum())
                                    .printSeparator()
                                    .printTableType(finalInfo.getQueueName(), list.get(finalPosition * 2), list.get(finalPosition * 2 + 1))
                                    .printWaitNum(groupList.get(finalPosition).size() - 1)//等待人数不包括自己
                                    .printPeopleNum(finalInfo.getPeople())
                                    .printTextLeft("排队时间：" + getStringDate(finalInfo.getCreate()))
                                    .printTextLeft("温馨提示：过号需要重新取号，敬请谅解")
                                    .printTextLeft("备注：" + SharePreferenceUtil.getPreference(QueueActivity.this, SmartPosPrivateKey.REMARK, ""))
                                    .printSeparator()
                                    .printQr(url)
                                    .end();
                        }
                    }
                });
        editString = "";
        edit = 0;
        if (peoplecount_tv != null) {
            peoplecount_tv.setText("");
            phone_tv.setText("");
            peoplecount_ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_selector));
            phone_ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_noselector));
            fetch_tv.setEnabled(false);
        } else {
            peoplecount_tv_dialog.setText("");
            phone_tv_dialog.setText("");
            peoplecount_ll_dialog.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_selector));
            phone_ll_dialog.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_noselector));
            fetch_tv_dialog.setEnabled(false);
            ticketDialog.dismiss();
        }
    }

    private void changeSHopInfo() {//检测之前修改门店信息是否上传后台
        boolean isChange = SharePreferenceUtil.getBooleanPreference(QueueActivity.this, SmartPosPrivateKey.ISCHANGESHOP, true);
        if (isChange) {

            ShopUpdateParams params = new ShopUpdateParams();//ShopUpdateParams
            List<Queues> queues = new ArrayList<>();
            String[] queueNames = new String[]{"A", "B", "C", "D", "E"};
            for (int i = 0; i < queueNames.length; i++) {
                Queues queue = new Queues();
                queue.setShopQueueID(GlobeMethod.getQueueId().get(i));
                queue.setName(queueNames[i]);
                queue.setMinNum(list.get(2 * i));
                queue.setMaxNum(list.get(2 * i + 1));
                queues.add(queue);
            }
            params.setShopId(SharePreferenceUtil.getPreferenceLong(QueueActivity.this, SmartPosPrivateKey.ID, 0));
            params.setUserId(SharePreferenceUtil.getPreferenceLong(QueueActivity.this, SmartPosPrivateKey.USER, 0));
            params.setShopName(shopName);
            params.setAddress(address);
            params.setMobile(SharePreferenceUtil.getPreference(QueueActivity.this, SmartPosPrivateKey.PHONE, ""));
            params.setRemark(SharePreferenceUtil.getPreference(QueueActivity.this, SmartPosPrivateKey.REMARK, ""));
            params.setQueues(queues);

            PaiduiHttp.getInstance().getPaiduiService().shopUpdate(params)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(result -> result)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onCompleted() {
                            Log.e("TAG", "shopUpdateCompleted: ");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", "shopUpdateError: " + e);
                        }

                        @Override
                        public void onNext(Boolean result) {
                            Log.e("TAG", "shopUpdateNext: ");
                            SharePreferenceUtil.saveBooleanPreference(QueueActivity.this, SmartPosPrivateKey.ISCHANGESHOP, false);
                        }
                    });
        }
    }

    public MediaPlayer createMediaPlayForSound(int resourceId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(QueueActivity.this, resourceId);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        return mediaPlayer;
    }

    public static boolean isPhoneNumber(String mobiles) {
//        Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$");
//        Matcher matcher = pattern.matcher(mobiles);
//        return matcher.matches();
        if (!TextUtils.isEmpty(mobiles) && mobiles.length() == 11) {//只需要判断11位号码就可以
            return true;
        } else {
            return false;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUESTCODE:
                    if (mFragments != null)
                        mFragments.clear();
                    else
                        mFragments = new ArrayList<>();
                    queueNum = SharePreferenceUtil.getPreferenceInteger(QueueActivity.this, SmartPosPrivateKey.QUEUE_NUM, 0);
                    String queueName = SharePreferenceUtil.getPreference(QueueActivity.this, SmartPosPrivateKey.QUEUE_NAME, "");
                    queueNameArray = queueName.split(",");
                    list = GlobeMethod.getQueue();
                    listNotZero = GlobeMethod.getQueueNotZero();
                    if (table.equals(SmartPosPrivateKey.TABEL_QUEUE)) {
                        queueInfoList = OrmHelper.getInstance(QueueActivity.this).queryAll();//查询排队表全部数据
                    } else {
                        queueInfoList = BackupHelper.getInstance(QueueActivity.this).queryAll();//查询备份表全部数据
                    }
                    for (int i = 0; i < queueNum; i++) {
                        FragmentTable fragmentTable = new FragmentTable();
                        fragmentTable.setQueueId(i);
                        mFragments.add(fragmentTable);
                    }
                    historyFragment = new FragmentHistory();
                    mFragments.add(historyFragment);
                    myPagerAdapter.notifyDataSetChanged();
                    tabStrip.setViewPager(mViewPager);
                    initData();
                    break;
            }
        }
    }

    //横竖屏切换
    public void screenChange() {
        setContentLayout(R.layout.activity_queue_land, R.layout.activity_queue_port);
//        setContentLayout(screen);
        initNav();
        initDrawerLayout();
        initRight_ll();
        initFetch();
        initTab();
        initData();
    }

    /**
     * 由Date转String
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

}
