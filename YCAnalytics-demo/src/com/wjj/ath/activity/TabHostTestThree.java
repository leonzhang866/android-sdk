package com.wjj.ath.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wjj.ath.R;
import com.yiche.ycanalytics.YCPlatform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class TabHostTestThree extends Activity {
	ListView listview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.three);
		listview=(ListView) findViewById(R.id.list);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
                R.layout.list, new String[] { "text" },
                new int[] { R.id.list_tv });
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it =new Intent(TabHostTestThree.this,DetailActivity.class);
				startActivity(it);
			}
		});
	}

	private List<Map<String, String>> getData() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for(int i=0;i<100;i++){
			 Map<String, String> map = new HashMap<String, String>();
		        map.put("text", "test"+i);
		        list.add(map);
		}
        return list;
    }

	@Override
	protected void onResume() {
		super.onResume();
		YCPlatform.beginRecordPageInfoWithPageName(TabHostTestThree.class
				.getName());
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onPause() {
		super.onPause();
		YCPlatform.endRecordPageInfoWithPageName(TabHostTestThree.class
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