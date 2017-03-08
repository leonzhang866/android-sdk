package com.example.ycadsdk_demo;

import java.util.List;
import com.yiche.bigdata.ad.ISDKCallBack;
import com.yiche.bigdata.ad.YCAdPlatform;
import com.yiche.bigdata.ad.bean.AdBean;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
/**
 * 
 * 广告SDKv1.1.0依赖于监测sdk v1.1.0版本，请先接入监测SDK
 *
 */
public class MainActivity extends Activity {
   private Button btn1,btn2,btn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1=(Button) findViewById(R.id.btn1);
        btn2=(Button) findViewById(R.id.btn2);
        btn3=(Button) findViewById(R.id.btn3);
        btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				YCAdPlatform.getAd(getApplicationContext(), "6b9c869e-b306-32b4-db17-a0eea4018920", new int[]{2384}, new ISDKCallBack() {
					
					@Override
					public void onResponse(int status, List<AdBean> list) {
						if(status==2000){
							//TODO 物料返回后，由开发者自己去渲染广告内容
							Toast.makeText(MainActivity.this, status+"", 1000).show();
							//status==2000 表示请求成功
							//Adbean 字段说明：
							//resourceId	广告对象唯一标识
							//pid	广告位id
							//picUrl	广告素材图片地址,(视频类型:该图片地址则为封面图片)
							//url	广告素材落地页 (视频类型素材,该地址为视频资源地址)
							//creativeid	物料id
							//type	广告类型 0:图文  1:组图 2:大图 3:视频
							//title	广告标题
							
						}
					}
				});
			}
		});
        btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//发送曝光统计
				//resourceId为该物料唯一识别标识，可以在AdBean中取得
				YCAdPlatform.sendExpose("dkrgj5ogeho5eowogrone5", v);
			}
		});
        btn3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//广告点击统计接口
				//resourceId为该物料唯一识别标识，可以在AdBean中取得
				YCAdPlatform.sendClick("dkrgj5ogeho5eowogrone5", v);
			}
		});
    }
}
