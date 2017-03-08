package com.wjj.ath.activity;

import com.wjj.ath.R;
import com.yiche.ycanalytics.YCPlatform;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class SecondActivity extends Activity{
	WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);
		webView = (WebView) findViewById(R.id.web);
		WebSettings webSettings = webView.getSettings();
		if(webSettings!=null){
			webSettings.setJavaScriptEnabled(true);
		}
		webView.loadUrl("http://m.yiche.com");
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				YCPlatform.onPageStarted(url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
			}
		});
		
	}
	
	protected void onResume() {
		super.onResume();
		YCPlatform.beginRecordPageInfoWithPageName(SecondActivity.class
				.getName());
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onPause() {
		super.onPause();
		YCPlatform.endRecordPageInfoWithPageName(SecondActivity.class.getName());
		YCPlatform.onPageEnded(this);
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
