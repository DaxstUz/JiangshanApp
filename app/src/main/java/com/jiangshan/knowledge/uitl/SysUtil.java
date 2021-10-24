package com.jiangshan.knowledge.uitl;

import java.io.File;
import java.util.Locale;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.SignalStrength;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiangshan.knowledge.application.AppApplication;


/**
 * app系统工具类,如获取屏幕宽高，获取剩余内存
 * 
 * @author xc.li
 * @date 2015年7月14日
 */
public class SysUtil {

	/**
	 * 获取手机屏幕的宽和高
	 * 
	 * @return {w,h}
	 */
	public static int[] getScreenDispaly() {
		WindowManager windowManager = (WindowManager) AppApplication
				.getApplication().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;// 手机屏幕的宽度
		int height = dm.heightPixels;// 手机屏幕的高度
		int result[] = { width, height };
		return result;
	}

	/**
	 * 获取剩余内存
	 */
	public static long getFreeMemory() {
		ActivityManager am = (ActivityManager) AppApplication.getApplication()
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		return mi.availMem;
	}


	/**
	 * 获取2/3/4g信号强度
	 */
//	public static void getXgStrongth() {
//		NetReceiver.NetState state = NetReceiver.isConnected(AppApplication.getApplication());
//		getXgSignalLevel(null, null, state);
//		/*final TelephonyManager telPhoneMag = (TelephonyManager) BaseApplication
//				.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
//		telPhoneMag.listen(new PhoneStateListener() {
//
//			@Override
//			public void onSignalStrengthsChanged(SignalStrength signalStrength) {
//				super.onSignalStrengthsChanged(signalStrength);
//				String operator = telPhoneMag.getNetworkOperator();
//				NetState state = NetReceiver.isConnected(BaseApplication.getApplication());
//				if (signalStrength.isGsm() ||
//						(!state.equals(NetReceiver.NetState.NET_NO) &&
//						!state.equals(NetReceiver.NetState.NET_UNKNOWN)) ) {// 是否Xg信号
//					getXgSignalLevel(operator, signalStrength, state);
//				} else {
//					NetInfoCache.setNetInfo(NetInfoCache.NET_4G, 0);
//					NetInfoCache.setNetInfo(NetInfoCache.NET_3G, 0);
//					NetInfoCache.setNetInfo(NetInfoCache.NET_2G, 0);
//				}
//			}
//
//		}, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);*/
//	}

//	private static int getXgSignalLevel(String operator,
//			SignalStrength signalStrength, NetReceiver.NetState state) {
//		int signalLevel = 0;
//		//int strong = 0;
//		String netXg = "3g";
//		if (NetReceiver.NetState.NET_WIFI.equals(state)) {
//			signalLevel = 5;
//			netXg = NetInfoCache.NET_WIFI;
//		}else if (NetReceiver.NetState.NET_4G.equals(state)) {
//			/*strong = signalStrength.getCdmaDbm();
//			if (strong >= -65) {
//				signalLevel = 5;
//			} else if (strong >= -75) {
//				signalLevel = 4;
//			} else if (strong >= -85) {
//				signalLevel = 3;
//			} else if (strong >= -95) {
//				signalLevel = 2;
//			} else if (strong >= -105) {
//				signalLevel = 1;
//			} else {
//				signalLevel = 0;
//			}*/
//			signalLevel = 5;
//			netXg = NetInfoCache.NET_4G;
//		} else if (NetReceiver.NetState.NET_3G.equals(state)) {
//			/*if ("46003".equals(operator)) {// 电信
//				strong = signalStrength.getEvdoDbm();
//				if (strong >= -65) {
//					signalLevel = 5;
//				} else if (strong >= -75) {
//					signalLevel = 4;
//				} else if (strong >= -85) {
//					signalLevel = 3;
//				} else if (strong >= -95) {
//					signalLevel = 2;
//				} else if (strong >= -105) {
//					signalLevel = 1;
//				} else {
//					signalLevel = 0;
//				}
//			} else if ("46001".equals(operator)) {// 联通
//				strong = signalStrength.getCdmaDbm();
//				if (strong >= -75) {
//					signalLevel = 5;
//				} else if (strong >= -80) {
//					signalLevel = 4;
//				} else if (strong >= -85) {
//					signalLevel = 3;
//				} else if (strong >= -95) {
//					signalLevel = 2;
//				} else if (strong >= -100) {
//					signalLevel = 1;
//				} else {
//					signalLevel = 0;
//				}
//			} else if ("46000".equals(operator)) {// 移动
//				strong = signalStrength.getGsmSignalStrength();
//				if (strong >= 12) {
//					signalLevel = 5;
//				} else if (strong >= 10) {
//					signalLevel = 4;
//				} else if (strong >= 8) {
//					signalLevel = 3;
//				} else if (strong >= 5) {
//					signalLevel = 2;
//				} else if (strong >= 20) {
//					signalLevel = 1;
//				} else {
//					signalLevel = 0;
//				}
//			}*/
//			signalLevel = 5;
//			netXg = NetInfoCache.NET_3G;
//		} else if (NetReceiver.NetState.NET_2G.equals(state)) {
//			signalLevel = 5;
//			netXg = NetInfoCache.NET_2G;
//		} else {
//			NetInfoCache.setNetInfo(NetInfoCache.NET_WIFI, 0);
//			NetInfoCache.setNetInfo(NetInfoCache.NET_4G, 0);
//			NetInfoCache.setNetInfo(NetInfoCache.NET_2G, 0);
//		}
//		NetInfoCache.setNetInfo(netXg, signalLevel);
//		return signalLevel;
//	}

	/**
	 * 获取存储卡路径
	 * 
	 * @return
	 */
	public static String getSdCardPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取根目录
		} else {// 否则获取手机根目录
			sdDir = Environment.getRootDirectory();
		}
		return sdDir.getPath();
	}

	/**
	 * 获取缓存路径
	 * 
	 * @return
	 */
	public static String getCachePath() {
		Context context = AppApplication.getApplication()
				.getApplicationContext();
		final String cachePath = (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED || !Environment
				.isExternalStorageRemovable()) ? context.getExternalCacheDir()
				.getPath() : context.getCacheDir().getPath();
		return cachePath;
	}

	/**
	 * 将字符串首字母转化成大写
	 * 
	 * @param s
	 * @return
	 */
	public static String firstCharUpperCase(String s) {
		if (s == null || "".equals(s))
			return ("");
		return s.substring(0, 1).toUpperCase(Locale.CHINESE) + s.substring(1);
	}

	private static Toast mToast = null;
	/**
	 * 自定义消息提示弹出
	 * 
	 * @param text
	 */
	public static Toast makeText(String text) {
		return makeText(AppApplication.getApplication(), text, Toast.LENGTH_SHORT);
	}

	/**
	 * 自定义消息提示弹出<br/>
	 * 资源定义： 自定义图形文件lib_shape_toast_show
	 * @param context
	 * @param text
	 * @param duration
	 */
	@SuppressLint("InflateParams")
	public static Toast makeText(Context context, String text, int duration) {
		if(mToast != null){
			LinearLayout view = (LinearLayout)mToast.getView();
			TextView tv = (TextView)view.getChildAt(0);
			tv.setText(text);
		}else{
			LinearLayout.LayoutParams llParams =
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			LinearLayout layout = ViewUtil.getLinLayout(context, llParams);
			layout.setOrientation(LinearLayout.VERTICAL);
			ShapeDrawable drawable = ViewUtil.createRoundCornerShapeDrawable(40, 0, Utils.GC_MAIN_COLOR);
			//layout.setBackgroundResource(ViewUtil.getDrawableRs(context, "lib_shape_toast_show"));
			layout.setBackgroundDrawable(drawable);
			LinearLayout.LayoutParams textParams =
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			textParams.setMargins(20, 10, 20, 10);
			TextView toast_text = ViewUtil.getTextView(context, text, textParams);
			toast_text.setTextColor(Color.parseColor("#FFFFFF"));
			layout.addView(toast_text);
			mToast = new Toast(context);
			mToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 12, 130);
			mToast.setDuration(duration);
			mToast.setView(layout);
		}
		mToast.show();
		return mToast;
	}

	/**
	 * 获取随机颜色
	 * @return RGB值
	 */
	public static String getRandColorCode(){ 
		String r,g,b; 
		Random random = new Random(); 
		r = Integer.toHexString(random.nextInt(256)).toUpperCase(); 
		g = Integer.toHexString(random.nextInt(256)).toUpperCase(); 
		b = Integer.toHexString(random.nextInt(256)).toUpperCase(); 

		r = r.length()==1 ? "0" + r : r ; 
		g = g.length()==1 ? "0" + g : g ; 
		b = b.length()==1 ? "0" + b : b ; 
		String color = r+g+b;
		if("000000".equals(color) || "FFFFFF".equals(color.toUpperCase())){
			color = getRandColorCode();
		}
		return color; 
	}
}
