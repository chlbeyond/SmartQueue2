package com.smartqueue2.database.ormsql;

import android.content.Context;

import com.smartqueue2.database.orm.DaoMaster;
import com.smartqueue2.database.orm.DaoSession;
import com.smartqueue2.database.orm.QueueInfoBackupDao;
import com.smartqueue2.database.orm.QueueInfoDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * Created by Administrator on 2018/5/13.
 */

public class BackupHelper {

    private static BackupHelper instance;

    private DaoMaster daoMaster = null;
    private DaoSession daoSession = null;

    public static BackupHelper getInstance(Context context) {//通过这个来获得session实例，就是单例
        if(instance==null){
            instance = new BackupHelper(context);
        }
        return instance;
    }

    public BackupHelper(Context context){
        MySqlOpenHelper openHelper = new MySqlOpenHelper(context,"ORMDBBACKUP");//数据库名
        daoMaster = new DaoMaster(openHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

    private QueueInfoDao getDao() {
        return daoSession.getQueueInfoDao();
    }

    /**
     * 向数据库中插入一条记录
     * @param info
     */
    public void insert(QueueInfo info) {
        getDao().insert(info);
    }

    /**
     * 向数据库中插入一串数据
     * @param infoList
     */
    public void insertList(List<QueueInfo> infoList){
        getDao().insertInTx(infoList);
    }

    /**
     * 删除一条记录
     * @param info
     */
    public void delete(QueueInfo info) {
        getDao().delete(info);
    }

    /**
     * 根据id删除数据
     * @param id
     */
    public void deleteById(Long id) {
        getDao().deleteByKey(id);
    }

    /**
     * 删除所有
     */
    public void deleteAll() {
        getDao().deleteAll();
    }

    /**
     * 更新数据库
     * @param info
     */
    public void update(QueueInfo info) {
        getDao().update(info);
    }

    /**
     * 查询所有数据
     */
    public List<QueueInfo> queryAll() {
        Query<QueueInfo> query = getDao().queryBuilder().build();
        return query.list();
//        return queueInfoDao.loadAll();//加载全部
    }

    /**
     * 根据队列名QueueName查询该队列所有数据
     * @param queueName
     */
    public List<QueueInfo> queryByQueueName(String queueName) {
        Query<QueueInfo> query = getDao().queryBuilder()
                .where(QueueInfoDao.Properties.QueueName.eq(queueName))
                .build();
        return query.list();
    }

    /**
     * 根据队列名QueueName和序号num来查询该条队列
     * @param queueName
     * @param num
     */
    public QueueInfo queryByQueueNameAndNum(String queueName, int num) {
        QueueInfo info = getDao().queryBuilder()
                .where(QueueInfoDao.Properties.QueueName.eq(queueName), QueueInfoDao.Properties.Num.eq(num)).unique();
        return info;
    }

    /**
     * 根据id降序查询所有
     */
    public List<QueueInfo> queryAllByDesc() {
        Query<QueueInfo> query = getDao().queryBuilder()
                .orderDesc(QueueInfoDao.Properties.Id)//降序
                .build();
        return query.list();
    }

    /**
     *  分页查询
     * @param pageSize 当前第几页
     * @param pageNum  每页显示多少个
     * @return
     */
    public List<QueueInfo> queryPaging(int pageSize, int pageNum){
        Query<QueueInfo> query = getDao().queryBuilder()
                .offset(pageSize * pageNum).limit(pageNum)
                .build();
        return query.list();
    }

    /**
     * 根据id降序偏移查询
     * @param offset 偏移量：从第几个开始查
     * @param limit 限制：查询多少条数据
     */
    public List<QueueInfo> queryPagingByDesc(int offset, int limit){
        Query<QueueInfo> query =  getDao().queryBuilder()
                .orderDesc(QueueInfoDao.Properties.Id)
                .offset(offset).limit(limit)
                .build();
        return query.list();
    }
}
