package com.yiche.ycanalytics.apkdata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.yiche.ycanalytics.YCPlatform;
import com.yiche.ycanalytics.apkdata.lib.AndroidAppProcess;
import com.yiche.ycanalytics.apkdata.lib.AndroidProcesses;
import com.yiche.ycanalytics.utils.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import static android.content.Context.ACTIVITY_SERVICE;

/**
 * 类: AdwareManager 描述: 总管理者
 */
public class AdwareManager {
	MyLogger myLogger=MyLogger.getLogger(AdwareManager.class.getName());
	List<AppBean>  mapAppProcess=new ArrayList<AppBean>();

	private static final String TAG = "AdwareManager";
	private Context mContext;

	public AdwareManager(Context context) {
		this.mContext = context;
	}
	private AppBean appBean;
	// 不断被调用，更新管理
	public void onDo() {

//		myLogger.d("onDo");
		// 更新当前最新RunningName
		synchronized (this) { 
			appBean=getTopApp();
			if(mapAppProcess.size()==0){
				if(appBean.getPackageName()==null){//处于桌面状态
//					myLogger.d("处于桌面状态");
				}else{//刚打开一个应用 开始记录打开时间
					myLogger.d("刚打开一个应用 开始记录打开时间"+appBean.getPackageName());
					appBean.setStartTime(System.currentTimeMillis());
					mapAppProcess.add(appBean);
				}
			}else{
				if(appBean.getPackageName()==null){//app已结束，处于桌面状态 
					myLogger.d("app已结束，处于桌面状态");
					//插入数据库
					insertToDb(mapAppProcess);
					//删除list
					mapAppProcess.clear();
					myLogger.d("map任务栈："+mapAppProcess.size());
					
				}else{
					//app已结束，当前手机已打开新的APP
					if(!isContainKey(mapAppProcess, appBean.getPackageName())){
						myLogger.d("app已结束，新打开app:"+appBean.getPackageName());
						//插入数据库
						insertToDb(mapAppProcess);
						//从map中删除
						mapAppProcess.clear();
						//新打开一个应用 开始记录打开时间
						appBean.setStartTime(System.currentTimeMillis());
						mapAppProcess.add(appBean);
						
					}else{
//						myLogger.d("新打开app继续运行");
					}
					
				}
					
			}
			
		}

	}
	private boolean isContainKey(List<AppBean> list ,String string){
		for(AppBean appBean:list){
			if(appBean.getPackageName().equals(string)){
				return true;
			}
		}
		return false;
	}
	
	private void insertToDb(List<AppBean> list) {
		for (AppBean appBean : list) {
			if (appBean.getPackageName() != null && appBean.getStartTime() != 0) {
				JSONObject jo = new JSONObject();
				try {
					jo.put("pkName", appBean.getPackageName());
					jo.put("during", String.valueOf((System.currentTimeMillis() - appBean.getStartTime())/1000.000));
					jo.put("updateTime", Utils.getDataFromMillis(System.currentTimeMillis()));

				} catch (JSONException e) {
					e.printStackTrace();
				}
				YCPlatform.addApkData(jo.toString());
			}
		}

	}

	public AppBean getTopApp() {
		AppBean appBean = new AppBean();
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { //
			ActivityManager am = ((ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE));
			List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(5);
			if(taskInfo!=null&&taskInfo.size()>0&&!CommonData.systemApps.contains(taskInfo.get(0).topActivity.getPackageName())){
				appBean.setPackageName(taskInfo.get(0).topActivity.getPackageName());
				appBean.setStartTime(System.currentTimeMillis());
				myLogger.d("top app = " + taskInfo.get(0).topActivity.getPackageName());
			}
		} else { // For versions Lollipop[5.0] and above
			try {
				List<AndroidAppProcess> processes = AndroidProcesses.getRunningForegroundApps(mContext);

				if (processes.size() > 0) {
					Collections.sort(processes, new Comparator<AndroidAppProcess>() {

						@Override
						public int compare(AndroidAppProcess lhs, AndroidAppProcess rhs) {

							try {
								return lhs.oom_score() - rhs.oom_score();
							} catch (IOException e) {
								e.printStackTrace();
							}
							return 0;
						}
					});
					for (AndroidAppProcess process : processes) {
						if (!CommonData.systemApps.contains(process.getPackageName()) && process.oom_score() < 120) {
							appBean.setPackageName(process.name);
							appBean.setStartTime(System.currentTimeMillis());
							break;
						}
					}
				}
			} catch (IOException e) {

			}
		}
		return appBean;
	}

}
