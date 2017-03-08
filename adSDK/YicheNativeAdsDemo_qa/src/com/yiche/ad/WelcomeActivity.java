package com.yiche.ad;


import com.example.ycadsdk_demo.R;
import com.yiche.ycanalytics.YCPlatform;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WelcomeActivity extends Activity{
	Button button2;
	EditText editText,editText2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		editText=(EditText) findViewById(R.id.button1);
		editText2=(EditText) findViewById(R.id.button3);
		button2=(Button) findViewById(R.id.button2);
		
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String s=editText.getText().toString().trim();
				String ss=editText2.getText().toString().trim();
				
				if(s.contains("，")||TextUtils.isEmpty(s)){
					Toast.makeText(WelcomeActivity.this, "广告位不合法",1000).show();
					return;
				}
				if(TextUtils.isEmpty(ss)){
//					Toast.makeText(WelcomeActivity.this, "输入key为空，将使用默认key",1000).show();
//					return;
				}
				Intent  it=new Intent(WelcomeActivity.this, MainActivity.class);
				it.putExtra("pid", s);
				it.putExtra("key", ss);
				startActivity(it);
			}
		});
		
		

	}
	
	//统计用户在前台页面使用的总时长  
		@Override
	    protected void onResume() {
	    	super.onResume();
			YCPlatform.beginRecordPageInfoWithPageName(WelcomeActivity.class.getName());
	    }

		@Override
		protected void onPause() {
			super.onPause();
			YCPlatform.endRecordPageInfoWithPageName(WelcomeActivity.class.getName());
		}

}
