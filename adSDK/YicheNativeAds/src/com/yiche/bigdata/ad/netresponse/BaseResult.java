package com.yiche.bigdata.ad.netresponse;

import com.yiche.bigdata.ad.YCErrorCode;

/**
 * 服务器返回结果基类
 * 
 * @author wanglirong
 * 
 */
public  class BaseResult
{
    protected int mErrorCode = YCErrorCode.YC_Error;
    protected int mRequestTag;
    protected int mRequestId;
    protected String mErrorString;
    protected String mAccepTime;

    public int getErrorCode()
    {
        return mErrorCode;
    }

    public void setErrorCode(int errorCode)
    {
        this.mErrorCode = errorCode;
    }

    public String getErrorString()
    {
        return mErrorString;
    }

    public void setErrorString(String errorString)
    {
        this.mErrorString = errorString;
    }

    public void setRequestTag(int requestTag)
    {
        this.mRequestTag = requestTag;
    }

    public void setRequestId(int requestId)
    {
        this.mRequestId = requestId;
    }

    public String getAcceptTime()
    {
        return mAccepTime;
    }

    public void setAccepTime(String mAccepTime)
    {
        this.mAccepTime = mAccepTime;
    }

}
