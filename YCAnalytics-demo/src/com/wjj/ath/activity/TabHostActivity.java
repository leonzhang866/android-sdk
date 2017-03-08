package com.wjj.ath.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import com.umeng.analytics.MobclickAgent;
import com.wjj.ath.R;
import com.wjj.ath.widget.AnimationTabHost;
import com.yiche.ycanalytics.YCPlatform;
import com.yiche.ycanalytics.YCPlatformSettings;
import com.yiche.ycanalytics.utils.Constants;

public class TabHostActivity extends TabActivity implements OnTabChangeListener {
//	private GestureDetector gestureDetector;
	private AnimationTabHost mTabHost;
	private TabWidget mTabWidget;
	/** 记录当前分页ID */
	private int currentTabID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Constants.DEBUG=true;
		mTabHost = (AnimationTabHost) findViewById(android.R.id.tabhost);
		mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
		mTabHost.setOnTabChangedListener(this);
		init();
		onTabChanged("0");// 人为调用回调方法，初始化选项卡tabs的颜色
//		gestureDetector = new GestureDetector(new TabHostTouch());
		//MobclickAgent.setDebugMode(true);
		MobclickAgent.openActivityDurationTrack(false);
		 MobclickAgent.setAutoLocation(true);
         MobclickAgent.setSessionContinueMillis(1000);

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		YCPlatform.beginRecordPageInfoWithPageName(TabHostActivity.class.getName());
		
		MobclickAgent.onPageStart(TabHostActivity.class.getName());
        MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		YCPlatform.endRecordPageInfoWithPageName(TabHostActivity.class.getName());
		
		 MobclickAgent.onPageEnd(TabHostActivity.class.getName());
	        MobclickAgent.onPause(this);
	}

	private void init() {
		setIndicator(R.drawable.icon_1_c, "模拟操作", new Intent(this,
				TabHostTestOne.class));
		setIndicator(R.drawable.icon_2_c, "设备信息", new Intent(this,
				TabHostTestTwo.class));
		setIndicator(R.drawable.icon_3_c, "选车", new Intent(this,
				TabHostTestThree.class));
		setIndicator(R.drawable.icon_4_c, "优惠", new Intent(this,
				TabHostTestFour.class));
		setIndicator(R.drawable.icon_4_c, "html", new Intent(this,
				SecondActivity.class));
		mTabHost.setOpenAnimation(false);
	}

	private void setIndicator(int icon, String tabId, Intent intent) {
		String str = tabId;
		TabHost.TabSpec localTabSpec = mTabHost.newTabSpec(str)
				.setIndicator(str, getResources().getDrawable(icon))
				.setContent(intent);
		mTabHost.addTab(localTabSpec);
	}

	@Override
	public void onTabChanged(String tabId) {
		// tabId 为newTabSpec(String tag) 中传入的字符串tag，这里tag是0,1,2,3 可以转换为整形便于判断
		int tabID=0 ;
		if(tabId.equals("模拟操作")){
			tabID =0;
		}else if(tabId.equals("设备信息")){
			tabID =1;
		}else if(tabId.equals("选车")){
			tabID =2;
		}else if(tabId.equals("优惠")){
			tabID =3;
		}else if(tabId.equals("html")){
			tabID =4;
		}
		for (int i = 0; i < mTabWidget.getChildCount(); i++) {
			if (i == tabID) {
				mTabWidget.getChildAt(i).setBackgroundResource(
						R.drawable.indicator_selected);
			} else {
				mTabWidget.getChildAt(i).setBackgroundResource(
						R.drawable.indicator_unselected);
			}
		}
	}

//	@Override
//	public boolean dispatchTouchEvent(MotionEvent event) {
//		if (gestureDetector.onTouchEvent(event)) {
//			event.setAction(MotionEvent.ACTION_CANCEL);
//		}
//		return super.dispatchTouchEvent(event);
//	}

	private class TabHostTouch extends SimpleOnGestureListener {
		/** 滑动翻页所需距离 */
		private static final int ON_TOUCH_DISTANCE = 80;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// 右滑动，切换到左边一个tab
			if (e2.getX() - e1.getX() >= ON_TOUCH_DISTANCE) {
				currentTabID = mTabHost.getCurrentTab() - 1;
				if (currentTabID < 0) {// 循环
					currentTabID = mTabHost.getTabCount() - 1;
				}
			}
			// 左滑动，切换到右边一个tab
			else if (e1.getX() - e2.getX() >= ON_TOUCH_DISTANCE) {
				currentTabID = mTabHost.getCurrentTab() + 1;
				if (currentTabID >= mTabHost.getTabCount()) {// 循环
					currentTabID = 0;
				}
			}
			mTabHost.setCurrentTab(currentTabID);
			return false;
		}
	}
	
	
}
