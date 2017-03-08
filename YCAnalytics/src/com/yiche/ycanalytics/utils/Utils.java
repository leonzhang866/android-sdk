package com.yiche.ycanalytics.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.yiche.ycanalytics.YCPlatformInternal;
import com.yiche.ycanalytics.bean.EventBean;
import com.yiche.ycanalytics.db.DataManager;
import com.yiche.ycanalytics.json.JSONManager;
import com.yiche.ycanalytics.utils.Constants;

public class Utils{
	private static MyLogger mLogger = MyLogger.getLogger(Utils.class.getName());
	
	public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
	
	public static JSONArray getAllUserApp(Context context){
		JSONArray jarr=new JSONArray();
		PackageManager pm =context.getPackageManager();
		List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		for(ApplicationInfo info : applicationInfos){
			if(filterApp(info)){
				try {
					//获取应用的名称
					String app_name = info.loadLabel(pm).toString();
					//获取应用的包名
					String packageName = info.packageName;
					JSONObject jo=new JSONObject();
					jo.put("pkName", packageName);
					jo.put("appName", app_name);
					jarr.put(jo);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return jarr;
		
	}
	//判断应用程序是否是用户程序
    public static boolean filterApp(ApplicationInfo info) {
    	//原来是系统应用，用户手动升级
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
            //用户自己安装的应用程序
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }
	/**
	 * 获取内部存储路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getSavePath(Context context) {
		File savePath = context.getCacheDir();

		mLogger.d(savePath.toString());
		return savePath.toString();
	}

	/**
	 * 未引用方法
	 * 
	 * 跳转到新的Activity
	 * 
	 * @param sourceAcitivity
	 * @param desAcitvity
	 */
	public static void startNewActivity(Activity sourceAcitivity,
			Activity desAcitvity) {
		Intent intent = new Intent();
		intent.setClass(sourceAcitivity, desAcitvity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		sourceAcitivity.startActivity(intent);
	}

	/**
	 * 未引用方法
	 * 
	 * 跳转到新的Activity
	 * 
	 * @param sourceAcitivity
	 * @param desAcitvity
	 */
	public static void startNewActivity(Activity sourceAcitivity,
			Class<?> desClass) {
		Intent intent = new Intent();
		intent.setClass(sourceAcitivity, desClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		sourceAcitivity.startActivity(intent);
	}

	/**
	 * AES加密字符串
	 * 
	 * @param input
	 *            字符串
	 * @return 加密的字符串
	 */
	public static String encodeString(String input) {
		AES myaes = new AES();
		return myaes.aesEncrypt(input);
	}

	/**
	 * 解密字符串
	 * 
	 * @param input
	 *            字符串
	 * @return 解密后的字符串
	 */
	public static String decodeString(String input) {
		AES myaes = new AES();
		return myaes.aesDecrypt(new String(input));
	}

	/**
	 * 获取包名
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppPackage(Context context) {
		return context.getPackageName();
	}

	/**
	 * 获取当前应用的名称
	 * 
	 * @return
	 */
	public static String getAppName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo;
		String appName = "";
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			appName = (String) packInfo.applicationInfo
					.loadLabel(packageManager);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return appName;
	}

	/**
	 * 获取游戏的版本
	 * 
	 * @return
	 */
	public static String getGameVersion(Context context) {
		return PhoneHelper.getAppVersionName(context);
	}

	/**
	 * 获取版本号
	 * 
	 * @return
	 */
	public static String getGameVersionCode(Context context) {
		return PhoneHelper.getAppVersionCode(context);
	}

	public static String getChannel(Context context) {

		
		if(!Constants.CID.equalsIgnoreCase("unknow")){
			return Constants.CID;
		}
		
		String channelid = SharePreferenceUtil.getInstance(context).getString(SharePreferenceUtil.YC_APP_CHANNELID);
		if (channelid == null||channelid.equals("unknow")) {
			
			channelid = PhoneHelper.getChannelData(context,Constants.CHANNEL_NAME);
			if(!channelid.equals("-100")){
				SharePreferenceUtil.getInstance(context).saveString(SharePreferenceUtil.YC_APP_CHANNELID, channelid);
			}
			return channelid;
		}
		return channelid;
	}
	
	public static String getAppKey(Context context) {

		String appkey = SharePreferenceUtil.getInstance(context).getString(SharePreferenceUtil.YC_APP_KEY);

		if (appkey == null||appkey.equals("unknow")) {
			appkey = PhoneHelper.getChannelData(context,Constants.KEY_NAME);
			if(!appkey.equals("-100")){
				SharePreferenceUtil.getInstance(context).saveString(SharePreferenceUtil.YC_APP_KEY, appkey);;
			}
			return appkey;
		}
		return appkey;
	}
	public static void initParams(final Context context) {
		final String appkey = PhoneHelper.getChannelData(context, Constants.KEY_NAME);
		final String channelid = PhoneHelper.getChannelData(context, Constants.CHANNEL_NAME);
		
		if (!appkey.equals("-100")) {
			SharePreferenceUtil.getInstance(context).saveString(SharePreferenceUtil.YC_APP_KEY, appkey);
		}
		if (!channelid.equals("-100")) {
			SharePreferenceUtil.getInstance(context).saveString(SharePreferenceUtil.YC_APP_CHANNELID, channelid);
		}
		
		
	}
	
	/**
	 * 保持文件内容到sd
	 * 
	 * @param dir
	 * @param filename
	 * @param obj
	 */
	public synchronized static void savefile(String dir, String filename,
			Object obj) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File dirPath = new File(dir);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}

			// 打开文件
			File file = new File(dirPath, filename);

			FileOutputStream fos = null;
			ObjectOutputStream oos = null;
			try {
				if (!file.exists()) {
					file.createNewFile();
				}

				// 保存文件
				fos = new FileOutputStream(file);
				oos = new ObjectOutputStream(fos);
				oos.writeObject(obj);
				oos.close();
				fos.close();
			} catch (Exception e) {
				//e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						oos.close();
						fos.close();
						oos = null;
						fos = null;
					} catch (IOException e) {
						//e.printStackTrace();
					}
				}
			}

		}
	}

	/**
	 * 文件内容读取
	 * 
	 * @param dir
	 * @param filename
	 * @return
	 */
	public synchronized static Object readFile(String dir, String filename) {
		Object obj = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			File file = new File(dir + filename);
			if (file.exists()) {
				// 读取
				fis = new FileInputStream(file);
				ois = new ObjectInputStream(fis);
				obj = ois.readObject();

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
					ois = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return obj;
	}

	/**
	 * 保存下载图片到指定路径
	 * 
	 * @param dir
	 *            存储图片路径
	 * @param filename
	 *            文件名称
	 * @param data
	 *            图片数据
	 */
	public synchronized static void savaImage(String dir, String filename,
			byte[] data) {
		File dirPath = new File(dir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		// 打开文件
		File imgFile = new File(dirPath, filename);
		if (!imgFile.exists()) {
			FileOutputStream fos = null;
			try {
				imgFile.createNewFile();
				// 保存图片到sd卡
				fos = new FileOutputStream(imgFile);
				fos.write(data);
				fos.close();
				// 下边的保存方式只能保存在apk包内部
				// os = openFileOutput("1.png",
				// Context.MODE_WORLD_WRITEABLE);
				// os.write(ConstValue.imgByte);
				// os.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
						fos = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * filename 图片名称
	 * 
	 * @param dir
	 * @param filename
	 * @param opt
	 * @return
	 */
	public synchronized static Bitmap readImage(String dir, String filename,
			Options opt) {
		if (filename == null || filename.trim().length() <= 0) {
			return null;
		}
		InputStream in = null;
		Bitmap bitmap = null;
		File file = null;
		try {
			file = new File(dir, filename);
			if (!file.exists()) {
				return null;
			}
			in = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(in, null, opt);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	/**
	 * 关闭软件键盘
	 * 
	 * @param context
	 */
	public static void hideSoftInputFromWindow(Activity context) {
		if (null != context.getCurrentFocus()) {
			((InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(context.getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * Encode uid to protect it from the third party
	 * 
	 * @param uid
	 * @return sid
	 */
	public static int uidToSid(int uid) {
		int sid = 0;
		sid = (uid & 0x0000ff00) << 16;
		sid += ((uid & 0xff000000) >> 8) & 0x00ff0000;
		sid += (uid & 0x000000ff) << 8;
		sid += (uid & 0x00ff0000) >> 16;
		sid ^= 282335; // 该值定了就不能再改了，否则就出问题了
		return sid;
	}

	/**
	 * Decode uid from sid
	 * 
	 * @param sid
	 * @return uid
	 */
	public static int sidToUid(int sid) {
		int uid;
		sid ^= 282335; // 该值定了就不能再改了，否则就出问题了
		uid = (sid & 0x00ff0000) << 8;
		uid += (sid & 0x000000ff) << 16;
		uid += ((sid & 0xff000000) >> 16) & 0x0000ff00;
		uid += (sid & 0x0000ff00) >> 8;
		return uid;
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			// Log.e("WifiPreference IpAddress", ex.toString());
		}
		// 获取不到ip时传入空串
		return "";
	}

	/**
	 * 判断App是否在前台运行
	 * 
	 * @return
	 */
	public static boolean isAppOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = context.getPackageName();
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	public static String getDisplaySize(String sizelong) {
		String result = sizelong;
		try {
			long size_byte = Long.parseLong(sizelong);
			if (size_byte < 1024) {
				result = sizelong + "B";
			} else if (size_byte < 1024 * 1024) {
				result = ((int) ((size_byte / 1024.0f) * 100)) / 100.0f + "KB";
			} else if (size_byte < 1024 * 1024 * 1024) {
				result = ((int) ((size_byte / (1024 * 1024.0f)) * 100))
						/ 100.0f + "MB";
			} else if (size_byte < 1024 * 1024 * 1024 * 1024) {
				result = ((int) ((size_byte / (1024 * 1024 * 1024.0f)) * 100))
						/ 100.0f + "GB";
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static String getResolution(Context context) {
		WindowManager windowManager = (WindowManager)context.getSystemService( Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();
		return screenHeight + "*" + screenWidth;
	}

	public static String getOperator(Context context){
		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		String operator = telManager.getSimOperator();

		if (operator != null) {

			if (operator.equals("46000") || operator.equals("46002")
					|| operator.equals("46007")) {

				// 中国移动
				return "中国移动";
			} else if (operator.equals("46001")) {

				// 中国联通
				return "中国联通";
			} else if (operator.equals("46003")) {

				// 中国电信
				return "中国电信";
			}
		}
		return "unknown";

    }
	public static String getPackageName(Context context){
		return context.getPackageName();
	}
	
	public static String getDataFromMillis(Long time){
		DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(time);
	}
	
	public static void creatData(final Finish Finish) {
		
		new Thread(){
			public void run() {
				String contentData = JSONManager.getJsonBuilder().buildStatisticsContentJsonObject("auto_create").toString();
				for(int i=0;i<500;i++){
					DataManager.getUserDbHandler().addoneEvents(new EventBean(contentData, Constants.NATIVEVIEW, System.currentTimeMillis()));
				}
				Finish.finish();
			};
		}.start();
		
		
	}
	public interface Finish {
		void finish();
	}
	/**
     * 是否存在debug_yc文件
     * 
     * @return
     */
    public static boolean isShowLogcat()
    {
        File file = new File(getLocalFile() + "/debug_yc");
        return file.exists();
    }

    public static String getLocalFile()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
	
}
