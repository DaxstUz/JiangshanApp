package com.jiangshan.knowledge.uitl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 各类View创建帮助类
 * @author xc.li
 * @date 2015年12月5日
 */
public class ViewUtil {

	/**
	 * 创建View
	 * @param context
	 * @param lop 可选
	 * @return
	 */
	public static View getView(Context context, LayoutParams... lop){
		View view = new View(context);
		viewLayoutParamsMatch(view, lop);
		return view;
	}

	/**
	 * 创建LinearLayout
	 * @param context
	 * @param lop 可选
	 * @return
	 */
	public static LinearLayout getLinLayout(Context context, LayoutParams... lop){
		LinearLayout view = new LinearLayout(context);
		viewLayoutParamsMatch(view, lop);
		return view;
	}

	/**
	 * 创建RelativeLayout
	 * @param context
	 * @param lop 可选
	 * @return
	 */
	public static RelativeLayout getRelaLayout(Context context, LayoutParams... lop){
		RelativeLayout view = new RelativeLayout(context);
		viewLayoutParamsMatch(view, lop);
		return view;
	}

	/**
	 * 创建FrameLayout
	 * @param context
	 * @param lop 可选
	 * @return
	 */
	public static FrameLayout getFrameLayout(Context context, LayoutParams... lop){
		FrameLayout view = new FrameLayout(context);
		viewLayoutParamsMatch(view, lop);
		return view;
	}

	/**
	 * 创建ImageView 默认WRAP_CONTENT
	 * @param context
	 * @param lop 可选
	 * @return
	 */
	public static ImageView getImageView(Context context, String res, LayoutParams... lop){
		ImageView view = new ImageView(context);
		viewLayoutParamsWrap(view, lop);
		if(res.indexOf(".")==-1)
			view.setBackgroundResource(getDrawableRs(context, res.toString()));
		else
			view.setImageDrawable(getAssetsDrawable(context, res));
		return view;
	}

	/**
	 * 创建Button
	 * @param context
	 * @param lop 可选
	 * @return
	 */
	public static Button getButton(Context context, String val, LayoutParams... lop){
		Button view = new Button(context);
		viewLayoutParamsWrap(view, lop);
		if(val != null)
			view.setText(val);
		return view;
	}

	/**
	 * 创建TextView
	 * @param context
	 * @param lop 可选
	 * @return
	 */
	public static TextView getTextView(Context context, String val, LayoutParams... lop){
		TextView view = new TextView(context);
		viewLayoutParamsWrap(view, lop);
		if(val != null)
			view.setText(val);
		return view;
	}

	public static EditText getEditText(Context context, String val, LayoutParams... lop){
		EditText view = new EditText(context);
		viewLayoutParamsWrap(view, lop);
		if(val != null)
			view.setText(val);
		return view;
	}

	public static CheckBox getCheckBox(Context context, String name, LayoutParams... lop){
		CheckBox view = new CheckBox(context);
		viewLayoutParamsWrap(view, lop);
		view.setText(name);
		return view;
	}

	/**
	 * 自定义checkbox
	 * @param context
	 * @param val
	 * @param checked
	 * @param lop
	 * @return
	 */
//	public static MyCheckBox getMyCheckBox(final Context context, String val, String text, boolean checked, LayoutParams... lop){
//		MyCheckBox view = (MyCheckBox)getLinLayout(context);
//		view.setOrientation(LinearLayout.HORIZONTAL);
//		view.setGravity(Gravity.CENTER_VERTICAL);
//		view.setTag(checked);
//		viewLayoutParamsWrap(view, lop);
//		String resName = "btn_check_off";
//		if(checked){
//			resName = "btn_check_on";
//		}
//		final ImageView iv = getImageView(context, resName);
//		view.addView(iv);
//		iv.setTag(val);
//		TextView tv = getTextView(context, text);
//		tv.setTextColor(Color.GRAY);
//		view.addView(tv);
//		view.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(v.getTag()!=null && Boolean.valueOf(v.getTag().toString())){
//					iv.setImageResource(getDrawableRs(context, "btn_check_off"));
//					v.setTag(false);
//				}else if(v.getTag()!=null && !Boolean.valueOf(v.getTag().toString())){
//					iv.setImageResource(getDrawableRs(context, "btn_check_on"));
//					v.setTag(true);
//				}
//			}
//		});
//		return view;
//	}

	/**
	 * 获取下拉选择框
	 * <LinearLayout
        android:id="@+id/testdd"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal" >

        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="哈哈" />
    	</LinearLayout>

	 * List<ItemBean> dataList = new ArrayList<ItemBean>();
			ItemBean bean = new ItemBean();
			bean.text = "测试";
			bean.value = "test";
			bean.startLogo = "floating_caohua_logo";
			bean.endLogo = "skd_note_dotor";
			dataList.add(bean);
			ItemBean bean1 = new ItemBean();
			bean1.text = "测试1";
			bean1.value = "test1";
			bean1.startLogo = "floating_caohua_logo";
			bean1.endLogo = "skd_note_dotor";
			dataList.add(bean1);

			LinearLayout.LayoutParams lp = ViewUtil.getLinearLayoutParams(1);
			lp.width = 200;
			lp.setMargins(0, 5, 0, 5);
			LinearLayout select = ViewUtil.getSelectView(this.getActivity(), dataList, lp);

			LinearLayout ttt = (LinearLayout)view.findViewById(ViewUtil.getIdRs(this.getActivity(), "testdd"));
			ttt.addView(select);
	 * @param context
	 * @param val
	 * @param text
	 * @param checked
	 * @param lop
	 */
//	public static LinearLayout getSelectView(final Context context, List<ItemBean> dataList, LayoutParams... lop){
//		LinearLayout container = getLinLayout(context, lop);
//		container.setOrientation(LinearLayout.HORIZONTAL);
//		container.setGravity(Gravity.CENTER_VERTICAL);
//		container.setBackgroundColor(Color.LTGRAY);
//		String text = "", value = "";
//		if(dataList != null && dataList.size()>0){
//			ItemBean firBean = dataList.get(0);
//			text = firBean.text;
//			value = firBean.value;
//		}
//
//		LinearLayout.LayoutParams txtlp = getLinearLayoutParams(4);
//		txtlp.weight = 1;
//		final EditText tv = getEditText(context, text, txtlp);
//		tv.setGravity(Gravity.CENTER_VERTICAL);
//		container.addView(tv);
//		tv.setTag(value);
//		LinearLayout.LayoutParams ivlp = getLinearLayoutParams(4);
//		ivlp.weight = 0.2f;
//		ImageView iv = getImageView(context, "arrow_down", ivlp);
//		container.addView(iv);
//		iv.setTag(false);
//		MyPopuWindow.SelectValueCallBack callback = new SelectValueCallBack(){
//			@Override
//			public void onItemClick(ItemBean data, View v) {
//				tv.setText(data.text);
//				tv.setTag(data.value);
//				Object tag = v.getTag();
//				if(tag != null && !Boolean.valueOf(tag.toString())){
//					v.setTag(true);
//					v.setBackgroundResource(getDrawableRs(context, "arrow_up"));
//				}else{
//					v.setTag(false);
//					v.setBackgroundResource(getDrawableRs(context, "arrow_down"));
//				}
//			}
//
//			@Override
//			public View buildItem(ItemBean data) {
//				return null;
//			}
//		};
//		final MyPopuWindow myPopuWin = getPopuWin(context, container, iv, dataList, callback);
//		iv.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				Object tag = v.getTag();
//				if(tag != null && !Boolean.valueOf(tag.toString())){
//					v.setTag(true);
//					myPopuWin.show();
//					v.setBackgroundResource(getDrawableRs(context, "arrow_up"));
//				}else{
//					v.setTag(false);
//					myPopuWin.close();
//					v.setBackgroundResource(getDrawableRs(context, "arrow_down"));
//				}
//			}
//		});
//		return container;
//	}

	/**
	 * 根据点击按钮位置显示弹出框
	 * @param context
	 * @param container 点击按钮的容器
	 * @param clicker 点击按钮
	 * @param dataList 列表数据
	 * @param callback 列表点击回调
	 * @return
	 */
//	public static MyPopuWindow getPopuWin(final Context context, View container, View clicker,
//                                          List<MyPopuWindow.ItemBean> dataList, SelectValueCallBack callback){
//		MyPopuWindow ppw = new MyPopuWindow(context, container, clicker, dataList);
//		ppw.setCallback(callback);
//		return ppw;
//	}


	/**
	 * 获取Assets目录下的图片
	 * @param context
	 * @param name
	 * @return
	 */
	public static BitmapDrawable getAssetsDrawable(Context context, String name) {
		AssetManager am = context.getAssets();
		InputStream is = null;
		Bitmap bitmap = null;
		try {
			is = am.open("CaoHuaSDK/Images/"+name);
			bitmap = BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new BitmapDrawable(bitmap);
	}

	/**
	 * 设置布局参数MATCH_PARENT
	 * @param view
	 * @param lop 可选
	 */
	private static void viewLayoutParamsMatch(View view, LayoutParams... lop) {
		if(lop != null && lop.length>0){
			view.setLayoutParams(lop[0]);
		}else{
			LinearLayout.LayoutParams params =
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			view.setLayoutParams(params);
		}
	}
	/**
	 * 设置布局参数WRAP_CONTENT
	 * @param view
	 * @param lop 可选
	 */
	private static void viewLayoutParamsWrap(View view, LayoutParams... lop) {
		if(lop != null && lop.length>0){
			view.setLayoutParams(lop[0]);
		}else{
			LinearLayout.LayoutParams params =
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			view.setLayoutParams(params);
		}
	}

	/**
	 * 获取LinearLayout布局参数
	 * @param type 0： 横向MATCH_PARENT，纵向WRAP_CONTENT; <br/>
	 * 		1： 横向WRAP_CONTENT，纵向MATCH_PARENT; <br/>
	 * 		2： 横向MATCH_PARENT，纵向MATCH_PARENT; <br/>
	 * 		3：横向WRAP_CONTENT，纵向WRAP_CONTENT; <br/>
	 *  	4：横向0，纵向MATCH_PARENT; <br/>
	 *  	5：横向MATCH_PARENT，纵向0; <br/>
	 * @return
	 */
	public static LinearLayout.LayoutParams getLinearLayoutParams(int type){
		LinearLayout.LayoutParams params =
			new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		if(type == 1){
			params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		}else if(type == 2){
			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		}else if(type == 3){
			params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}else if(type == 4){
			params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
		}else if(type == 5){
			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
		}
		return params;
	}

	/**
	 * 获取RelativeLayout布局参数
	 * @param type 0： 横向MATCH_PARENT，纵向WRAP_CONTENT; <br/>
	 * 		1： 横向WRAP_CONTENT，纵向MATCH_PARENT; <br/>
	 * 		2： 横向MATCH_PARENT，纵向MATCH_PARENT; <br/>
	 * 		3：横向WRAP_CONTENT，纵向WRAP_CONTENT
	 * @return
	 */
	public static RelativeLayout.LayoutParams getRelaLayoutParams(int type){
		RelativeLayout.LayoutParams params =
			new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		if(type == 1){
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		}else if(type == 2){
			params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		}else if(type == 3){
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		return params;
	}

	/**
	 * 获取布局资源文件ID
	 * @param context
	 * @param resName 布局文件名
	 * @return
	 */
	public static int getLayoutRs(Context context, String resName){
		return context.getResources().getIdentifier(resName, "layout", context.getPackageName());
	}
	/**
	 * 获取布局资源里布局组件View的ID
	 * @param context
	 * @param resName Id名称
	 * @return
	 */
	public static int getIdRs(Context context, String resName){
		return context.getResources().getIdentifier(resName, "id", context.getPackageName());
	}
	/**
	 * 获取图形资源drawable的ID
	 * @param context
	 * @param resName
	 * @return
	 */
	public static int getDrawableRs(Context context, String resName){
		return context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
	}
	/**
	 * 获取颜色资源ID
	 * @param context
	 * @param resName
	 * @return
	 */
	public static int getColorRs(Context context, String resName){
		return context.getResources().getIdentifier(resName, "color", context.getPackageName());
	}
	/**
	 * 获取样式资源ID
	 * @param context
	 * @param resName 样式名称
	 * @return
	 */
	public static int getStyleRs(Context context, String resName){
		return context.getResources().getIdentifier(resName, "style", context.getPackageName());
	}

	/**
	 * 获取属性资源ID
	 * @param context
	 * @param resName
	 * @return
	 */
	public static int getAttrRs(Context context, String resName){
		return context.getResources().getIdentifier(resName, "attr", context.getPackageName());
	}

	/**
	 * 获取属性资源ID
	 * @param context
	 * @param resName
	 * @return
	 */
	public static int getStringRs(Context context, String resName){
		return context.getResources().getIdentifier(resName, "string", context.getPackageName());
	}

	/**
	 * 获取styleable资源ID
	 * @param context
	 * @param resName 样式名称
	 * @return
	 */
	public static int getStyleableRs(Context context, String resName){
		return context.getResources().getIdentifier(resName, "styleable", context.getPackageName());
	}
	/**
	 * 获取styleable资源Array
	 * @param context
	 * @param resName
	 * @return
	 */
	public static final int[] getStyleablesRsIntArray(Context context, String resName) {
		try {
			Field[] fields2 = Class.forName(context.getPackageName() + ".R$styleable").getFields();
			for (Field f : fields2) {
				if (f.getName().equals(resName)) {
					int[] ret = (int[]) f.get(null);
					return ret;
				}
			}
		} catch (Throwable t) {
		}
		return null;
	}


	/** 设置Selector。 */
    public static StateListDrawable newSelector(Context context, int idNormal, int idPressed, int idFocused,
                    int idUnable) {
            StateListDrawable bg = new StateListDrawable();
            Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
            Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
            Drawable focused = idFocused == -1 ? null : context.getResources().getDrawable(idFocused);
            Drawable unable = idUnable == -1 ? null : context.getResources().getDrawable(idUnable);
            // View.PRESSED_ENABLED_STATE_SET
            bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);
            // View.ENABLED_FOCUSED_STATE_SET
            bg.addState(new int[] { android.R.attr.state_enabled, android.R.attr.state_focused }, focused);
            // View.ENABLED_STATE_SET
            bg.addState(new int[] { android.R.attr.state_enabled }, normal);
            // View.FOCUSED_STATE_SET
            bg.addState(new int[] { android.R.attr.state_focused }, focused);
            // View.WINDOW_FOCUSED_STATE_SET
            bg.addState(new int[] { android.R.attr.state_window_focused }, unable);
            // View.EMPTY_STATE_SET
            bg.addState(new int[] {}, normal);
            return bg;
    }

    /**
	 * 获取圆角边框矩形的Drawable，使用：<br/>
	 * view.setBackgroundDrawable(ViewUtil.createRoundCornerShapeDrawable(100, 100, Color.RED));
	 * @param radius 角弧度，越大越圆
	 * @param borderLength 边框厚度，如果不需要边框，则设置足够大即可
	 * @param borderColor 边框颜色
	 * @return
	 */
	public static ShapeDrawable createRoundCornerShapeDrawable(float radius,
			float borderLength, int borderColor) {
		float[] outerRadii = new float[8];
		float[] innerRadii = new float[8];
		for (int i = 0; i < 8; i++) {
			outerRadii[i] = radius + borderLength;
			innerRadii[i] = radius;
		}
		RoundRectShape rrs=new RoundRectShape(outerRadii,
				new RectF(borderLength, borderLength, borderLength, borderLength), innerRadii);

		ShapeDrawable sd = new ShapeDrawable(rrs);
		sd.getPaint().setColor(borderColor);

		return sd;
	}

	public static ShapeDrawable createRoundCornerShapeDrawable(float radius,
			float borderLength, String borderColor) {
		float[] outerRadii = new float[8];
		float[] innerRadii = new float[8];
		for (int i = 0; i < 8; i++) {
			outerRadii[i] = radius + borderLength;
			innerRadii[i] = radius;
		}
		ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(outerRadii,
				new RectF(borderLength, borderLength, borderLength, borderLength), innerRadii));
		sd.getPaint().setColor(Color.parseColor(borderColor));
		return sd;
	}

	/**
	 * 获取左圆角边框矩形的Drawable，使用：<br/>
	 * view.setBackgroundDrawable(ViewUtil.createRoundCornerShapeDrawable(100, 100, Color.RED));
	 * @param radius 角弧度，越大越圆
	 * @param borderLength 边框厚度，如果不需要边框，则设置足够大即可
	 * @param borderColor 边框颜色
	 * @return
	 */
	public static ShapeDrawable createLeftRoundCornerShapeDrawable(float radius,
			float borderLength, int borderColor) {
		float[] outerRadii = new float[8];
		float[] innerRadii = new float[8];
		outerRadii[0] = radius + borderLength;
		innerRadii[0] = radius;
		outerRadii[1] = radius + borderLength;
		innerRadii[1] = radius;
		outerRadii[6] = radius + borderLength;
		innerRadii[6] = radius;
		outerRadii[7] = radius + borderLength;
		innerRadii[7] = radius;

		ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(outerRadii,
				new RectF(borderLength, borderLength, borderLength,
						borderLength), innerRadii));
		sd.getPaint().setColor(borderColor);
		return sd;
	}

	/**
	 * 获取右圆角边框矩形的Drawable，使用：<br/>
	 * view.setBackgroundDrawable(ViewUtil.createRoundCornerShapeDrawable(100, 100, Color.RED));
	 * @param radius 角弧度，越大越圆
	 * @param borderLength 边框厚度，如果不需要边框，则设置足够大即可
	 * @param borderColor 边框颜色
	 * @return
	 */
	public static ShapeDrawable createRightRoundCornerShapeDrawable(float radius,
			float borderLength, int borderColor) {
		float[] outerRadii = new float[8];
		float[] innerRadii = new float[8];
		outerRadii[2] = radius + borderLength;
		innerRadii[2] = radius;
		outerRadii[3] = radius + borderLength;
		innerRadii[3] = radius;
		outerRadii[4] = radius + borderLength;
		innerRadii[4] = radius;
		outerRadii[5] = radius + borderLength;
		innerRadii[5] = radius;

		ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(outerRadii,
				new RectF(borderLength, borderLength, borderLength,
						borderLength), innerRadii));
		sd.getPaint().setColor(borderColor);
		return sd;
	}

	public static ShapeDrawable createTopRoundCornerShapeDrawable(float radius,
			float borderLength, String borderColor) {
		float[] outerRadii = new float[8];
		float[] innerRadii = new float[8];
		outerRadii[0] = radius + borderLength;
		innerRadii[0] = radius;
		outerRadii[1] = radius + borderLength;
		innerRadii[1] = radius;
		outerRadii[2] = radius + borderLength;
		innerRadii[2] = radius;
		outerRadii[3] = radius + borderLength;
		innerRadii[3] = radius;

		ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(outerRadii,
				new RectF(borderLength, borderLength, borderLength,
						borderLength), innerRadii));
		sd.getPaint().setColor(Color.parseColor(borderColor));
		return sd;
	}

	public static ShapeDrawable createBotRoundCornerShapeDrawable(float radius,
			float borderLength, String borderColor) {
		float[] outerRadii = new float[8];
		float[] innerRadii = new float[8];
		outerRadii[4] = radius + borderLength;
		innerRadii[4] = radius;
		outerRadii[5] = radius + borderLength;
		innerRadii[5] = radius;
		outerRadii[6] = radius + borderLength;
		innerRadii[6] = radius;
		outerRadii[7] = radius + borderLength;
		innerRadii[7] = radius;

		ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(outerRadii,
				new RectF(borderLength, borderLength, borderLength,
						borderLength), innerRadii));
		sd.getPaint().setColor(Color.parseColor(borderColor));
		return sd;
	}

	/**
	 * 重新计算设置ListView的高度
	 * @param listView
	 * @param adapter
	 */
	public static void reSetLisViewtHeight(ListView listView, Adapter adapter) {
		int totalHeight = 0;
		int count = adapter.getCount();
		// 计算高度
		for (int i = 0; i < count; i++) {
			View listItem = adapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight() + listView.getDividerHeight();
		}
		// 设置高度
		LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight;
		listView.setLayoutParams(params);
	}

	/**
	 * 重新计算设置GridView的高度
	 * @param gridView
	 * @param adapter
	 * @param column 列数
	 */
	public static void reSetGridViewHeight(GridView gridView, Adapter adapter, int column) {
		int totalHeight = 0;
		int count = adapter.getCount();
		int rows = count / column;
		if (count % column > 0) {
			rows += 1;
		}
		for (int i = 0; i < rows; i++) {
			View listItem = adapter.getView(i, null, gridView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight /* + (gv_book_select.getHeight() * (rows - 1))*/;
		gridView.setLayoutParams(params);
	}
}
