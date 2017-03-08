package com.yiche.bigdata.ad;

import java.util.List;
import com.yiche.bigdata.ad.bean.AdBean;
import com.yiche.bigdata.ad.utils.YCNoProguard;

public abstract class ISDKCallBack implements YCNoProguard {
    public abstract void onResponse(int status ,List<AdBean> paramString);
}
