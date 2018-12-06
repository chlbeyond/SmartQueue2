package com.smartqueue2.mainpage.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.smartqueue2.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;


public class AidlUtil {
    private static final String SERVICE＿PACKAGE = "woyou.aidlservice.jiuiv5";
    private static final String SERVICE＿ACTION = "woyou.aidlservice.jiuiv5.IWoyouService";

    private IWoyouService woyouService;
    private static AidlUtil mAidlUtil = new AidlUtil();
    private Context context;

    private AidlUtil() {
    }

    public static AidlUtil getInstance() {
        return mAidlUtil;
    }

    /**
     * 连接服务
     *
     * @param context context
     */
    public void connectPrinterService(Context context) {
        this.context = context.getApplicationContext();
        Intent intent = new Intent();
        intent.setPackage(SERVICE＿PACKAGE);
        intent.setAction(SERVICE＿ACTION);
        context.getApplicationContext().startService(intent);
        context.getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    /**
     * 断开服务
     *
     * @param context context
     */
    public void disconnectPrinterService(Context context) {
        if (woyouService != null) {
            context.getApplicationContext().unbindService(connService);
            woyouService = null;
        }
    }

    public boolean isConnect() {
        return woyouService != null;
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

    public ICallback generateCB(final PrinterCallback printerCallback) {
        return new ICallback.Stub() {
            @Override
            public void onRunResult(boolean isSuccess, int code, String msg) throws RemoteException {

            }

        };
    }

    /**
     * 设置打印浓度
     */
    private int[] darkness = new int[]{0x0600, 0x0500, 0x0400, 0x0300, 0x0200, 0x0100, 0,
            0xffff, 0xfeff, 0xfdff, 0xfcff, 0xfbff, 0xfaff};

    public void setDarkness(int index) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        int k = darkness[index];
        try {
            woyouService.sendRAWData(ESCUtil.setPrinterDarkness(k), null);
            woyouService.printerSelfChecking(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得打印机系统信息，放在list中
     *
     * @return list
     */
    public List<String> getPrinterInfo() {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return null;
        }

        List<String> info = new ArrayList<>();
        try {
            info.add(woyouService.getPrinterSerialNo());
            info.add(woyouService.getPrinterModal());
            info.add(woyouService.getPrinterVersion());
            info.add(woyouService.getPrintedLength() + "");
            info.add("");
            //info.add(woyouService.getServiceVersion());
            PackageManager packageManager = context.getPackageManager();
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(SERVICE＿PACKAGE, 0);
                if (packageInfo != null) {
                    info.add(packageInfo.versionName);
                    info.add(packageInfo.versionCode + "");
                } else {
                    info.add("");
                    info.add("");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 初始化打印机
     */
    public void initPrinter() {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.printerInit(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印二维码
     */
    public void printQr(String data, int modulesize, int errorlevel) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }


        try {
            woyouService.setAlignment(1, null);
            woyouService.printQRCode(data, modulesize, errorlevel, null);
            woyouService.lineWrap(3, null);
            woyouService.cutPaper(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印条形码
     */
    public void printBarCode(String data, int symbology, int height, int width, int textposition) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }


        try {
            woyouService.printBarCode(data, symbology, height, width, textposition, null);
            woyouService.lineWrap(3, null);
            woyouService.cutPaper(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印小票
     * 店名、队列、队列名、最小人数、最大人数、等待人数、就餐人数、商店地址、商店电话
     */
    public void printTicket(String shopName, String queue, String queueName,int min,
                            int max, int waitNum, int people, String address, String phone) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            woyouService.sendRAWData(ESCUtil.boldOff(), null);//不加粗
            woyouService.sendRAWData(ESCUtil.underlineOff(), null);//不加下划线

            woyouService.setAlignment(1, null);//居中
            woyouService.printTextWithFont(shopName + "\n 排队单 \n", null, 57, null);//店名及字体大小
            woyouService.lineWrap(1, null);//间隔一行

            woyouService.setAlignment(1, null);
            woyouService.printTextWithFont(queue + "\n", null, 57, null);//队列及字体大小

            woyouService.setAlignment(0, null);//居左
            woyouService.setFontSize(24, null);
            woyouService.printOriginalText("================================================" + "\n", null);//分隔符

            woyouService.setAlignment(0, null);
            woyouService.printTextWithFont("餐桌类型：" + queueName + "(" + min + "-" + max + "人桌)" + "\n", null, 24, null);

            woyouService.setAlignment(0, null);
            woyouService.printTextWithFont("等待桌数：" + waitNum + " 桌" + "\n", null, 24, null);

            woyouService.setAlignment(0, null);
            woyouService.printTextWithFont("就餐人数：" + people +" 人" + "\n", null, 24, null);

            woyouService.setAlignment(0, null);
            woyouService.printTextWithFont("温馨提示：过号需要重新取号，敬请谅解。" + "\n", null, 24, null);

            woyouService.setAlignment(0, null);
            woyouService.printTextWithFont("地址：" + address + "\n", null, 24, null);

            woyouService.setAlignment(0, null);
            woyouService.printTextWithFont("电话：" + phone + "\n", null, 24, null);

            woyouService.setAlignment(1, null);
            woyouService.printTextWithFont("本系统由深圳市彩虹云宝网络有限公司开发 \n http://www.sanyipos.com"+ "\n", null, 24, null);
            woyouService.lineWrap(1, null);//间隔一行
            woyouService.cutPaper(null);//切纸
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印文字
     * 内容、字体、对齐方式、走纸多少行、是否切纸
     */
    public void printText(String content, float size, int alignment, int lineWrap, boolean isCut) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            if (false) {//不加粗
                woyouService.sendRAWData(ESCUtil.boldOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.boldOff(), null);
            }

            if (false) {//不加下划线
                woyouService.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.underlineOff(), null);
            }

            woyouService.setAlignment(alignment, null);//靠左0、居中1、靠右2
            woyouService.printTextWithFont(content + "\n", null, size, null);//内容及字体大小
            woyouService.lineWrap(lineWrap, null);//走纸lineWrap行
            if (isCut) {
                woyouService.cutPaper(null);//切纸
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*
    * 打印分隔符
     */

    /*
    *打印图片
     */
    public void printBitmap(Bitmap bitmap) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.setAlignment(1, null);
            woyouService.printBitmap(bitmap, null);
            woyouService.lineWrap(3, null);
            woyouService.cutPaper(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印图片和文字按照指定排列顺序
     */
    public void printBitmap(Bitmap bitmap, int orientation) {
        if (woyouService == null) {
            Toast.makeText(context, "服务已断开！", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (orientation == 0) {
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("横向排列\n", null);
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("横向排列\n", null);
            } else {
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("\n纵向排列\n", null);
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("\n纵向排列\n", null);
            }
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*
    * 空打三行！
     */
    public void print3Line() {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void sendRawData(byte[] data) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.sendRAWData(data, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //获取当前的打印模式
    public int getPrintMode() {
        if (woyouService == null) {
            Toast.makeText(context, "服务已断开！", Toast.LENGTH_LONG).show();
            return -1;
        }

        int res;
        try {
            res = woyouService.getPrinterMode();
        } catch (RemoteException e) {
            e.printStackTrace();
            res = -1;
        }
        return res;
    }

    //获取打印机状态
    public int getPrinterState(){
        if (woyouService == null) {
            Toast.makeText(context,R.string.toast_2,Toast.LENGTH_LONG).show();
            return 3;//状态异常
        }
        int state = 3;//状态异常
        try {
            state = woyouService.updatePrinterState();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return state;
    }

    public boolean isUsable() {//使用前可判断服务是否已断开
        if (woyouService == null) {
            Toast.makeText(context, "服务已断开！", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

     /*********************************以下为链式打印***********************************************/

    public AidlUtil printShopName(String shopName) {
        if (woyouService == null) {
            return this;
        }
        try {
//            woyouService.sendRAWData(ESCUtil.boldOff(), null);//不加粗
//            woyouService.sendRAWData(ESCUtil.underlineOff(), null);//不加下划线
            woyouService.setAlignment(1, null);//居中
            woyouService.printTextWithFont(shopName + "\n 排队单 \n", null, 57, null);//店名及字体大小
            woyouService.lineWrap(1, null);//间隔一行
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return this;
    }

    public AidlUtil printTicketNum(String ticketNum) {
        if (woyouService == null) {
            return this;
        }
        try {
//            woyouService.sendRAWData(ESCUtil.boldOff(), null);//不加粗
//            woyouService.sendRAWData(ESCUtil.underlineOff(), null);//不加下划线
            woyouService.setAlignment(1, null);
            woyouService.printTextWithFont(ticketNum + "\n", null, 57, null);//队列及字体大小
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return this;
    }

    public AidlUtil printSeparator() {
        if (woyouService == null) {
            return this;
        }
        try {
//            woyouService.sendRAWData(ESCUtil.boldOff(), null);//不加粗
//            woyouService.sendRAWData(ESCUtil.underlineOff(), null);//不加下划线

            woyouService.setAlignment(0, null);//居左
            woyouService.setFontSize(24, null);
            woyouService.printOriginalText("================================================" + "\n", null);//分隔符
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return this;
    }

    public AidlUtil printTableType(String queueName,int min, int max) {
        if (woyouService == null) {
            return this;
        }
        try {
            woyouService.setAlignment(0, null);
            woyouService.printTextWithFont("餐桌类型：" + queueName + "(" + min + "-" + max + "人桌)" + "\n", null, 24, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return this;
    }

    public AidlUtil printWaitNum(int waitNum) {
        if (woyouService == null) {
            return this;
        }
        try {
            woyouService.setAlignment(0, null);
            woyouService.printTextWithFont("等待桌数：" + waitNum + " 桌" + "\n", null, 24, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return this;
    }

    public AidlUtil printPeopleNum(int people) {
        if (woyouService == null) {
            return this;
        }
        try {
            woyouService.setAlignment(0, null);
            woyouService.printTextWithFont("就餐人数：" + people +" 人" + "\n", null, 24, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return this;
    }

    public AidlUtil printTextLeft(String text) {
        if (woyouService == null) {
            return this;
        }
        try {
            woyouService.setAlignment(0, null);
            woyouService.printTextWithFont(text + "\n", null, 24, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return this;
    }

    public AidlUtil printQr(String data) {
        if (woyouService == null) {
            return this;
        }
        try {
            woyouService.setAlignment(1, null);
            woyouService.printTextWithFont("请用微信扫描二维码，实时关注排队进度！" + "\n", null, 24, null);
            woyouService.lineWrap(1, null);
            woyouService.printQRCode(data, 8, 3, null);
            woyouService.lineWrap(2, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return this;
    }

    public AidlUtil printPhoto(Bitmap bitmap) {
        if (woyouService == null) {
            return this;
        }
        try {
            woyouService.setAlignment(1, null);
            woyouService.printTextWithFont("请用微信扫描二维码，实时关注排队进度！" + "\n", null, 24, null);
            woyouService.lineWrap(1, null);
            woyouService.printBitmap(bitmap, null);
            woyouService.lineWrap(2, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void end() {
        if (woyouService == null) {
            return;
        }
        try {
            woyouService.setAlignment(1, null);
            woyouService.printTextWithFont("本系统由深圳市彩虹云宝网络有限公司开发 \n http://www.sanyipos.com"+ "\n", null, 24, null);
            woyouService.lineWrap(3, null);
            woyouService.cutPaper(null);//切纸
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
