package com.yiche.ycanalytics.db;

import android.content.Context;

import com.yiche.ycanalytics.YCPlatformInternal;

/**
 * 数据库管理类
 * 
 * @author wanglirong
 * 
 */
public final class DataManager
{

    private static final Object mInstanceSync = new Object();
    private static DataManager mInstance;
    private DaoFactory mDaoFactory;

    /**
     * 对外接口，获取UserDao对象
     * 
     * @return
     */
    public synchronized static IUserDao getUserDbHandler()
    {
        return _getInstance()._getUserDbHandler();
    }

    private synchronized IUserDao _getUserDbHandler()
    {
        return mDaoFactory.getUserDao();
    }

    /**
     * @param context
     * @return
     */
    private static DataManager _getInstance()
    {

        synchronized (mInstanceSync)
        {

            if (mInstance == null)
            {
                mInstance = new DataManager();
            }
        }
        return mInstance;
    }

    /**
     * @notice should not call new DataManager() direct
     */
    private DataManager()
    {
        mDaoFactory = DaoFactory.getInstance(YCPlatformInternal.getInstance().getSDKContext());
    }

}
