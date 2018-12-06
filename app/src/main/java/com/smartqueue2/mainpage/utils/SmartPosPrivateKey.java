package com.smartqueue2.mainpage.utils;

public class SmartPosPrivateKey {
	public static final String SP_RD_SALT = "salt";
	public static final String SP_RD_DEVICEID = "device_id";
	public static final String SP_RD_SHOPID = "shop_id";
	public static final String SP_RD_ACCESSCODE = "access_code";
	public static final String SP_RD_ISMASTER = "is_master";
	public static final String SP_RD_DEVICEREGISTERED = "device_registered";
	public static final String SP_RD_POSNAME = "pos";
	public static final String SP_RD_ANGET_ADDRESS = "agent_address";

	public static final String ST_LOCAL_USB_PRINTER = "local_usb_printer_config";
	public static final String ST_LOCAL_COM_PRINTER = "local_com_printer_config";
	public static final String ST_LOCAL_BAUDRATE_PRINTER = "local_baudrate_printer_config";
	public static final String ST_PUBLICE_DEVICE = "is_public_device";
	public static final String ST_POPUP_KEYBOARD = "is_popup_keyboard";
	public static final String ST_SPELL_ORDER = "is_spell_order";
	public static final String ST_PRE_IS_EXIT = "pre_is_exit";
	public static final String ST_IS_SNACK_PATTERN = "is_snack_pattern";
	public static final String ST_LOCAL_PRESENTATION="local_media_directory";

	public static final String ST_LOCAL_ENCODE_TYPE="is_num";
	public static final String PACKGE_NAME = "com.go2smartphone.smartpos";


	public static final String ACCOUNT = "account";//账号
	public static final String PASSWORD = "password";//密码

	public static final String LOCKPASSWORD = "lockPassword";//解锁密码
	public static final String SWITCHSCREEN = "switchScreen"; //横竖屏切换

	public static final String ID = "id";//id
	public static final String USER = "user";//userId

	public static final String SHOP_NAME = "shopName";//门店名字
	public static final String ADDRESS = "address";//门店地址
	public static final String PHONE = "phone";//门店电话
	public static final String REMARK = "remark";//备注

	public static final String A_1 = "A_1";//队列A的最小人数
	public static final String A_2 = "A_2";//队列A的最大人数
	public static final String B_1 = "B_1";
	public static final String B_2 = "B_2";
	public static final String C_1 = "C_1";
	public static final String C_2 = "C_2";
	public static final String D_1 = "D_1";
	public static final String D_2 = "D_2";
	public static final String E_1 = "E_1";
	public static final String E_2 = "E_2";

	public static final String A_ID = "A_ID";//队列A的id
	public static final String B_ID = "B_ID";
	public static final String C_ID = "C_ID";
	public static final String D_ID = "D_ID";
	public static final String E_ID = "E_ID";

	public static final String A_NUM = "A_num";//队列A最大的序号，取号时要根据这个来生成序号
	public static final String B_NUM = "B_num";
	public static final String C_NUM = "C_num";
	public static final String D_NUM = "D_num";
	public static final String E_NUM = "E_num";

	public static final String QUEUE_NUM = "Queue_num";//总共多少队列，队列数量
	public static final String QUEUE_NAME= "Queue_Name";//队列的名字字符串

	public static final String TABEL_BEUSED = "table_beused";//被使用的表
	public static final String TABEL_QUEUE = "table_queue";//排队表
	public static final String TABEL_BACKUP = "table_backup";//备份表

	public static final String ISEMPTYQUEUE = "isEmptyQueue";//是否清空排队数据
	public static final String ISCHANGESHOP= "isChangeShop";//是否修改门店信息

}
