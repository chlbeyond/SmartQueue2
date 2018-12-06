package com.smartqueue2.database.ormsql;

import android.content.Context;

import com.smartqueue2.database.orm.DaoMaster;
import com.smartqueue2.database.orm.DaoSession;
import com.smartqueue2.database.orm.QueueSetInfoDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * Created by Administrator on 2018/6/20.
 */

public class QueueSetInfoHelper {
    private static QueueSetInfoHelper instance;

    private DaoMaster daoMaster = null;
    private DaoSession daoSession = null;

    public static QueueSetInfoHelper getInstance(Context context) {//通过这个来获得session实例，就是单例
        if(instance==null){
            instance = new QueueSetInfoHelper(context);
        }
        return instance;
    }

    public QueueSetInfoHelper(Context context){
        MySqlOpenHelper openHelper = new MySqlOpenHelper(context,"ORMDB");//数据库名
        daoMaster = new DaoMaster(openHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

    private QueueSetInfoDao getDao() {
        return daoSession.getQueueSetInfoDao();
    }

    /**
     * 向数据库中插入一条记录
     * @param info
     */
    public void insert(QueueSetInfo info) {
        getDao().insert(info);
    }

    /**
     * 向数据库中插入多条数据
     * @param infoList
     */
    public void insertList(List<QueueSetInfo> infoList){
        getDao().insertInTx(infoList);
    }

    /**
     * 删除一条记录
     * @param info
     */
    public void delete(QueueSetInfo info) {
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
    public void update(QueueSetInfo info) {
        getDao().update(info);
    }

    /**
     * 查询所有数据
     */
    public List<QueueSetInfo> queryAll() {
        Query<QueueSetInfo> query = getDao().queryBuilder().build();
        return query.list();
//        return queueInfoDao.loadAll();//加载全部
    }

    /**
     * 根据id查询该队列设置
     * @param id
     */
    public List<QueueSetInfo> queryById(Long id) {
        Query<QueueSetInfo> query = getDao().queryBuilder()
                .where(QueueSetInfoDao.Properties.Id.eq(id))
                .build();
        return query.list();
    }

    /**
     * 根据id降序查询所有
     */
    public List<QueueSetInfo> queryAllByDesc() {
        Query<QueueSetInfo> query = getDao().queryBuilder()
                .orderDesc(QueueSetInfoDao.Properties.Id)//降序
                .build();
        return query.list();
    }

    /**
     *  分页查询
     * @param pageSize 当前第几页
     * @param pageNum  每页显示多少个
     * @return
     */
    public List<QueueSetInfo> queryPaging(int pageSize, int pageNum){
        Query<QueueSetInfo> query = getDao().queryBuilder()
                .offset(pageSize * pageNum).limit(pageNum)
                .build();
        return query.list();
    }

    /**
     * 根据id降序偏移查询
     * @param offset 偏移量：从第几个开始查
     * @param limit 限制：查询多少条数据
     */
    public List<QueueSetInfo> queryPagingByDesc(int offset, int limit){
        Query<QueueSetInfo> query =  getDao().queryBuilder()
                .orderDesc(QueueSetInfoDao.Properties.Id)
                .offset(offset).limit(limit)
                .build();
        return query.list();
    }
}
