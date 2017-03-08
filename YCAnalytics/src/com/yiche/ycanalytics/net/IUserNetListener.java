package com.yiche.ycanalytics.net;

import com.yiche.ycanalytics.netresponse.BaseResult;


public interface IUserNetListener extends INetListener
{
    public abstract void onNetResponse(int requestTag, BaseResult responseData, int requestId, int loginType);
}
