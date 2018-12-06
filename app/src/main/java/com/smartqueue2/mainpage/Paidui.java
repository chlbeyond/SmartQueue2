package com.smartqueue2.mainpage;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartqueue2.mainpage.utils.AidlUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Paidui extends Application {
	private static Paidui ourInstance = new Paidui();
	public boolean log = false;
	public Gson gson;

	public static final long ONE_KB = 1024L;
	public static final long ONE_MB = ONE_KB * 1024L;
	public static final long CACHE_DATA_MAX_SIZE = ONE_MB * 3L;

	public static Paidui getInstance() {
		return ourInstance;
	}

	public static String currentDate() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(now);
	}

	public static String currentTime() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(now);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ourInstance = this;
		this.initGson();
		AidlUtil.getInstance().connectPrinterService(this);//连接打印机服务器
	}

	public static Context getContext() {
		return ourInstance.getApplicationContext();
	}

	public static void toast(String text) {
		Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
	}

	public static void toastLong(String text) {
		Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
	}

	private void initGson() {
		this.gson = new GsonBuilder()
				.setDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss"
				)
				.create();
	}


}
