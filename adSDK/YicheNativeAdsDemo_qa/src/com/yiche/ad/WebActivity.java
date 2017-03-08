package com.yiche.ad;

import com.example.ycadsdk_demo.R;
import com.yiche.ycanalytics.YCPlatform;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {
	WebView web;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);
		web = (WebView) findViewById(R.id.web);

		String urls = getIntent().getStringExtra("url");
		WebSettings webSettings = web.getSettings();
		webSettings.setJavaScriptEnabled(true);
		web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				web.loadUrl(url);
				return true;
			}
		});
		web.loadUrl(urls);
	}


}
