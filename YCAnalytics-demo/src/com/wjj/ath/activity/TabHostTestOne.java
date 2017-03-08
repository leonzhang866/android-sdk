package com.wjj.ath.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.umeng.analytics.MobclickAgent;
import com.wjj.ath.R;
import com.yiche.ycanalytics.YCPlatform;
import com.yiche.ycanalytics.YCPlatformSettings;
import com.yiche.ycanalytics.bean.EventBundle;
import com.yiche.ycanalytics.controlmanager.PageType;
import com.yiche.ycanalytics.utils.Utils;
import com.yiche.ycanalytics.utils.Utils.Finish;
//import com.yiche.ycanalytics.utils.Utils;
//import com.yiche.ycanalytics.utils.Utils.Finish;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TabHostTestOne extends Activity {
	Button btn_init, btn_yclogin, btn_thirdlogin, btn_click, btn_custom,
			btn_location, btn_time, btn_jump, btn_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.one);
		btn_init = (Button) findViewById(R.id.btn_init);
		btn_yclogin = (Button) findViewById(R.id.btn_yclogin);
		btn_thirdlogin = (Button) findViewById(R.id.btn_thirdlogin);
		btn_click = (Button) findViewById(R.id.btn_click);
		btn_custom = (Button) findViewById(R.id.btn_custom);
		btn_location = (Button) findViewById(R.id.btn_location);
		btn_time = (Button) findViewById(R.id.btn_time);
		btn_jump = (Button) findViewById(R.id.btn_jump);
		btn_data = (Button) findViewById(R.id.btn_data);
//		JSONObject jsonObject=new JSONObject();
//		try {
//			jsonObject.put("slotId", "ddg");
//			jsonObject.put("creativeId", "ddg");
//			jsonObject.put("url", "ddg");
//			jsonObject.put("name", "ddg");
//			jsonObject.put("status", "ddg");
//			jsonObject.put("responseCode", "ddg");
//			jsonObject.put("responseTime", "ddg");
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		YCPlatform.addNetData(jsonObject.toString());

		btn_init.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		btn_yclogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				YCPlatform.loginStatisticsWithYCUserName("yiche", "mail");
			}
		});
		btn_thirdlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				YCPlatform.loginStatisticsWithThirdPartUserName("dsjhfksdjhfskjd", "wx");
			}
		});
		btn_click.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				YCPlatform.recordCountClickWithEvent("click_event");
				MobclickAgent.onEvent(TabHostTestOne.this, "click");
	            MobclickAgent.onEvent(TabHostTestOne.this, "click", "button");
			}
		});
		btn_custom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<EventBundle> list =new ArrayList<EventBundle>();
				list.add(new EventBundle("key1", "value1"));
				list.add(new EventBundle("key2", "value2"));
				list.add(new EventBundle("key3", "value3"));
				YCPlatform.recordCustomWithEventName("custom", list);
				
				
//				Map<String, String> map_value = new HashMap<String, String>();
//	            map_value.put("type", "popular");
//	            map_value.put("artist", "JJLin");
//	            MobclickAgent.onEventValue(TabHostTestOne.this, "music", map_value, 12000);
	            
	            HashMap<String,String> map = new HashMap<String,String>();
	            map.put("type","book");
	            map.put("quantity","3"); 
	            MobclickAgent.onEvent(TabHostTestOne.this, "purchase", map);


	            HashMap<String,String> mapA = new HashMap<String,String>();
	            mapA.put("type","car");
	            mapA.put("quantity","9"); 
	            MobclickAgent.onEvent(TabHostTestOne.this, "purchase", mapA);

	            MobclickAgent.onEvent(TabHostTestOne.this,"Forward");
	            MobclickAgent.onEvent(TabHostTestOne.this,"ForwardB");
	            
	           int duration = 12000; //开发者需要自己计算音乐播放时长
	          Map<String, String>  map_value1 = new HashMap<String, String>();
	          map_value1.put("type" , "popular" );
	          map_value1.put("artist" , "JJLin" );	
	          MobclickAgent.onEventValue(TabHostTestOne.this, "music" , map_value1, duration);


	          duration = 2333; //开发者需要自己计算音乐播放时长
	          Map<String, String> map_value_A = new HashMap<String, String>();
	          map_value_A.put("type" , "pop" );
	          map_value_A.put("artist" , "3333" );	
	          MobclickAgent.onEventValue(TabHostTestOne.this, "music" , map_value_A, duration);
	            
			}
		});
		btn_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				YCPlatform.recordLocationInfo("3455656.34", "76677766.12");
			}
		});
		btn_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				YCPlatform.recordPageInfoWithPageName("page1", 23, PageType.NATIVEVIEW);
			}
		});
		btn_jump.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it=new Intent(TabHostTestOne.this, SecondActivity.class);
				startActivity(it);
			}
		});
		btn_data.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final ProgressDialog dialog=new ProgressDialog(TabHostTestOne.this);
				dialog.setCancelable(false);
				dialog.setMessage("数据生成中...");
				dialog.show();
				Utils.creatData(new Finish() {
					@Override
					public void finish() {
						dialog.dismiss();
					}
				});
			}
		});

	}

	protected void onResume() {
		super.onResume();
		YCPlatform.beginRecordPageInfoWithPageName(TabHostTestOne.class
				.getName());
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onPause() {
		super.onPause();
		YCPlatform.endRecordPageInfoWithPageName(TabHostTestOne.class.getName());
	}
	
	private long mPressedTime=0;
	@Override
	public void onBackPressed() {
		
		long mNowTime = System.currentTimeMillis();//获取第一次按键时间
		if((mNowTime - mPressedTime) > 2000){//比较两次按键时间差
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			mPressedTime = mNowTime;
		}else{
			this.finish();
			
			YCPlatform.onAppExit(getApplicationContext());
			System.exit(0);
		}		
	}
	
	
}
