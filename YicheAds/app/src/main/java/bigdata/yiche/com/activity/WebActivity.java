package bigdata.yiche.com.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.yiche.ycanalytics.YCPlatform;

import bigdata.yiche.com.R;

/**
 * Created by wanglirong on 16/10/11.
 */

public class WebActivity extends Activity {
    private WebView webView;
    private TextView tv_back;
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_web);
        webView=(WebView) findViewById(R.id.web);
        tv_back=(TextView) findViewById(R.id.back);
        tv_title=(TextView) findViewById(R.id.title);
        String url=getIntent().getStringExtra("url");
        String title=getIntent().getStringExtra("title");
        tv_title.setText(title);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //统计html页面时常事件
                //注意：需要配合该webview所在activity中调用YCPlatform.onPause(this)方法来调用
                YCPlatform.onPageStarted(url);
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        YCPlatform.onPause(this);
    }
}
