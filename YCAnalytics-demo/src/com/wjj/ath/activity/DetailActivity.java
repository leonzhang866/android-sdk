package com.wjj.ath.activity;

import com.wjj.ath.R;
import com.yiche.ycanalytics.YCPlatform;
import android.app.Activity;
import android.os.Bundle;

public class DetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
	}

	@Override
	protected void onResume() {
		super.onResume();
		YCPlatform.beginRecordPageInfoWithPageName(DetailActivity.class
				.getName());
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onPause() {
		super.onPause();
		YCPlatform.endRecordPageInfoWithPageName(DetailActivity.class
				.getName());
	}
}