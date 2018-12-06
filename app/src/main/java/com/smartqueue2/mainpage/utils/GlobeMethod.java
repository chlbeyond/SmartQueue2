package com.smartqueue2.mainpage.utils;

import com.smartqueue2.database.ormsql.BackupHelper;
import com.smartqueue2.database.ormsql.OrmHelper;
import com.smartqueue2.login.Activity.ShopInfoActivity;
import com.smartqueue2.mainpage.Paidui;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/11.
 */

public class GlobeMethod {

    //清空一些数据
    public static void emptyData() {
        SharePreferenceUtil.saveLongPreference(Paidui.getContext(), SmartPosPrivateKey.ID, 0);

        SharePreferenceUtil.saveStringPreference(Paidui.getContext(), SmartPosPrivateKey.SHOP_NAME, "");
        SharePreferenceUtil.saveStringPreference(Paidui.getContext(), SmartPosPrivateKey.ADDRESS, "");
        SharePreferenceUtil.saveStringPreference(Paidui.getContext(), SmartPosPrivateKey.PHONE, "");
        SharePreferenceUtil.saveStringPreference(Paidui.getContext(), SmartPosPrivateKey.REMARK, "");
        SharePreferenceUtil.saveStringPreference(Paidui.getContext(), SmartPosPrivateKey.LOCKPASSWORD, "");//锁屏密码

//        SharePreferenceUtil.saveBooleanPreference(Paidui.getContext(), SmartPosPrivateKey.ISEMPTYQUEUE, true);//标记清空排队数据
        BackupHelper.getInstance(Paidui.getContext()).deleteAll();//删除数据库中的数据
        OrmHelper.getInstance(Paidui.getContext()).deleteAll();

        SharePreferenceUtil.saveIntPreference(Paidui.getContext(), SmartPosPrivateKey.A_NUM, 0);//删除每个队列最大的序号（）
        SharePreferenceUtil.saveIntPreference(Paidui.getContext(), SmartPosPrivateKey.B_NUM, 0);
        SharePreferenceUtil.saveIntPreference(Paidui.getContext(), SmartPosPrivateKey.C_NUM, 0);
        SharePreferenceUtil.saveIntPreference(Paidui.getContext(), SmartPosPrivateKey.D_NUM, 0);
        SharePreferenceUtil.saveIntPreference(Paidui.getContext(), SmartPosPrivateKey.E_NUM, 0);

        initQueue();
    }

    //获得队列id
    public static List<Long> getQueueId() {
        List<Long> list = new ArrayList<>();
        list.add(SharePreferenceUtil.getPreferenceLong(Paidui.getContext(), SmartPosPrivateKey.A_ID, 0));
        list.add(SharePreferenceUtil.getPreferenceLong(Paidui.getContext(), SmartPosPrivateKey.B_ID, 0));
        list.add(SharePreferenceUtil.getPreferenceLong(Paidui.getContext(), SmartPosPrivateKey.C_ID, 0));
        list.add(SharePreferenceUtil.getPreferenceLong(Paidui.getContext(), SmartPosPrivateKey.D_ID, 0));
        list.add(SharePreferenceUtil.getPreferenceLong(Paidui.getContext(), SmartPosPrivateKey.E_ID, 0));
        return list;
    }

    /**
     * 初始化队列数据
      */
    public static void initQueue() {
        SharePreferenceUtil.saveIntPreference(Paidui.getContext(), SmartPosPrivateKey.A_1, 1);
        SharePreferenceUtil.saveIntPreference(Paidui.getContext(), SmartPosPrivateKey.A_2, 4);
        SharePreferenceUtil.saveIntPreference(Paidui.getContext(), SmartPosPrivateKey.B_1, 5);
        SharePreferenceUtil.saveIntPreference(Paidui.getContext(), SmartPosPrivateKey.B_2, 8);
        SharePreferenceUtil.saveIntPreference(Paidui.getContext(), SmartPosPrivateKey.C_1, 9);
        SharePreferenceUtil.saveIntPreference(Paidui.getContext(), SmartPosPrivateKey.C_2, 12);
        SharePreferenceUtil.saveIntPreference(Paidui.getContext(), SmartPosPrivateKey.D_1, 0);
        SharePreferenceUtil.saveIntPreference(Paidui.getContext(), SmartPosPrivateKey.D_2, 0);
        SharePreferenceUtil.saveIntPreference(Paidui.getContext(), SmartPosPrivateKey.E_1, 0);
        SharePreferenceUtil.saveIntPreference(Paidui.getContext(), SmartPosPrivateKey.E_2, 0);
    }

    /**
     * 获取文件中保存的队列全部数据
     */
    public static List<Integer> getQueue() {
        List<Integer> list = new ArrayList<>();
        list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.A_1, 0));//队列A的最小人数
        list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.A_2, 0));//队列A的最大人数
        list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.B_1, 0));
        list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.B_2, 0));
        list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.C_1, 0));
        list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.C_2, 0));
        list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.D_1, 0));
        list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.D_2, 0));
        list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.E_1, 0));
        list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.E_2, 0));
        return list;
    }

    /**
     * 获取文件中保存的队列非0的数据
     */
    public static List<Integer> getQueueNotZero() {
        List<Integer> list = new ArrayList<>();
        if (SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.A_2, 0) != 0) {
            list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.A_1, 0));//队列A的最小人数
            list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.A_2, 0));//队列A的最大人数
        }
        if (SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.B_2, 0) != 0) {
            list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.B_1, 0));
            list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.B_2, 0));
        }
        if (SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.C_2, 0) != 0) {
            list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.C_1, 0));
            list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.C_2, 0));
        }
        if (SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.D_2, 0) != 0) {
            list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.D_1, 0));
            list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.D_2, 0));
        }
        if (SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.E_2, 0) != 0) {
            list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.E_1, 0));
            list.add(SharePreferenceUtil.getPreferenceInteger(Paidui.getContext(), SmartPosPrivateKey.E_2, 0));
        }
        return list;
    }
}
