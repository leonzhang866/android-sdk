package bigdata.yiche.com.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.yiche.ycanalytics.YCPlatform;
import java.util.Timer;
import java.util.TimerTask;
import bigdata.yiche.com.R;

public class MainActivity extends FragmentActivity {
    protected static final String TAG = "MainActivity";
    private Fragment[] fragments;
    private Fragment_one onefragment;
    private Fragment_two twofragment;
    private Fragment_three threefragment;
    private Fragment_four fourfragment;
    private ImageView[] imagebuttons;
    private TextView[] textviews;
    private int[] images;
    private int[] images_up;
    private int index;
    private int currentTabIndex;// 当前fragment的index

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_main);
        initTabView();
    }

    private void initTabView() {
        onefragment = new Fragment_one();
        twofragment = new Fragment_two();
        threefragment = new Fragment_three();
        fourfragment = new Fragment_four();
        fragments = new Fragment[] {onefragment, twofragment,
                threefragment, fourfragment};
        imagebuttons = new ImageView[4];
        imagebuttons[0] = (ImageView) findViewById(R.id.ib_weixin);
        imagebuttons[1] = (ImageView) findViewById(R.id.ib_contact_list);
        imagebuttons[2] = (ImageView) findViewById(R.id.ib_find);
        imagebuttons[3] = (ImageView) findViewById(R.id.ib_profile);

        imagebuttons[0].setSelected(true);

        textviews = new TextView[4];
        textviews[0] = (TextView) findViewById(R.id.tv_weixin);
        textviews[1] = (TextView) findViewById(R.id.tv_contact_list);
        textviews[2] = (TextView) findViewById(R.id.tv_find);
        textviews[3] = (TextView) findViewById(R.id.tv_profile);
        textviews[0].setTextColor(0xFF1e67e0);

        images=new int[4];
        images[0]=R.mipmap.tab_user_down;
        images[1]=R.mipmap.tab_send_down;
        images[2]=R.mipmap.tab_styles_down;
        images[3]=R.mipmap.tab_more_down;
        images_up=new int[4];
        images_up[0]=R.mipmap.tab_user_up;
        images_up[1]=R.mipmap.tab_send_up;
        images_up[2]=R.mipmap.tab_styles_up;
        images_up[3]=R.mipmap.tab_more_up;
        imagebuttons[0].setImageResource(images[0]);
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, onefragment)
//                .attach(twofragment)
//                .attach(threefragment)
//                .attach(fourfragment)
//                .hide(twofragment).hide(fourfragment).hide(threefragment)
//                .show(onefragment)
                .commit();
//        getSupportFragmentManager().beginTransaction().show(onefragment);
    }

    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.tab_one:
                index = 0;
                break;
            case R.id.tab_two:
                index = 1;
                break;
            case R.id.tab_three:
                index = 2;
                break;
            case R.id.tab_four:
                index = 3;
                break;
        }

        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            fragments[currentTabIndex].onPause();
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]).show(fragments[index]);
            }else{
                trx.show(fragments[index]);

            }
            trx.commit();

//}
        }else{
            return;
        }
        imagebuttons[currentTabIndex].setSelected(false);
        imagebuttons[index].setImageResource(images[index]);
        imagebuttons[currentTabIndex].setImageResource(images_up[currentTabIndex]);
        // 把当前tab设为选中状态
        imagebuttons[index].setSelected(true);
        textviews[index].setTextColor(0xFF1e67e0);
        textviews[currentTabIndex].setTextColor(0xFF9A9A9A);
        currentTabIndex = index;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private int keyBackClickCount = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (keyBackClickCount++) {
                case 0:
                    Toast.makeText(this, "再次按返回键退出", Toast.LENGTH_SHORT).show();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            keyBackClickCount = 0;
                        }
                    }, 3000);
                    break;
                case 1:
                    YCPlatform.onAppExit(this);
                    finish();
                    System.exit(0);
                    break;

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
