package com.yiche.ycanalytics.net;

import com.yiche.ycanalytics.netresponse.BaseResult;


/**
 * 联网回调接口、成功、失败等
 * 
 * @author wanglirong
 * 
 */
public interface INetListener {

    enum DownLoadStatus {
        EDlsInit,
        EDlsDownLoading,
        EDlsDownLoadComplete,
        EDlsDownLoadErr,
    };

    /**
     * 网络正常返回
     * 
     * @param requestTag tag
     * @param responseData 返回数据
     * @param requestId 请求id
     */
    public abstract void onNetResponse(int requestTag, BaseResult responseData, int requestId);

    public abstract void onDownLoadStatus(DownLoadStatus status, int requestId);

    public abstract void onDownLoadProgressCurSize(long curSize, long totalSize, int requestId);

    /**
     * 网络异常
     * 
     * @param requestTag
     * @param requestId
     * @param errorCode
     * @param msg
     */
    public abstract void onNetResponseErr(int requestTag, int requestId, int errorCode, String msg);

   

}
