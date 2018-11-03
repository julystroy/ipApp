/*
package com.cartoon.utils;

import com.cartoon.CartoonApp;
import com.cartoon.bean.BangaiDownLoadList;
import com.cartoon.bean.CartoonDown;
import com.cartoon.bean.CartoonItem;
import com.cartoon.bean.Listener;
import com.cartoon.greendao.BangaiDownDao;
import com.cartoon.greendao.CartoonDownDao;
import com.cartoon.greendao.CartoonItemDao;
import com.cartoon.greendao.DaoSession;
import com.cartoon.greendao.ListenerDao;
import com.cartton.library.utils.DebugLog;

import java.util.List;

import de.greenrobot.dao.query.Query;
import fm.qingting.download.DownloadHelper;

*/
/**
 * 数据库操作类
 * <p/>
 * Created by David on 16/6/26.
 *//*

public class GreenDaoUtils {

    private DaoSession daoSession;

    public GreenDaoUtils() {
        this.daoSession = CartoonApp.getInstance().getDaoSession();
    }

    */
/**
     * 漫画信息
     *//*

    public CartoonDownDao getCartoonDownDao() {
        return daoSession.getCartoonDownDao();
    }

    public BangaiDownDao getBangaiDownDao() {return daoSession.getBangaiDownDao();}

    public CartoonItemDao getCartoonItemDao() {
        return daoSession.getCartoonItemDao();
    }

    public ListenerDao getListenerDao() {
        return daoSession.getListenerDao();
    }

    */
/**
     * 插入漫画下载列表
     *//*

    public void insertCartoonDownList(List<CartoonDown> list) {
        if (list == null) return;

        for (CartoonDown down : list) {
            insertCartoonDown(down);
        }
    }

    */
/**
     * 插入漫画下载条目
     *//*

    public void insertCartoonDown(CartoonDown down) {
        List<CartoonItem> list = down.getContent();
        if (list != null) {
            for (CartoonItem item : list) {
                DebugLog.i("...item..." + item.getId());
                getCartoonItemDao().insertOrReplace(item);
            }
        }
        getCartoonDownDao().insertOrReplace(down);
    }

    public void insertCartoonItem(CartoonItem item) {
        getCartoonItemDao().insertOrReplace(item);
    }

    public boolean exitCarttonDown(String cartoon_id) {
        if (getCartoonDownById(cartoon_id) != null) {
            return true;
        }
        return false;
    }

    public void deleteCartoonDown(String down_id) {
        DebugLog.i("...down_id.." + down_id);
        getCartoonDownDao().deleteByKey(down_id);
    }

    */
/**
     * @return
     *//*

    public List<CartoonDown> getCartoonDownList() {
        Query query = getCartoonDownDao().queryBuilder().
                orderAsc(CartoonDownDao.Properties.Collect).build();
        return query.list();
    }

    public CartoonDown getCartoonDownById(String id) {
        CartoonDown down = null;

        Query query = getCartoonDownDao().queryBuilder()
                .where(CartoonDownDao.Properties.Id.eq(id))
                .build();

        if (!query.list().isEmpty()) {
            down = (CartoonDown) query.list().get(0);
        }


        return down;
    }

    */
/**
     * 插入收听信息
     *
     * @param listener
     *//*

    public void insertListener(Listener listener) {
        getListenerDao().insertOrReplace(listener);
    }

    public Listener queryListener(String program_id) {
        Listener listener = null;
        Query query = getListenerDao().queryBuilder()
                .where(ListenerDao.Properties.Program_id.eq(program_id),
                        ListenerDao.Properties.State.eq(DownloadHelper.DownloadState.STATE_COMPLETED))
                .build();

        if (!query.list().isEmpty()) {
            listener = (Listener) query.list().get(0);
        }
        return listener;
    }

    public Listener queryListenerById(String id) {
        Listener listener = null;
        Query query = getListenerDao().queryBuilder()
                .where(ListenerDao.Properties.Id.eq(id))
                .build();

        if (!query.list().isEmpty()) {
            listener = (Listener) query.list().get(0);
        }
        return listener;
    }

    public List<Listener> getListenerList() {
        Query query = getListenerDao().queryBuilder().
                orderAsc(ListenerDao.Properties.Collect).build();
        return query.list();
    }

    public void deleteListener(Listener listener) {
        getListenerDao().deleteByKey(listener.getId());
    }

    */
/**
     * 插入番外下载条目
     *//*

    public void insertBangaiDown(BangaiDownLoadList down) {
        if (down != null) {

            getBangaiDownDao().insertOrReplace(down);
        }
    }

    public boolean exitBangaiDown(String cartoon_id) {
        if (getBangaiDownById(cartoon_id) != null) {
            return true;
        }
        return false;
    }

    public void deleteBangaiDown(String down_id) {
        DebugLog.i("...down_id.." + down_id);
        getBangaiDownDao().deleteByKey(down_id);
    }

    */
/**
     * @return
     *//*

    public List<BangaiDownLoadList> getBangaiDownList() {
        if (BangaiDownDao.Properties.Id != null) {

            Query query = getBangaiDownDao().queryBuilder().
                    orderAsc(BangaiDownDao.Properties.Id).build();

            return query.list();
        }else
            return null;
    }

    public BangaiDownDao getBangaiDownById(String id) {
        BangaiDownDao down = null;

        Query query = getBangaiDownDao().queryBuilder()
                .where(BangaiDownDao.Properties.Id.eq(id))
                .build();

        if (!query.list().isEmpty()) {
            down = (BangaiDownDao) query.list().get(0);
        }


        return down;
    }

}
*/
