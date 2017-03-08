package com.yiche.bigdata.ad;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.yiche.bigdata.ad.bean.AdBean;
import com.yiche.bigdata.ad.bean.YCAdBean;
import com.yiche.bigdata.ad.json.JSONManager;
import com.yiche.bigdata.ad.net.ASIHttpRequest;
import com.yiche.bigdata.ad.net.INetListener;
import com.yiche.bigdata.ad.net.NetManager;
import com.yiche.bigdata.ad.netresponse.AdResult;
import com.yiche.bigdata.ad.netresponse.BaseResult;
import com.yiche.bigdata.ad.utils.Constants;
import com.yiche.bigdata.ad.utils.MyLogger;
import com.yiche.bigdata.ad.utils.Utils;
import com.yiche.bigdata.ad.utils.YCNoProguard;
import com.yiche.ycanalytics.YCPlatform;

public class YCPlatformInternal implements YCNoProguard{
	
	private static YCPlatformInternal mInstance;
    private Context mContext;
    private static MyLogger mLogger = MyLogger.getLogger(YCPlatformInternal.class.getName());
    private Map<String,YCAdBean> map=new ConcurrentHashMap<String, YCAdBean>();
    private YCPlatformInternal()
    {
    }
   
    public synchronized static YCPlatformInternal getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new YCPlatformInternal();
        }
        return mInstance;
    }
    

	public Context getSDKContext()
    {
        return mContext;
    }

	public void getAd(Context context, String pubid, final int[] pids,final ISDKCallBack isdkCallBack) {
		if(Utils.isShowLogcat()){
			Constants.DEBUG=true;
		}
		mContext=context;
		String bodyData = JSONManager.getJsonBuilder().buildGetADJsonObject(context,pubid,pids).toString();
		final long currentTimes=System.currentTimeMillis();
        NetManager.getHttpConnect().sendRequest(Constants.YC_SERVEL_URL, Constants.NET_TAG_DEFAULT, bodyData,new INetListener() {
			
			@Override
			public void onNetResponseErr(int requestTag, int requestId, int errorCode,
					String msg) {
				isdkCallBack.onResponse(errorCode,null);
				long during=System.currentTimeMillis()-currentTimes;
				YCPlatform.addNetData(addNetData(pids,"",Constants.YC_SERVEL_URL,"dad-ad-reqeust","1","",String.valueOf((during/1000.000))));
			}
			
			@Override
			public void onNetResponse(int requestTag, BaseResult responseData,
					int requestId) {
				long during=System.currentTimeMillis()-currentTimes;
				AdResult adResult=(AdResult)responseData;
				List<AdBean> resp=adResult.getList();
				List<YCAdBean> list=new ArrayList<YCAdBean>();
				StringBuffer stringBuffer=new StringBuffer();
				if(resp!=null&&resp.size()>0){
					for(int i=0;i<resp.size();i++){
						YCAdBean ycAdBean=new YCAdBean();
						ycAdBean.setCreativeId(resp.get(i).getCreativeId());
						stringBuffer.append(resp.get(i).getCreativeId());
						stringBuffer.append(",");
						ycAdBean.setDvid(resp.get(i).getDvid());
						ycAdBean.setPicUrls(resp.get(i).getPicUrls());
						ycAdBean.setPid(resp.get(i).getPid());
						ycAdBean.setResourceId(resp.get(i).getResourceId());
						ycAdBean.setTitle(resp.get(i).getTitle());
						ycAdBean.setType(resp.get(i).getType());
						ycAdBean.setUrl(resp.get(i).getUrl());
						ycAdBean.setExposureTp(resp.get(i).getExposureTp());
						ycAdBean.setClickTp(resp.get(i).getClickTp());
						list.add(ycAdBean);
						map.put(resp.get(i).getResourceId(), ycAdBean);
						resp.get(i).setClickTp("");
						resp.get(i).setExposureTp("");
					}
				}
				
				isdkCallBack.onResponse(adResult.getErrorCode(),resp);
				YCPlatform.addNetData(addNetData(pids,stringBuffer.toString(),Constants.YC_SERVEL_URL,"dad-ad-reqeust","0",String.valueOf(adResult.getErrorCode()),String.valueOf(during/1000.000)));
				
			}
			
			@Override
			public void onDownLoadStatus(DownLoadStatus status, int requestId) {
				
			}
			
			@Override
			public void onDownLoadProgressCurSize(long curSize, long totalSize,
					int requestId) {
				
			}
		},ASIHttpRequest.POST);
	}
	protected String addNetData(int[] slotId, String creativeId, String url,
			String name, String status, String responseCode, String responseTime) {
		StringBuffer stringBuffer=new StringBuffer();
		if(slotId.length>0){
			for(int i=0;i<slotId.length;i++){
				stringBuffer.append(slotId[i]);
				stringBuffer.append(",");
			}
		}
		JSONObject jo=new JSONObject();
		try {
			jo.put("slotId", stringBuffer.toString());
			jo.put("creativeId", creativeId);
			jo.put("url", url);
			jo.put("name", name);
			jo.put("status", status);
			jo.put("responseCode", responseCode);
			jo.put("responseTime", responseTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo.toString();
		
	}

	public void sendEffectiveExpose(String resourceId,View view) {
		boolean isCovered=Utils.isViewCovered(view);
		final YCAdBean adBean=map.get(resourceId);
		if(adBean==null){return;}
		String url = JSONManager.getJsonBuilder().buildExposeUrl(adBean,isCovered);
        NetManager.getHttpConnect().sendRequest(url, Constants.NET_TAG_EXPOSE, null,new INetListener() {
			
			@Override
			public void onNetResponseErr(int requestTag, int requestId, int errorCode,
					String msg) {
			}
			
			@Override
			public void onNetResponse(int requestTag, BaseResult responseData,
					int requestId) {
				if(responseData!=null){
					if(responseData.getErrorCode()==2000){
						String urls[]=adBean.getExposureTp().split(",");
						if(urls.length==0){
							return;
						}
						for(int i=0;i<urls.length;i++){
							if("".equals(urls[i])){
								sendThirdExpose(urls[i]);
							}
						}
					}
				}
				
			}
			
			@Override
			public void onDownLoadStatus(DownLoadStatus status, int requestId) {
				
			}
			
			@Override
			public void onDownLoadProgressCurSize(long curSize, long totalSize,
					int requestId) {
				
			}
		},ASIHttpRequest.GET);
	}
	/**
	 * 发送第三方曝光
	 * @param resourceId
	 * @param percent
	 * @param view
	 */
	public void sendThirdExpose(String url) {
		if(TextUtils.isEmpty(url)){
			return;
		}
        NetManager.getHttpConnect().sendRequest(url, Constants.NET_TAG_EXPOSE, null,new INetListener() {
			
			@Override
			public void onNetResponseErr(int requestTag, int requestId, int errorCode,
					String msg) {
			}
			
			@Override
			public void onNetResponse(int requestTag, BaseResult responseData,
					int requestId) {
				
			}
			
			@Override
			public void onDownLoadStatus(DownLoadStatus status, int requestId) {
				
			}
			
			@Override
			public void onDownLoadProgressCurSize(long curSize, long totalSize,
					int requestId) {
				
			}
		},ASIHttpRequest.GET);
	}

	public void sendClick(String resourceId, View view) {
		final YCAdBean adBean=map.get(resourceId);
		if(adBean==null){return;}
		if(!TextUtils.isEmpty(adBean.getClickTp())){
			String urls[]=adBean.getClickTp().split(",");
			for(int i=0;i<urls.length;i++){
				sendThirdExpose(urls[i]);
			}
		}
		
	}

	public void releaseAd(String resourceId) {
		if(map!=null&&!TextUtils.isEmpty(resourceId)){
			map.remove(resourceId);
		}
	}

	public void releaseAd() {
		if(map!=null){
			map.clear();
		}
	}
}
