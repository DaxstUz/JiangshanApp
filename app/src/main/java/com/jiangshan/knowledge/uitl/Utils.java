package com.jiangshan.knowledge.uitl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.jiangshan.knowledge.application.AppApplication;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 帮助类
 * 
 * @author DaxstUz
 * @创建时间 2015年3月2日 上午10:29:35 博客 http://blog.csdn.net/u010931818
 */
public class Utils {

	public static String token;
	public static String userId;
	public static String vipId;
	public static String pic;
	public static String vipGrade;

	public static ClickUtil clickUtil;

	public static PayUtil payUtil;

//	public static MarkMoney mark;

	/**
	 * 分页大小
	 */
	public static final int PageSize = 15;

	/**
	 * 圆角
	 */
	public static final int cornersize = 5;

	/**
	 * 点击时间间隔
	 */
	public static final long time = 1500;
	
	/**
	 * 主体颜色
	 */
	public static String GC_MAIN_COLOR = "#ea9c1f";

//	/* 图片 宽：高2.5:1 */
//	public static DisplayImageOptions options = new DisplayImageOptions.Builder()
//			// .showImageOnLoading(R.drawable.mbpic_loading) //设置图片在下载期间显示的图片
//			.showImageForEmptyUri(
//					ViewUtil.getDrawableRs(AppApplication.getApplication(),
//							"mbpic_loading"))
//			// // 设置图片Uri为空或是错误的时候显示的图片
//			// // .showImageOnFail(R.drawable.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
//			.showImageForEmptyUri(
//					ViewUtil.getDrawableRs(AppApplication.getApplication(),
//							"mbpic_loading"))
//			.showImageOnFail(
//					ViewUtil.getDrawableRs(AppApplication.getApplication(),
//							"mbpic_loading"))
//			.showImageOnLoading(
//					ViewUtil.getDrawableRs(AppApplication.getApplication(),
//							"mbpic_loading")).cacheInMemory(true)
//			// 设置下载的图片是否缓存在内存中
//			.cacheOnDisc(true)
//			// 设置下载的图片是否缓存在SD卡中
//			.bitmapConfig(Bitmap.Config.RGB_565)
//			.imageScaleType(ImageScaleType.NONE)
//			// .decodingOptions(new Bitm)//设置图片的解码配置
//			// .displayer(new RoundedBitmapDisplayer(5))
//			.build();


	/**
	 * 返回屏幕的宽、高
	 * 
	 * @return
	 */
	public static int[] getScreenDispaly() {
		WindowManager windowManager = (WindowManager) AppApplication
				.getApplication().getSystemService(Context.WINDOW_SERVICE);
		int width = windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽度
		int height = windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度
		int result[] = { width, height };
		return result;
	}

	/**
	 * 获取http请求路径：判断url是否带http://,没有则加上本地路径头
	 * 
	 * @param url
	 * @return
	 */
	public static String getHttpUrl(String url) {
		String urls = url;
		if (url.toLowerCase(Locale.CHINESE).indexOf("http://") == -1) {
//			urls = UrlConstant.Ip1 + UrlConstant.Port1 + "/" + urls;
		}
		return urls;
	}

	/**
	 * 根据端口，获取图片地址
	 * 
	 * @param port
	 * @return
	 */
	public static String getImageUrl(String port) {
		if ("8080".equals(port)) {
			return "http://img1.chamanhua.com:8080";
		}
		if ("8081".equals(port)) {
			return "http://img2.chamanhua.com:8081";

		}
		if ("8082".equals(port)) {
			return "http://img3.chamanhua.com:8082";

		}
		return null;
	}

	/**
	 * 实现文本复制功能 add by wangqianzhou
	 * 
	 * @param content
	 */
	public static void copy(String content, Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content.trim());
	}

	/**
	 * 实现粘贴功能 add by wangqianzhou
	 * 
	 * @param context
	 * @return
	 */
	public static String paste(Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		return cmb.getText().toString().trim();
	}

	private static ProgressDialog pd;

	private static AlertDialog dlg;

	// 开始请求网络
	public static void startnet(Context content) {
		// pd = new ProgressDialog(content);
		// pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// pd.setMessage("正在加载...");
		// pd.setCancelable(true); // 璁剧疆瀵硅瘽妗嗚兘鐢�鍙栨秷"鎸夐挳鍏抽棴
		// pd.setCanceledOnTouchOutside(false);
		// pd.show();

		dlg = new AlertDialog.Builder(content).create();
		dlg.setCancelable(false);
		dlg.setCanceledOnTouchOutside(false);
		dlg.show();
		Window window = dlg.getWindow();
		// 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,文件中定义view内容
		LinearLayout lib_base_ll = ViewUtil.getLinLayout(content);
		lib_base_ll.setGravity(Gravity.CENTER);
		lib_base_ll.setOrientation(LinearLayout.VERTICAL);
		window.setContentView(lib_base_ll);
		ProgressBar pb = new ProgressBar(content);
		lib_base_ll.addView(pb);
	}

	// 结束
	public static void endnet() {
		// if(pd!=null){
		// pd.dismiss();
		// }
		if (dlg != null) {
			dlg.dismiss();
		}
	}

	/**
	 * 返回距离当前有多少小时
	 * */
	public static long dataToNow(Date d) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long days = 0;
		try {
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = format.format(date);
			long diff = date.getTime() - d.getTime();
			days = diff / (1000 * 60 * 60 * 24);
		} catch (Exception e) {
		}
		return days;

	}

//	public static Update updateInfo;
}
