package com.yiche.bigdata.ad.net;

/**
 * 网络接口封装
 * 
 * @author wanglirong
 * 
 */
public interface IHttpInterface
{

    public abstract int sendRequest(String url, int requestTag, String bodydata, INetListener listener,int type);

    public abstract int sendDownLoadRequest(String url, String filepath, INetListener listener);

    public abstract void cancelRequestById(int requestId);

    public abstract void cancelAllRequest();

}
