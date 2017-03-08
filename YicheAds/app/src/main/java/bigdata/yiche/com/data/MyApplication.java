package bigdata.yiche.com.data;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.util.LruCache;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.yiche.ycanalytics.YCPlatform;

import java.io.File;

import bigdata.yiche.com.net.manager.RequestManager;
import bigdata.yiche.com.net.toolbox.ImageLoader;
import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import cn.finalteam.toolsfinal.StorageUtils;

/**
 * Created by yiche on 16/10/10.
 */
public class MyApplication extends Application{

    private static ImageLoader sImageLoader = null;

    private final NetworkImageCache imageCacheMap = new NetworkImageCache();

    public static ImageLoader getImageLoader() {
        return sImageLoader;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        YCPlatform.init(MyApplication.this);
        RequestManager.getInstance().init(MyApplication.this);
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());  //缓存文件夹路径
        sImageLoader = new ImageLoader(RequestManager.getInstance()
                .getRequestQueue(), imageCacheMap);
        initUniversalImageLoader();
    }

    /**
     * @description NetworkImageCache
     */
    public static class NetworkImageCache extends LruCache<String, Bitmap>
            implements ImageLoader.ImageCache {

        public NetworkImageCache() {
            this(getDefaultLruCacheSize());
        }

        public NetworkImageCache(int sizeInKiloBytes) {
            super(sizeInKiloBytes);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight() / 1024;
        }

        @Override
        public Bitmap getBitmap(String url) {
            return get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            put(url, bitmap);
        }

        public static int getDefaultLruCacheSize() {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            return cacheSize;
        }
    }
    private void initUniversalImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new ColorDrawable(Color.parseColor("#f0f0f0")))
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        int memCacheSize = 1024 * 1024 * memClass / 8;

        File cacheDir = new File(Environment.getExternalStorageDirectory().getPath() + "/jiecao/cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(new UsingFreqLimitedMemoryCache(memCacheSize))
                .memoryCacheSize(memCacheSize)
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
                .defaultDisplayImageOptions(options)
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }
}
