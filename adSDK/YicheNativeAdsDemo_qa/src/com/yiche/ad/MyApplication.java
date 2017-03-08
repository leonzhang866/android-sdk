package com.yiche.ad;

import java.io.File;

import android.app.Application;
import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.yiche.ycanalytics.YCPlatform;
import com.yiche.ycanalytics.utils.Constants;

public class MyApplication extends Application {  
	  
    @Override  
    public void onCreate() {  
        super.onCreate(); 
        Constants.DEBUG=true;
		YCPlatform.init(MyApplication.this);
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());
        
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());  //缓存文件夹路径
    	ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
    	        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions 内存缓存文件的最大长宽
    	        .diskCacheExtraOptions(480, 800, null)  // 本地缓存的详细信息(缓存的最大长宽)，最好不要设置这个 
    	        .threadPoolSize(3) // default  线程池内加载的数量
    	        .threadPriority(Thread.NORM_PRIORITY - 2) // default 设置当前线程的优先级
    	        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
    	        .denyCacheImageMultipleSizesInMemory()
    	        .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //可以通过自己的内存缓存实现
    	        .memoryCacheSize(2 * 1024 * 1024)  // 内存缓存的最大值
    	        .memoryCacheSizePercentage(13) // default
    	        .diskCache(new UnlimitedDiskCache(cacheDir)) // default 可以自定义缓存路径  
    	        .diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
    	        .diskCacheFileCount(100)  // 可以缓存的文件数量 
    	        // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
    	        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) 
    	        .imageDownloader(new BaseImageDownloader(getApplicationContext())) // default
    	        .imageDecoder(new BaseImageDecoder(true)) // default
    	        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
    	        .writeDebugLogs() // 打印debug log
    	        .build(); //开始构建
//        //创建默认的ImageLoader配置参数  
//        ImageLoaderConfiguration configuration = ImageLoaderConfiguration  
//                .createDefault(this);  
          
        ImageLoader.getInstance().init(config) ;
    }  
  
}  
