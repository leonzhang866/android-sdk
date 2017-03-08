package com.yiche.ycanalytics.net;

/**
 * 网络管理类
 * 
 * @author wanglirong
 * 
 */
public class NetManager {
    private static final Object mInstanceSync = new Object();
    private static NetManager mNetManagerInstance = null;

    private HttpImpl mHttpImpl = null;

    public static void initNetMgr() {
        _getNetManager();
    }

    /**
     * Below API is Public API
     * 
     * @return
     */
    public static IHttpInterface getHttpConnect() {
        NetManager _manager = _getNetManager();
        return _manager.mHttpImpl;
    }

    /**
     * Below API is Private API
     * 
     * @return
     */
    private static NetManager _getNetManager() {

        synchronized (mInstanceSync) {

            if (mNetManagerInstance == null) {
                mNetManagerInstance = new NetManager();
            }
        }
        return mNetManagerInstance;
    }

    /**
     * Contruct
     */
    private NetManager() {
        mHttpImpl = new HttpImpl();
    }
}
