package com.wjj.ath.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.wjj.ath.R;
import com.yiche.ycanalytics.YCPlatform;
//import com.yiche.ycanalytics.json.JSONManager;

public class TabHostTestTwo extends Activity {
	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.two);
		textView=(TextView) findViewById(R.id.text);
//		textView.setText(JSONManager.getJsonBuilder().buildInitJsonObject().toString());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		YCPlatform.beginRecordPageInfoWithPageName(TabHostTestTwo.class.getName());
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		YCPlatform.endRecordPageInfoWithPageName(TabHostTestTwo.class.getName());
		
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