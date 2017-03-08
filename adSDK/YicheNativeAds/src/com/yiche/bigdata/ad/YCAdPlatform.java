package com.yiche.bigdata.ad;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.yiche.bigdata.ad.utils.YCNoProguard;

public class YCAdPlatform implements YCNoProguard{
	
    private static YCPlatformInternal mPlatformInternal = YCPlatformInternal.getInstance();
    /*
     *获取广告
     */
	public static void getAd(Context context ,String pubid,int[] pids ,ISDKCallBack isdkCallBack){
		try{
			if(context==null||pids==null||TextUtils.isEmpty(pubid)){
				return;
			}
			mPlatformInternal.getAd(context,pubid,pids,isdkCallBack);
		}catch(Throwable t){
			
		}
		
	}

	
	/*
	  广告曝光接口
	  输入参数：广告位ID;素材ID;view为展示此广告的view;visiblePercent为当前广告可见度，取0-100整数值
	  输出参数：无
	*/
	public static void sendExpose(String resourceId,View view){
		try{
			if(TextUtils.isEmpty(resourceId)||view==null){
				return;
			}
			mPlatformInternal.sendEffectiveExpose(resourceId,view);
		}catch(Throwable t){
			
		}
		
	}

	/*
	  广告点击接口
	  输入参数：广告位ID，素材ID，view为展示此广告的view
	  输出参数：无
	*/
	public static void sendClick(String resourceId,View adView){
		try{
			mPlatformInternal.sendClick(resourceId,adView);
		}catch(Throwable t){
			
		}
		
	}


	/*
	  释放广告对象和物料接口，
	  输入参数：无
	  输出参数：无
	  说明：广告不再展示后必须调用此接口用来释放资源
	*/
	public static void releaseAd(String resourceId){
		try{
			mPlatformInternal.releaseAd(resourceId);
		}catch(Throwable t){
			
		}
		
	}
	/*
	  释放广告对象和物料接口，
	  输入参数：无
	  输出参数：无
	  说明：广告不再展示后必须调用此接口用来释放资源
	*/
	public static void releaseAd(){
		try{
			mPlatformInternal.releaseAd();
		}catch(Throwable t){
			
		}
		
	}

}
