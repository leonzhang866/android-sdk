package com.yiche.bigdata.ad.net;

import com.yiche.bigdata.ad.netresponse.BaseResult;


public interface IUserNetListener extends INetListener
{
    public abstract void onNetResponse(int requestTag, BaseResult responseData, int requestId, int loginType);
}
