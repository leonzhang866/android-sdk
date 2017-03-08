package com.yiche.bigdata.ad.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class Utils{
	private static MyLogger mLogger = MyLogger.getLogger(Utils.class.getName());

	public static String getToken(String pubid,String dvid,long ts ,String key){
		String[] str = { pubid, dvid, String.valueOf(ts),key }; 
		Arrays.sort(str); // 按照字典序排序
		String bigStr = str[0] + str[1] + str[2]+str[3];
		return SHA1(bigStr);
	}
	
	public static String SHA1(String decript) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/*** 
     * MD5加码 生成32位md5码 
     */  
    public static String string2MD5(String inStr){  
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");  
        }catch (Exception e){  
            System.out.println(e.toString());  
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = inStr.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
  
    }  

	/**
	 * 判断是否为平板
	 * 
	 * @return
	 */
	public static int isPad(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
		// 屏幕尺寸
		double screenInches = Math.sqrt(x + y);
		// 大于7尺寸则为Pad
		if (screenInches >= 7.0) {
			return 2;
		}else{
			return 1;
		}
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
	 * 获取app的版本
	 * 
	 * @return
	 */
	public static String getAppVersion(Context appcontext) {
		return PhoneHelper.getAppVersionName(appcontext);
	}

	/**
	 * 获取版本号
	 * 
	 * @return
	 */
	public static String getGameVersionCode(Context appcontext) {
		return PhoneHelper.getAppVersionCode(appcontext);
	}




	



	/**
	 * 保持文件内容到sd
	 * 
	 * @param dir
	 * @param filename
	 * @param obj
	 */
	public synchronized static void savefile(String dir, String filename,
			String obj) {
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
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						oos.close();
						fos.close();
						oos = null;
						fos = null;
					} catch (IOException e) {
						e.printStackTrace();
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
	 * @param subJson 需要插入的json
	 * @param flag 当false时清空文件
	 */
	public synchronized static void updateFile(JSONObject subJson,boolean flag){
		if(flag){//新增
			String json=readFile(Constants.mLogInfoDir, Constants.mLogInfoPath).toString();
			savefile(Constants.mLogInfoDir, Constants.mLogInfoPath, addJson(json, subJson));
		}else{//删除
			savefile(Constants.mLogInfoDir, Constants.mLogInfoPath, "");
		}
	}
	
	public static String addJson(String json, JSONObject subjson) {
		JSONArray jsonObject = null;
		try {
			if (!TextUtils.isEmpty(json)) {
				 jsonObject = new JSONArray(json);
			} else {
				 jsonObject = new JSONArray();
			}
			jsonObject.put(subjson);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
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
	public static String getPackageName(Activity activity){
		return activity.getPackageName();
	}
	
	@SuppressLint("SimpleDateFormat") 
	public static String getDataFromMillis(Long time){
		DateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
		return formatter.format(time);
	}


	public static String getMac(Context context) {

		String macSerial = "";
		String str = "";

		try {
			Process pp = Runtime.getRuntime().exec(
					"cat /sys/class/net/wlan0/address ");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
			macSerial="";
		}
		return macSerial;

	}

	public static String getRes(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		return String.valueOf(height)+"*"+String.valueOf(width);
	}

	public static boolean isViewCovered(final View view) {  
	    View currentView = view;  
	  
	    Rect currentViewRect = new Rect();  
	    boolean partVisible = currentView.getGlobalVisibleRect(currentViewRect);  
//	    boolean totalHeightVisible = (currentViewRect.bottom - currentViewRect.top) >= view.getMeasuredHeight();  
//	    boolean totalWidthVisible = (currentViewRect.right - currentViewRect.left) >= view.getMeasuredWidth();  
	    boolean totalViewVisible = partVisible ;  
	    if (!totalViewVisible)//if any part of the view is clipped by any of its parents,return true  
	        return true;  
	  
//	    while (currentView.getParent() instanceof ViewGroup) {  
//	        ViewGroup currentParent = (ViewGroup) currentView.getParent();  
//	        if (currentParent.getVisibility() != View.VISIBLE)//if the parent of view is not visible,return true  
//	            return true;  
//	  
//	        int start = indexOfViewInParent(currentView, currentParent);  
//	        for (int i = start + 1; i < currentParent.getChildCount(); i++) {  
//	            Rect viewRect = new Rect();  
//	            view.getGlobalVisibleRect(viewRect);  
//	            View otherView = currentParent.getChildAt(i);  
//	            Rect otherViewRect = new Rect();  
//	            otherView.getGlobalVisibleRect(otherViewRect);  
//	            if (Rect.intersects(viewRect, otherViewRect))//if view intersects its older brother(covered),return true  
//	                return true;  
//	        }  
//	        currentView = currentParent;  
//	    }  
	    return false;  
	}  
	private static int indexOfViewInParent(View view, ViewGroup parent) {  
	    int index;  
	    for (index = 0; index < parent.getChildCount(); index++) {  
	        if (parent.getChildAt(index) == view)  
	            break;  
	    }  
	    return index;  
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
