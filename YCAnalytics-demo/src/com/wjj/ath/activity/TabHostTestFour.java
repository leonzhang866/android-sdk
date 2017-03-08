package com.wjj.ath.activity;

import com.wjj.ath.R;
import com.yiche.ycanalytics.YCPlatform;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class TabHostTestFour extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.four);
	}
	
	protected void onResume() {
		super.onResume();
		YCPlatform.beginRecordPageInfoWithPageName(TabHostTestFour.class
				.getName());
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onPause() {
		super.onPause();
		YCPlatform.endRecordPageInfoWithPageName(TabHostTestFour.class
				.getName());
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
