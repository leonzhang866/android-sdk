package bigdata.yiche.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import bigdata.yiche.com.R;
import bigdata.yiche.com.view.MyScrollView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by yiche on 16/10/8.
 */
public class Fragment_three extends Fragment {
    private LinearLayout ll_1, ll_2, ll_3, ll_4;
    private ImageView imageView;
    private JCVideoPlayer videoView;
    private MyScrollView mScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View contentView = inflater.inflate(R.layout.fragment_three, null);
        ll_1=(LinearLayout) contentView.findViewById(R.id.ll_1);
        ll_2=(LinearLayout) contentView.findViewById(R.id.ll_2);
        ll_3=(LinearLayout) contentView.findViewById(R.id.ll_3);
        ll_4=(LinearLayout) contentView.findViewById(R.id.ll_4);
        imageView=(ImageView) contentView.findViewById(R.id.img4);
        videoView=(JCVideoPlayer) contentView.findViewById(R.id.video4);
        mScrollView=(MyScrollView) contentView.findViewById(R.id.scrollview);
        mScrollView.scrollTo(0,0);
        disableAutoScrollToBottom();
        ll_1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                jumptoWebActivity("东风风行S500 购车享优惠补贴千元好礼","http://c.ctags.cn/sy6/cu1/pc2/mt812?http://mai.m.yiche.com/pb/251.html?rfpa_tracker=17_812");
            }
        });
        ll_2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                jumptoWebActivity("Cross Polo 购车享千元礼包","http://c.ctags.cn/sy6/cu1/pc2/mt812?http://mai.m.yiche.com/pb/193.html?rfpa_tracker=17_812");
            }
        });
        ll_3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                jumptoWebActivity("长安CS75 购车即享千元超值补贴","http://c.ctags.cn/sy6/cu1/pc2/mt812?http://mai.m.yiche.com/pb/430.html?rfpa_tracker=17_812");
            }
        });
        imageView.setVisibility(View.GONE);
        videoView.setUp("android.resource://" + getActivity().getPackageName() + "/" +R.raw.test, "android.resource://" + getActivity().getPackageName() + "/" +R.drawable.video_pic,"",R.drawable.video_pic);

//        imageView.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                MediaController mc = new MediaController(getActivity());
//                imageView.setVisibility(View.GONE);
//                videoView.setUp("android.resource://" + getActivity().getPackageName() + "/" +R.raw.test, "android.resource://" + getActivity().getPackageName() + "/" +R.raw.video_pic,"",R.drawable.video_pic);
//            }
//        });

        return contentView;
    }


    private void jumptoWebActivity(String title,String url){
        Intent it =new Intent(getActivity(),WebActivity.class);
        it.putExtra("url",url);
        it.putExtra("title",title);
        startActivity(it);
    }
    private void disableAutoScrollToBottom() {
        mScrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        mScrollView.setFocusable(true);
        mScrollView.setFocusableInTouchMode(true);
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}