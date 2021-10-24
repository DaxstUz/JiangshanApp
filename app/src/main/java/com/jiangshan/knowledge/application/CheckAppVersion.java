package com.jiangshan.knowledge.application;//package com.xinle.application;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.util.Log;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.VolleyError;
//import com.android.volley.Request.Method;
//import com.android.volley.Response.ErrorListener;
//import com.android.volley.Response.Listener;
//import com.android.volley.toolbox.JsonObjectRequest;
//
///**
// * 漫云自己的版本检查（未使用）
// *
// * @author xc.li
// * @date 2015年6月25日
// */
//public class CheckAppVersion
//{
//
//	/**
//	 * 检查客户手机安装的APP的版本号是否与服务器版本号一致，小于时提示更新
//	 *
//	 * @param context
//	 */
//	public void checkAppVersion(final Context context, PackageManager pkgMag)
//	{
//		final String localVer = getVersionName(pkgMag); // 本地版本
//		NetState netState = NetReceiver.isConnected(context);
//		if (!NetState.NET_NO.equals(netState))
//		{// 存在网络时，检查版本
//			JsonObjectRequest joObjectRequest = new JsonObjectRequest(
//					Method.POST, UrlConstant.UrlqueryCurrVersion,
//					new JSONObject(), new Listener<JSONObject>()
//					{
//						@Override
//						public void onResponse(JSONObject response)
//						{
//							Log.d("tag", "response  " + response);
//							try
//							{
//								if (response.getJSONObject("object") != null)
//								{
//									// 服务器版本
//									double remoteVer = response.getJSONObject(
//											"object").getDouble("version");
//									if (remoteVer > Double.valueOf(localVer))
//									{// 当本地版本小于服务器版本时提示更新
//										// 创建对话框
//										AlertDialog dialog = new AlertDialog.Builder(
//												context).create();
//										// 设置对话框标题
//										dialog.setTitle("版本更新提示");
//										// 设置对话框消息
//										dialog.setMessage("有新版本V" + remoteVer
//												+ "，是否下载更新？");
//										// 添加选择按钮并注册监听
//										dialog.setButton("确定",
//												new BtnListener());
//										dialog.setButton2("取消",
//												new BtnListener());
//										// 显示对话框
//										dialog.show();
//									}
//								}
//							}
//							catch (JSONException e)
//							{
//								e.printStackTrace();
//							}
//						}
//					}, new ErrorListener()
//					{
//
//						@Override
//						public void onErrorResponse(VolleyError error)
//						{
//							Utils.endnet();
//						}
//					})
//			{
//				@Override
//				public Map<String, String> getHeaders() throws AuthFailureError
//				{
//					Map<String, String> map = new HashMap<String, String>();
//					map.put("Content-Type", "application/json; charset=utf-8");
//					return map;
//				}
//			};
//
//			joObjectRequest.setRetryPolicy(new DefaultRetryPolicy(6000,
//					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//			NetUtil.rqueue.add(joObjectRequest);
//		}
//	}
//
//	class BtnListener implements DialogInterface.OnClickListener
//	{
//
//		@Override
//		public void onClick(DialogInterface dialog, int which)
//		{
//			switch (which)
//			{
//				case AlertDialog.BUTTON_POSITIVE:// "确认"按钮下载更新
//
//					break;
//				case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
//					break;
//				default:
//					break;
//			}
//		}
//	}
//
//	/**
//	 * 获取本地版本号
//	 *
//	 * @param pkgMag
//	 * @return
//	 */
//	public String getVersionName(PackageManager pkgMag)
//	{
//		// getPackageName()是你当前类的包名，0代表是获取版本信息
//		try
//		{
//			PackageInfo packInfo = pkgMag.getPackageInfo("com.ch.mhy", 0);
//			String version = packInfo.versionName;
//			return version;
//		}
//		catch (NameNotFoundException e)
//		{
//			e.printStackTrace();
//			return "1.0.0";
//		}
//	}
//
//}
