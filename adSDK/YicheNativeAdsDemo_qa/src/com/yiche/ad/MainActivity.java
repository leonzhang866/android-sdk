package com.yiche.ad;

import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ycadsdk_demo.R;
import com.yiche.bigdata.ad.ISDKCallBack;
import com.yiche.bigdata.ad.YCAdPlatform;
import com.yiche.bigdata.ad.bean.AdBean;
import com.yiche.ycanalytics.YCPlatform;

public class MainActivity extends Activity {
	
	ListView listView;
	List<AdBean> map;
	List<AdBean> list;
    int [] pids1;
    
    ProgressBar mProgressbar;
    MyAdapter adapter;
    TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		String[] pids=getIntent().getStringExtra("pid").split(",");
		String key=getIntent().getStringExtra("key");
		if(TextUtils.isEmpty(key)){
			key="6b9c869e-b306-32b4-db17-a0eea4018920";
		}
		pids1=new int[pids.length];
		for(int i=0;i<pids.length;i++){
			pids1[i]=Integer.parseInt(pids[i]);
		}
		tv=(TextView) findViewById(R.id.tv1);
		listView=(ListView) findViewById(R.id.list);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				AdBean adBean=(AdBean) adapter.getItem(arg2);
				YCAdPlatform.sendClick(adBean.getResourceId(),new View(MainActivity.this));
				if(adBean.getType()==3){
					
				}else{
					Intent it=new Intent(MainActivity.this, WebActivity.class);
					it.putExtra("url", adBean.getUrl());
					startActivity(it);
				}
			}
		});
//		getad("dab6b5f4-d976-e841-0153-98d934d0-2665-2d10-9ee2-c50184d51ee0",pids1);
		getad(key,pids1);
	}
	
	private void getad(String string, int[] pids) {
		YCAdPlatform.getAd(this, string, pids, new ISDKCallBack() {
			@Override
			public void onResponse(int status, List<AdBean> paramString) {
				Toast.makeText(MainActivity.this, status+"", 1000).show();
				if(status==2000&&paramString!=null){
					
					adapter = new MyAdapter(MainActivity.this, paramString);
					listView.setAdapter(adapter);
					if(paramString.size()>0){
						for(int i=0;i<paramString.size();i++){
							YCAdPlatform.sendExpose(paramString.get(i).getResourceId(),new View(MainActivity.this));
						}
					}
				}

			}

		});
	}
	

	//统计用户在前台页面使用的总时长  
		@Override
	    protected void onResume() {
	    	super.onResume();
			YCPlatform.beginRecordPageInfoWithPageName(MainActivity.class.getName());
	    }
		@Override
	    protected void onStop() {
	    	super.onStop();
	    }
	
		

		@Override
		protected void onPause() {
			super.onPause();
			YCPlatform.endRecordPageInfoWithPageName(MainActivity.class.getName());
		}
	
}