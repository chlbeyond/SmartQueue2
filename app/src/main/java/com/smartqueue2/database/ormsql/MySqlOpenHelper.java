package com.smartqueue2.database.ormsql;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.smartqueue2.database.orm.DaoMaster;
import com.smartqueue2.database.orm.QueueInfoDao;

import org.greenrobot.greendao.database.Database;


/**
 * Created by Administrator on 2018/1/31 0031.
 * 由于DaoMaster.DevOpenHelper的升级数据库很暴力，
 * 所以我们要继承OpenHelper再复写升级方法，当然如果不升级的话，可以不用写
 */

public class MySqlOpenHelper extends DaoMaster.OpenHelper {

    public MySqlOpenHelper(Context context, String name) {
        super(context, name);
    }

    public MySqlOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        },QueueInfoDao.class);
    }
}
