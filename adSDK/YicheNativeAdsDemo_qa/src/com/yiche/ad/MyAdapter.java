package com.yiche.ad;

import java.io.File;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import com.example.ycadsdk_demo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yiche.bigdata.ad.bean.AdBean;

public class MyAdapter extends BaseAdapter {

	public static final String KEY = "key";
	public static final String VALUE = "value";
	public static final int VALUE_pic_text = 0;// 4种不同的布局
	public static final int VALUE_pic_three = 1;
	public static final int VALUE_pic_big = 2;
	public static final int VALUE_vedio = 3;
	private LayoutInflater mInflater;
	private Context context;
	ImageLoader imageLoader;
	private List<AdBean> myList;
	DisplayImageOptions options;

	public MyAdapter(Context context, List<AdBean> myList) {
		this.myList = myList;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		// .showImageOnLoading(R.drawable.ic_stub) // 设置图片下载期间显示的图片
		// .showImageForEmptyUri(R.drawable.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
		// .showImageOnFail(R.drawable.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(false) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build(); // 构建完成
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

		final com.yiche.bigdata.ad.bean.AdBean adBean = myList.get(position);
		int type = getItemViewType(position);
		ViewHolderPT holderPT = null;
		ViewHolderThree holderthree = null;
		ViewHolderBig holderbig = null;

		String[] urls = adBean.getPicUrls();
		if (convertView == null) {
			switch (type) {
			case VALUE_pic_big:
				holderbig = new ViewHolderBig();
				convertView = mInflater.inflate(R.layout.list_item_3, null);
				holderbig.iv = (ImageView) convertView.findViewById(R.id.img3);
				holderbig.tv = (TextView) convertView.findViewById(R.id.title3);
				imageLoader.displayImage(urls[0], holderbig.iv, options);
				holderbig.tv.setText(getTitle(adBean));
				convertView.setTag(holderbig);
				break;
			case VALUE_pic_text:
				holderPT = new ViewHolderPT();
				convertView = mInflater.inflate(R.layout.list_item_1, null);
				holderPT.iv = (ImageView) convertView.findViewById(R.id.img1);
				holderPT.tv = (TextView) convertView.findViewById(R.id.title1);
				imageLoader.displayImage(urls[0], holderPT.iv, options);
				holderPT.tv.setText(getTitle(adBean));
				convertView.setTag(holderPT);
				break;
			case VALUE_pic_three:
				holderthree = new ViewHolderThree();
				convertView = mInflater.inflate(R.layout.list_item_2, null);
				holderthree.iv1 = (ImageView) convertView
						.findViewById(R.id.img21);
				holderthree.iv2 = (ImageView) convertView
						.findViewById(R.id.img22);
				holderthree.iv3 = (ImageView) convertView
						.findViewById(R.id.img23);
				holderthree.tv = (TextView) convertView
						.findViewById(R.id.title2);
				imageLoader.displayImage(urls[0], holderthree.iv1, options);
				imageLoader.displayImage(urls[1], holderthree.iv2, options);
				imageLoader.displayImage(urls[2], holderthree.iv3, options);
				holderthree.tv.setText(getTitle(adBean));
				convertView.setTag(holderthree);
				break;

			case VALUE_vedio:
				holderVedio = new ViewHolderVedio();
				convertView = mInflater.inflate(R.layout.list_item_4, null);
				holderVedio.videoView = (VideoView) convertView
						.findViewById(R.id.video4);
				holderVedio.tv = (TextView) convertView
						.findViewById(R.id.title4);
				holderVedio.iv = (ImageView) convertView
						.findViewById(R.id.img4);
				holderVedio.progressBar = (ProgressBar) convertView
						.findViewById(R.id.progressBar1);
				imageLoader.displayImage(urls[0], holderVedio.iv, options);
				holderVedio.tv.setText(getTitle(adBean));

				holderVedio.iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						holderVedio.progressBar.setVisibility(View.VISIBLE);
						String subName=adBean.getUrl().substring(adBean.getUrl().lastIndexOf(",")+1);
						HttpRequest.download(adBean.getUrl(), new File(
								Environment.getExternalStorageDirectory()
										.getPath() + "/ycdownload/test.mp4"),
								new FileDownloadCallback() {
									// 开始下载
									@Override
									public void onStart() {
										super.onStart();
									}

									// 下载进度
									@Override
									public void onProgress(int progress,
											long networkSpeed) {
										super.onProgress(progress, networkSpeed);
										holderVedio.progressBar
												.setProgress(progress);
									}

									// 下载失败
									@Override
									public void onFailure() {
										super.onFailure();
										Toast.makeText(context, "下载失败",
												Toast.LENGTH_SHORT).show();
									}

									// 下载完成（下载成功）
									@Override
									public void onDone() {
										super.onDone();
										holderVedio.iv.setVisibility(View.GONE);
										Toast.makeText(context, "下载成功",
												Toast.LENGTH_SHORT).show();
										holderVedio.videoView
												.setVisibility(View.VISIBLE);
										holderVedio.videoView
												.setMediaController(new MediaController(
														context));
										Uri videoUri = Uri.parse(Environment
												.getExternalStorageDirectory()
												.getPath()
												+ "/ycdownload/test.mp4");
										holderVedio.videoView
												.setVideoURI(videoUri);
										holderVedio.videoView.start();
									}
								});
					}
				});
				convertView.setTag(holderVedio);
				break;
			default:
				break;
			}
		} else {
			Log.d("baseAdapter", "Adapter_:" + (convertView == null));
			switch (type) {
			case VALUE_pic_text:
				holderPT = (ViewHolderPT) convertView.getTag();
				imageLoader.displayImage(urls[0], holderPT.iv, options);
				holderPT.tv.setText(getTitle(adBean));
				break;
			case VALUE_pic_three:
				holderthree = (ViewHolderThree) convertView.getTag();
				imageLoader.displayImage(urls[0], holderthree.iv1, options);
				imageLoader.displayImage(urls[1], holderthree.iv2, options);
				imageLoader.displayImage(urls[2], holderthree.iv3, options);
				holderthree.tv.setText(getTitle(adBean));
				break;
			case VALUE_pic_big:
				holderbig = (ViewHolderBig) convertView.getTag();
				imageLoader.displayImage(urls[0], holderbig.iv, options);
				holderbig.tv.setText(getTitle(adBean));
				break;
			case VALUE_vedio:
				holderVedio = (ViewHolderVedio) convertView.getTag();
				imageLoader.displayImage(urls[0], holderVedio.iv, options);
				holderVedio.tv.setText(getTitle(adBean));
				holderVedio.progressBar.setVisibility(View.GONE);
				holderVedio.videoView.setVisibility(View.VISIBLE);
				holderVedio.videoView.setMediaController(new MediaController(
						context));
				Uri videoUri = Uri.parse(Environment
						.getExternalStorageDirectory().getPath()
						+ "/ycdownload/test.mp4");
				holderVedio.videoView.setVideoURI(videoUri);
				holderVedio.videoView.start();
				
				break;
			default:
				break;
			}

			// holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	private String getTitle(AdBean adBean) {

		return adBean.getTitle() + "/CreativeId:" + adBean.getCreativeId()
				+ "/pid:" + adBean.getPid();
	}

	/**
	 * 根据数据源的position返回需要显示的的layout的type
	 * 
	 * type的值必须从0开始
	 * 
	 * */
	@Override
	public int getItemViewType(int position) {

		AdBean msg = myList.get(position);
		int type = msg.getType();
		Log.e("TYPE:", "" + type);
		return type;
	}

	/**
	 * 返回所有的layout的数量
	 * 
	 * */
	@Override
	public int getViewTypeCount() {
		return 4;
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
		private VideoView videoView;
		private ImageView iv;
		private ProgressBar progressBar;
	}

	public void playVideo(Context context, Message msg,
			ProgressBar mProgressbar, ImageView iv4, VideoView videoView) {
		mProgressbar.setProgress(msg.getData().getInt("size"));

		float temp = (float) mProgressbar.getProgress()
				/ (float) mProgressbar.getMax();

		int progress = (int) (temp * 100);
		if (progress == 100) {
			mProgressbar.setVisibility(View.GONE);
			iv4.setVisibility(View.GONE);
			Toast.makeText(context, "下载完成！", Toast.LENGTH_LONG).show();
			videoView.setVisibility(View.VISIBLE);
			videoView.setMediaController(new MediaController(context));
			Uri videoUri = Uri.parse(Environment.getExternalStorageDirectory()
					.getPath() + "/ycdownload/test.mp4");
			videoView.setVideoURI(videoUri);
			videoView.start();
		}

	}

}
