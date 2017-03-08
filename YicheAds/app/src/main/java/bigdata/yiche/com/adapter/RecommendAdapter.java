package bigdata.yiche.com.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yiche.bigdata.ad.YCAdPlatform;
import java.io.File;
import java.util.List;
import bigdata.yiche.com.R;
import bigdata.yiche.com.bean.TopicBean;
import bigdata.yiche.com.data.MyApplication;
import bigdata.yiche.com.net.VolleyError;
import bigdata.yiche.com.net.toolbox.ImageLoader;
import bigdata.yiche.com.utils.MyLogger;
import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by wanglirong on 16/10/11.
 */

public class RecommendAdapter extends BaseAdapter {
    private static MyLogger mLogger = MyLogger.getLogger(RecommendAdapter.class.getName());
    //ad
    public static final int VALUE_PIC_TEXT = 5;// 4种不同的布局
    public static final int VALUE_PIC_THREE = 6;
    public static final int VALUE_PIC_BIG = 7;
    public static final int VALUE_VEDIO = 8;
    //topic
    public static final int TOPIC_NORMAL = 0;
    public static final int TOPIC_SPECIAL = 1;
    public static final int TOPIC_NEWS = 2;
    public static final int TOPIC_BIG = 3;
    public static final int TOPIC_THREE = 4;
    private LayoutInflater mInflater;
    private Context context;
    private List<TopicBean> myList;

    public RecommendAdapter(Context context, List<TopicBean> myList) {
        this.myList = myList;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return myList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    ViewHolderVedio holderVedio = null;

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        final TopicBean adBean = myList.get(position);
        int type = getItemViewType(position);
        ViewHolderPT holderPT = null;
        ViewHolderThree holderthree = null;
        ViewHolderBig holderbig = null;
        ViewHolderTopicNormal viewHolderTopicNormal = null;
        ViewHolderTopicBigPic viewHolderTopicBigPic = null;
        ViewHolderTopicThreePic viewHolderTopicThreePic = null;
//        final MediaController mediaController=new MediaController(context);
        String[] picUrls = adBean.getPic_url();

        if (convertView == null) {
            switch (type) {
                case TOPIC_NORMAL:
                    viewHolderTopicNormal = new ViewHolderTopicNormal();
                    convertView = mInflater.inflate(R.layout.list_item_topic_text, null);
                    viewHolderTopicNormal.iv_img = (ImageView) convertView.findViewById(R.id.img_text);
                    viewHolderTopicNormal.iv_type = (Button) convertView.findViewById(R.id.btn_type);
                    viewHolderTopicNormal.tv_title = (TextView) convertView.findViewById(R.id.title_text);
                    viewHolderTopicNormal.tv_source = (TextView) convertView.findViewById(R.id.source_text);
                    viewHolderTopicNormal.tv_comments = (TextView) convertView.findViewById(R.id.comments);
                    viewHolderTopicNormal.ll_layout = (LinearLayout) convertView.findViewById(R.id.ll_layout);
                    viewHolderTopicNormal.tv_title.setText(adBean.getTitle());
                    viewHolderTopicNormal.tv_source.setText(adBean.getSource());
                    if (adBean.getSubType() == 1) {
                        viewHolderTopicNormal.iv_type.setVisibility(View.VISIBLE);
                        viewHolderTopicNormal.iv_type.setText("专题");
                        viewHolderTopicNormal.ll_layout.setVisibility(View.GONE);
                    } else if (adBean.getSubType() == 2) {
                        viewHolderTopicNormal.iv_type.setVisibility(View.VISIBLE);
                        viewHolderTopicNormal.iv_type.setText("新闻");
                        viewHolderTopicNormal.ll_layout.setVisibility(View.GONE);
                    } else {
                        viewHolderTopicNormal.iv_type.setVisibility(View.GONE);
                        viewHolderTopicNormal.ll_layout.setVisibility(View.VISIBLE);
                        viewHolderTopicNormal.tv_comments.setText(adBean.getComments() + "");
                    }
                    if (picUrls.length > 0) {
                        setImageView(picUrls[0], viewHolderTopicNormal.iv_img);
                    }
                    viewHolderTopicNormal.iv_img.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_pic)));
                    convertView.setTag(viewHolderTopicNormal);
                    break;
                case TOPIC_BIG:
                    viewHolderTopicBigPic = new ViewHolderTopicBigPic();
                    convertView = mInflater.inflate(R.layout.list_item_topic_big, null);
                    viewHolderTopicBigPic.iv_img = (ImageView) convertView.findViewById(R.id.img3);
                    viewHolderTopicBigPic.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
                    viewHolderTopicBigPic.tv_source = (TextView) convertView.findViewById(R.id.tv_source);
                    viewHolderTopicBigPic.tv_title = (TextView) convertView.findViewById(R.id.title3);
                    viewHolderTopicBigPic.tv_num.setText(adBean.getComments() + "图片");
                    viewHolderTopicBigPic.tv_source.setText(adBean.getSource());
                    viewHolderTopicBigPic.tv_title.setText(adBean.getTitle());
                    if (picUrls.length > 0) {
                        setImageView(picUrls[0], viewHolderTopicBigPic.iv_img);
                    }
                    viewHolderTopicBigPic.iv_img.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_big)));
                    convertView.setTag(viewHolderTopicBigPic);

                    break;
                case TOPIC_THREE:
                    viewHolderTopicThreePic = new ViewHolderTopicThreePic();
                    convertView = mInflater.inflate(R.layout.list_item_topic_three, null);
                    viewHolderTopicThreePic.iv_img1 = (ImageView) convertView.findViewById(R.id.img21);
                    viewHolderTopicThreePic.iv_img2 = (ImageView) convertView.findViewById(R.id.img22);
                    viewHolderTopicThreePic.iv_img3 = (ImageView) convertView.findViewById(R.id.img23);
                    viewHolderTopicThreePic.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
                    viewHolderTopicThreePic.tv_source = (TextView) convertView.findViewById(R.id.tv_source);
                    viewHolderTopicThreePic.tv_title = (TextView) convertView.findViewById(R.id.title2);
                    viewHolderTopicThreePic.tv_num.setText(adBean.getComments() + "图片");
                    viewHolderTopicThreePic.tv_source.setText(adBean.getSource());
                    viewHolderTopicThreePic.tv_title.setText(adBean.getTitle());
                    if (picUrls.length > 0) {
                        setImageView(picUrls[0], viewHolderTopicThreePic.iv_img1);
                    }
                    if (picUrls.length > 1) {
                        setImageView(picUrls[1], viewHolderTopicThreePic.iv_img2);
                    }
                    if (picUrls.length > 2) {
                        setImageView(picUrls[2], viewHolderTopicThreePic.iv_img3);
                    }
                    viewHolderTopicThreePic.iv_img1.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_pic)));
                    viewHolderTopicThreePic.iv_img2.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_pic)));
                    viewHolderTopicThreePic.iv_img3.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_pic)));
                    convertView.setTag(viewHolderTopicThreePic);
                    break;
                case VALUE_PIC_BIG:
                    holderbig = new ViewHolderBig();
                    convertView = mInflater.inflate(R.layout.list_item_ad_pic_big, null);
                    holderbig.iv = (ImageView) convertView.findViewById(R.id.img3);
                    holderbig.tv = (TextView) convertView.findViewById(R.id.title3);
                    holderbig.tv.setText(adBean.getAd_title());
                    if (picUrls.length > 0) {
                        setImageView(picUrls[0], holderbig.iv);
                    }
                    holderbig.iv.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_big)));
                    YCAdPlatform.sendExpose(adBean.getAd_resourceId(), convertView);
                    mLogger.d("发送曝光" + adBean.getType());
                    convertView.setTag(holderbig);
                    break;
                case VALUE_PIC_TEXT:
                    holderPT = new ViewHolderPT();
                    convertView = mInflater.inflate(R.layout.list_item_ad_pic_text, null);
                    holderPT.iv = (ImageView) convertView.findViewById(R.id.img1);
                    holderPT.tv = (TextView) convertView.findViewById(R.id.title1);
                    holderPT.tv.setText(adBean.getAd_title());
                    if (picUrls.length > 0) {
                        setImageView(picUrls[0], holderPT.iv);
                    }
                    holderPT.iv.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_pic)));
                    YCAdPlatform.sendExpose(adBean.getAd_resourceId(), convertView);
                    mLogger.d("发送曝光" + adBean.getType());
                    convertView.setTag(holderPT);
                    break;
                case VALUE_PIC_THREE:
                    holderthree = new ViewHolderThree();
                    convertView = mInflater.inflate(R.layout.list_item_ad_pic_three, null);
                    holderthree.iv1 = (ImageView) convertView
                            .findViewById(R.id.img21);
                    holderthree.iv2 = (ImageView) convertView
                            .findViewById(R.id.img22);
                    holderthree.iv3 = (ImageView) convertView
                            .findViewById(R.id.img23);
                    holderthree.tv = (TextView) convertView
                            .findViewById(R.id.title2);
                    holderthree.tv.setText(adBean.getAd_title());
                    if (picUrls.length > 0) {
                        setImageView(picUrls[0], holderthree.iv1);
                    }
                    if (picUrls.length > 1) {
                        setImageView(picUrls[1], holderthree.iv2);
                    }
                    if (picUrls.length > 2) {
                        setImageView(picUrls[2], holderthree.iv2);
                    }
                    YCAdPlatform.sendExpose(adBean.getAd_resourceId(), convertView);
                    mLogger.d("发送曝光" + adBean.getType());
                    convertView.setTag(holderthree);
                    break;

                case VALUE_VEDIO:
                    holderVedio = new ViewHolderVedio();
                    convertView = mInflater.inflate(R.layout.list_item_ad_vedio, null);
                    holderVedio.videoView = (JCVideoPlayer) convertView
                            .findViewById(R.id.video4);
                    holderVedio.tv = (TextView) convertView
                            .findViewById(R.id.title4);
                    holderVedio.iv = (ImageView) convertView
                            .findViewById(R.id.img4);
                    holderVedio.progressBar = (ProgressBar) convertView
                            .findViewById(R.id.progressBar1);
                    holderVedio.tv.setText(adBean.getAd_title());
                    if (picUrls.length > 0) {
                        setImageView(picUrls[0], holderVedio.iv);
                    }
                    YCAdPlatform.sendExpose(adBean.getAd_resourceId(), convertView);
                    mLogger.d("发送曝光" + adBean.getType());
                    holderVedio.progressBar.setVisibility(View.GONE);
                    holderVedio.videoView.setUp(adBean.getUrl(),picUrls[0],"");
//                    holderVedio.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        @Override
//                        public void onCompletion(MediaPlayer mp) {
//                            holderVedio.iv.setVisibility(View.VISIBLE);
//                            holderVedio.videoView.setVisibility(View.GONE);
//                        }
//                    });
//                    holderVedio.iv.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//
//                            YCAdPlatform.sendClick(adBean.getAd_resourceId(), v);
//                            holderVedio.iv.setVisibility(View.GONE);
//                            holderVedio.progressBar.setVisibility(View.VISIBLE);
//                            final String videoName = adBean.getAd_resourceId() + adBean.getUrl().substring(adBean.getUrl().lastIndexOf("."));
//                            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/ycdownload/" + videoName);
//                            if (!file.exists()) {
//                                HttpRequest.download(adBean.getUrl(), new File(
//                                                Environment.getExternalStorageDirectory()
//                                                        .getPath() + "/ycdownload/" + videoName),
//                                        new FileDownloadCallback() {
//                                            // 开始下载
//                                            @Override
//                                            public void onStart() {
//                                                super.onStart();
//                                            }
//
//                                            // 下载进度
//                                            @Override
//                                            public void onProgress(int progress,
//                                                                   long networkSpeed) {
//                                                super.onProgress(progress, networkSpeed);
//                                            }
//
//                                            // 下载失败
//                                            @Override
//                                            public void onFailure() {
//                                                super.onFailure();
//                                                Toast.makeText(context, "下载失败",
//                                                        Toast.LENGTH_SHORT).show();
//                                            }
//
//                                            // 下载完成（下载成功）
//                                            @Override
//                                            public void onDone() {
//                                                super.onDone();
//
//                                                holderVedio.progressBar.setVisibility(View.GONE);
//                                                holderVedio.videoView
//                                                        .setVisibility(View.VISIBLE);
////                                                holderVedio.videoView
////                                                        .setMediaController(mediaController);
//                                                Uri videoUri = Uri.parse(Environment
//                                                        .getExternalStorageDirectory()
//                                                        .getPath()
//                                                        + "/ycdownload/" + videoName);
//                                                holderVedio.videoView
//                                                        .setVideoURI(videoUri);
//                                                holderVedio.videoView.start();
//                                            }
//                                        });
//                            }else{
//                                holderVedio.progressBar.setVisibility(View.GONE);
//                                holderVedio.videoView
//                                        .setVisibility(View.VISIBLE);
//                                Uri videoUri = Uri.parse(Environment
//                                        .getExternalStorageDirectory()
//                                        .getPath()
//                                        + "/ycdownload/" + videoName);
//                                holderVedio.videoView
//                                        .setVideoURI(videoUri);
//                                holderVedio.videoView.start();
//
//                            }
//
//                        }
//                    });

                    convertView.setTag(holderVedio);
                    break;
                default:
                    break;
            }
        } else {
            Log.d("baseAdapter", "Adapter_:" + (convertView == null));
            switch (type) {
                case TOPIC_NORMAL:
                    viewHolderTopicNormal = (ViewHolderTopicNormal) convertView.getTag();
                    viewHolderTopicNormal.tv_title.setText(adBean.getTitle());
                    viewHolderTopicNormal.tv_source.setText(adBean.getSource());
                    if (adBean.getSubType() == 1) {
                        viewHolderTopicNormal.iv_type.setVisibility(View.VISIBLE);
                        viewHolderTopicNormal.iv_type.setText("专题");
                        viewHolderTopicNormal.ll_layout.setVisibility(View.GONE);
                    } else if (adBean.getSubType() == 2) {
                        viewHolderTopicNormal.iv_type.setVisibility(View.VISIBLE);
                        viewHolderTopicNormal.iv_type.setText("新闻");
                        viewHolderTopicNormal.ll_layout.setVisibility(View.GONE);
                    } else {
                        viewHolderTopicNormal.iv_type.setVisibility(View.GONE);
                        viewHolderTopicNormal.ll_layout.setVisibility(View.VISIBLE);
                        viewHolderTopicNormal.tv_comments.setText(adBean.getComments() + "");
                    }
                    if (picUrls.length > 0) {
                        setImageView(picUrls[0], viewHolderTopicNormal.iv_img);
                    }
                    break;
                case TOPIC_BIG:
                    viewHolderTopicBigPic = (ViewHolderTopicBigPic) convertView.getTag();
                    viewHolderTopicBigPic.tv_num.setText(adBean.getComments() + "图片");
                    viewHolderTopicBigPic.tv_source.setText(adBean.getSource());
                    viewHolderTopicBigPic.tv_title.setText(adBean.getTitle());
                    if (picUrls.length > 0) {
                        setImageView(picUrls[0], viewHolderTopicBigPic.iv_img);
                    }
                    break;
                case TOPIC_THREE:
                    viewHolderTopicThreePic = (ViewHolderTopicThreePic) convertView.getTag();
                    viewHolderTopicThreePic.tv_num.setText(adBean.getComments() + "图片");
                    viewHolderTopicThreePic.tv_source.setText(adBean.getSource());
                    viewHolderTopicThreePic.tv_title.setText(adBean.getTitle());
                    if (picUrls.length > 0) {
                        setImageView(picUrls[0], viewHolderTopicThreePic.iv_img1);
                    }
                    if (picUrls.length > 1) {
                        setImageView(picUrls[1], viewHolderTopicThreePic.iv_img2);
                    }
                    if (picUrls.length > 2) {
                        setImageView(picUrls[2], viewHolderTopicThreePic.iv_img3);
                    }
                    break;
                case VALUE_PIC_TEXT:
                    holderPT = (ViewHolderPT) convertView.getTag();
                    holderPT.tv.setText(adBean.getAd_title());
                    if (picUrls.length > 0) {
                        setImageView(picUrls[0], holderPT.iv);
                    }
                    YCAdPlatform.sendExpose(adBean.getAd_resourceId(), convertView);
                    mLogger.d("发送曝光" + adBean.getType());
                    break;
                case VALUE_PIC_THREE:
                    holderthree = (ViewHolderThree) convertView.getTag();
                    holderthree.tv.setText(adBean.getAd_title());
                    if (picUrls.length > 0) {
                        setImageView(picUrls[0], holderthree.iv1);
                    }
                    if (picUrls.length > 1) {
                        setImageView(picUrls[1], holderthree.iv2);
                    }
                    if (picUrls.length > 2) {
                        setImageView(picUrls[2], holderthree.iv3);
                    }
                    YCAdPlatform.sendExpose(adBean.getAd_resourceId(), convertView);
                    mLogger.d("发送曝光" + adBean.getType());
                    break;
                case VALUE_PIC_BIG:
                    holderbig = (ViewHolderBig) convertView.getTag();
                    holderbig.tv.setText(adBean.getAd_title());
                    if (picUrls.length > 0) {
                        setImageView(picUrls[0], holderbig.iv);
                    }
                    YCAdPlatform.sendExpose(adBean.getAd_resourceId(), convertView);
                    mLogger.d("发送曝光" + adBean.getType());
                    break;
                case VALUE_VEDIO:
                    holderVedio = (ViewHolderVedio) convertView.getTag();
                    holderVedio.tv.setText(adBean.getAd_title());
//                    holderVedio.progressBar.setVisibility(View.GONE);
//                    holderVedio.videoView.setVisibility(View.VISIBLE);
                    holderVedio.videoView.setUp(adBean.getUrl(), picUrls[0], "", new JCVideoPlayer.AdClick() {
                        @Override
                        public void onAdClick(View v) {
                            YCAdPlatform.sendClick(adBean.getAd_resourceId(), v);
                        }
                    });
//                    holderVedio.videoView.setMediaController(mediaController);
//                    if (picUrls.length > 0) {
//                        setImageView(picUrls[0], holderVedio.iv);
//                    }

//                    holderVedio.iv.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//
//                            YCAdPlatform.sendClick(adBean.getAd_resourceId(), v);
//                            holderVedio.iv.setVisibility(View.GONE);
//                            holderVedio.progressBar.setVisibility(View.VISIBLE);
//                            final String videoName = adBean.getAd_resourceId() + adBean.getUrl().substring(adBean.getUrl().lastIndexOf("."));
//                            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/ycdownload/" + videoName);
//                            if (!file.exists()) {
//                                HttpRequest.download(adBean.getUrl(), new File(
//                                                Environment.getExternalStorageDirectory()
//                                                        .getPath() + "/ycdownload/" + videoName),
//                                        new FileDownloadCallback() {
//                                            // 开始下载
//                                            @Override
//                                            public void onStart() {
//                                                super.onStart();
//                                            }
//
//                                            // 下载进度
//                                            @Override
//                                            public void onProgress(int progress,
//                                                                   long networkSpeed) {
//                                                super.onProgress(progress, networkSpeed);
//                                            }
//
//                                            // 下载失败
//                                            @Override
//                                            public void onFailure() {
//                                                super.onFailure();
//                                                Toast.makeText(context, "下载失败",
//                                                        Toast.LENGTH_SHORT).show();
//                                            }
//
//                                            // 下载完成（下载成功）
//                                            @Override
//                                            public void onDone() {
//                                                super.onDone();
//
//                                                holderVedio.progressBar.setVisibility(View.GONE);
//                                                holderVedio.videoView
//                                                        .setVisibility(View.VISIBLE);
//                                                holderVedio.videoView
//                                                        .setMediaController(new MediaController(
//                                                                context));
//                                                Uri videoUri = Uri.parse(Environment
//                                                        .getExternalStorageDirectory()
//                                                        .getPath()
//                                                        + "/ycdownload/" + videoName);
//                                                holderVedio.videoView
//                                                        .setVideoURI(videoUri);
//                                                holderVedio.videoView.start();
//                                            }
//                                        });
//                            }else{
//                                holderVedio.progressBar.setVisibility(View.GONE);
//                                holderVedio.videoView
//                                        .setVisibility(View.VISIBLE);
//                                Uri videoUri = Uri.parse(Environment
//                                        .getExternalStorageDirectory()
//                                        .getPath()
//                                        + "/ycdownload/" + videoName);
//                                holderVedio.videoView
//                                        .setVideoURI(videoUri);
//                                holderVedio.videoView.start();
//
//                            }
//
//                        }
//                    });
//                    holderVedio.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        @Override
//                        public void onCompletion(MediaPlayer mp) {
//                            holderVedio.iv.setVisibility(View.VISIBLE);
//                            holderVedio.videoView.setVisibility(View.GONE);
//                        }
//                    });
//                    holderVedio.videoView.start();
                    YCAdPlatform.sendExpose(adBean.getAd_resourceId(), convertView);
                    mLogger.d("发送曝光" + adBean.getType());
                    break;
                default:
                    break;
            }
        }
        return convertView;
    }

    private void setImageView(String picUrl, final ImageView iv_img) {
        MyApplication.getImageLoader().get(picUrl, new ImageLoader.ImageListener() {

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                iv_img.setBackgroundDrawable(new BitmapDrawable(context.getResources(), response.getBitmap()));
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

    }


    /**
     * 根据数据源的position返回需要显示的的layout的type
     * <p>
     * type的值必须从0开始
     */
    @Override
    public int getItemViewType(int position) {

        TopicBean msg = myList.get(position);
        int type = Integer.parseInt(msg.getType());
        Log.e("TYPE:", "" + type);
        return type;
    }

    /**
     * 返回所有的layout的数量
     */
    @Override
    public int getViewTypeCount() {
        return 9;
    }

    class ViewHolderTopicNormal {
        private ImageView iv_img;
        private Button iv_type;
        private TextView tv_title;
        private TextView tv_source;
        private TextView tv_comments;
        private LinearLayout ll_layout;

    }

    class ViewHolderTopicBigPic {
        private ImageView iv_img;
        private TextView tv_title;
        private TextView tv_source;
        private TextView tv_num;

    }

    class ViewHolderTopicThreePic {
        private ImageView iv_img1;
        private ImageView iv_img2;
        private ImageView iv_img3;
        private TextView tv_title;
        private TextView tv_source;
        private TextView tv_num;

    }

    class ViewHolderPT {
        private ImageView iv;
        private TextView tv;
    }

    class ViewHolderThree {
        private TextView tv;
        private ImageView iv1;//
        private ImageView iv2;//
        private ImageView iv3;//
    }


    class ViewHolderBig {
        private ImageView iv;
        private TextView tv;
    }

    class ViewHolderVedio {
        private TextView tv;
        private JCVideoPlayer videoView;
        private ImageView iv;
        private ProgressBar progressBar;
    }

    public void playVideo(Context context, Message msg,
                          ProgressBar mProgressbar, ImageView iv4, JCVideoPlayer videoView) {
        mProgressbar.setProgress(msg.getData().getInt("size"));

        float temp = (float) mProgressbar.getProgress()
                / (float) mProgressbar.getMax();

        int progress = (int) (temp * 100);
        if (progress == 100) {
            mProgressbar.setVisibility(View.GONE);
            iv4.setVisibility(View.GONE);
            Toast.makeText(context, "下载完成！", Toast.LENGTH_LONG).show();
            videoView.setVisibility(View.VISIBLE);
//            videoView.setMediaController(new MediaController(context));
            Uri videoUri = Uri.parse(Environment.getExternalStorageDirectory()
                    .getPath() + "/ycdownload/test.mp4");
//            videoView.setVideoURI(videoUri);
//            videoView.start();
        }

    }
}