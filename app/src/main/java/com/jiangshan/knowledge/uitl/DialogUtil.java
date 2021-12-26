package com.jiangshan.knowledge.uitl;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 弹框工具类<br/>
 * 只要调用alertDialog方法即可，例子：<br/>
 * DialogUtil.alertDialog(this, "系统提示", "弹框测试", new String[]{"取消", "确定"}, false,
 * null, null, new AlertButtonClick(){
 *
 * @author xc.li
 * @Override public void leftBtnClick(AlertDialog dlg) { dlg.dismiss(); }
 * @Override public void rightBtnClick(AlertDialog dlg) { dlg.dismiss(); } });
 * @date 2015年12月11日
 */
public class DialogUtil {

    public static DialogAttrs attrs = new DialogAttrs();

    /**
     * APP消息提示框<br/>
     * 消息框标题背景资源： shape_alert_dialog_head.xml<br/>
     * 消息框主体背景资源：shape_alert_dialog_body.xml<br/>
     * 按钮样式资源：shape_corner.xml<br/>
     *
     * @param context      Activity
     * @param title        标题
     * @param msg          消息主体 为null时将不显示消息体
     * @param btnVal       左右按钮数组
     * @param isCancelable 弹框是否可以触摸取消，true/false
     * @param view         自定义布局内容(可为null)
     * @param logoImg      顶部图片(可为null)
     * @param click        按钮点击回调事件，客户业务定义
     */
    @Deprecated
    public static void alertDialog(final Activity context, String title,
                                   String msg, String[] btnVal, boolean isCancelable,
                                   LinearLayout view, String logoImg, final AlertButtonClick click) {
        if (context == null || context.isFinishing()) {
            return;
        }
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setCancelable(isCancelable);
        dlg.setCanceledOnTouchOutside(isCancelable);
        dlg.show();
        Window window = dlg.getWindow();
        // 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,文件中定义view内容
        LinearLayout lib_base_ll = ViewUtil.getLinLayout(context);
        lib_base_ll.setGravity(Gravity.CENTER);
        lib_base_ll.setOrientation(LinearLayout.VERTICAL);
        window.setContentView(lib_base_ll);

        LinearLayout lib_main_ll = ViewUtil.getLinLayout(context);
        lib_main_ll.setOrientation(LinearLayout.VERTICAL);
        final LayoutParams titleParamsll = ViewUtil
                .getLinearLayoutParams(0);
        LinearLayout lib_title_ll = ViewUtil.getLinLayout(context,
                titleParamsll);
        lib_title_ll.setBackgroundResource(ViewUtil.getDrawableRs(context,
                "shape_alert_dialog_head"));
        lib_main_ll.addView(lib_title_ll);

        /*
         * 横线 LayoutParams lp=new LayoutParams(LayoutParams.FILL_PARENT, 5);
         * TextView tv=new TextView(context); tv.setLayoutParams(lp);
         * tv.setBackgroundColor(Color.parseColor("#f57d27"));
         * lib_main_ll.addView(tv);
         */

        LayoutParams titleParamLins = ViewUtil
                .getLinearLayoutParams(0);
        LinearLayout lib_title_lin = ViewUtil.getLinLayout(context,
                titleParamLins);
        lib_title_lin.setBackgroundResource(ViewUtil.getColorRs(context,
                "green"));
        lib_title_ll.addView(lib_title_lin);

        LayoutParams titleParams = ViewUtil
                .getLinearLayoutParams(0);
        titleParams.leftMargin = 20;
        TextView msgTitle = ViewUtil.getTextView(context, title, titleParams);
        msgTitle.setBackgroundResource(ViewUtil.getColorRs(context, "green"));
        msgTitle.setGravity(Gravity.CENTER_VERTICAL);
        msgTitle.setTextColor(Color.WHITE);
        msgTitle.setHeight(80);
        lib_title_lin.addView(msgTitle);

        LinearLayout lib_content_ll = ViewUtil.getLinLayout(context);
        lib_content_ll.setBackgroundResource(ViewUtil.getDrawableRs(context,
                "shape_alert_dialog_body"));
        lib_content_ll.setOrientation(LinearLayout.VERTICAL);
        lib_content_ll.setGravity(Gravity.CENTER_VERTICAL);

        if (msg != null) {
            LayoutParams msgParams = ViewUtil
                    .getLinearLayoutParams(0);
            msgParams.setMargins(20, 40, 20, 140);
            TextView msgTv = ViewUtil.getTextView(context, msg, msgParams);
            msgTv.setTextColor(Color.BLACK);
            lib_content_ll.addView(msgTv);
        }
        if (view != null)
            lib_content_ll.addView(view);

        LayoutParams linearLayoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, 2);
        LinearLayout lin_jx = ViewUtil
                .getLinLayout(context, linearLayoutParams);
        lin_jx.setBackgroundResource(ViewUtil.getColorRs(context, "ch_gray"));
        lib_content_ll.addView(lin_jx);

        LinearLayout buttonLl = getBtnView(context, dlg, btnVal, click);
        lib_content_ll.addView(buttonLl);

        lib_main_ll.addView(lib_content_ll);

        RelativeLayout.LayoutParams logoParams = ViewUtil
                .getRelaLayoutParams(0);
        logoParams
                .addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        LinearLayout lib_logo_ll = ViewUtil.getLinLayout(context, logoParams);

        if (logoImg != null) {
            final ImageView logo = ViewUtil.getImageView(context, "logo.png");
            lib_logo_ll.addView(logo);

            (new Handler()).post(new Runnable() {
                @Override
                public void run() {
                    titleParamsll.topMargin = logo.getHeight() * 4 / 5;
                }
            });
        }
        LayoutParams relaParams = ViewUtil
                .getLinearLayoutParams(2);
        RelativeLayout lib_rela_rl = ViewUtil
                .getRelaLayout(context, relaParams);
        lib_rela_rl.addView(lib_main_ll);
        lib_rela_rl.addView(lib_logo_ll);

        lib_base_ll.addView(lib_rela_rl);
    }

//	/**
//	 * 请求异常提示框
//	 *
//	 * @param context
//	 * @param errMsg  异常信息
//	 * @param params  刷新再次请求时所需参数
//	 */
//	public static void errorAlertDialog(final Activity context, String errMsg) {
//		AlertButtonClick click = new AlertButtonClick() {
//			@Override
//			public void leftBtnClick(AlertDialog dlg) {
//				RequestDataUtil.removePageReqCount(context);
//				RequestDataUtil.removeErrReqCount(context);
//				dlg.dismiss();
//			}
//
//			@Override
//			public void rightBtnClick(AlertDialog dlg) {
//				int netState = NetInfoCache.getNetState();
//				if (netState > 0) {
//					RequestDataUtil.removePageReqCount(context);
//					RequestDataUtil.removeErrReqCount(context);
//					List<List<Object>> errList = RequestDataUtil
//							.getErrReqList(context);
//					for (int i = 0; i < errList.size(); i++) {
//						List<Object> params = errList.get(i);
//						int type = Integer.valueOf(params.get(0).toString());
//						String url = params.get(1).toString();
//						@SuppressWarnings("unchecked")
//						HashMap<String, Object> param = (HashMap<String, Object>) params
//								.get(2);
//						AbsResponseData data = (AbsResponseData) params.get(3);
//						if (type == 1) {
//							RequestDataUtil.requestPageData(context, url,
//									param, data);
//						} else if (type == 2) {
//							RequestDataUtil.requestObjectData(context, url,
//									param, data);
//						} else if (type == 3) {
//							RequestDataUtil.requestJSONList(context, url,
//									param, data);
//						} else if (type == 4) {
//							RequestDataUtil.updateData(context, url, param,
//									data);
//						}
//					}
//					dlg.dismiss();
//				} else {
//					SysUtil.makeText(context, "亲，请先连接网络再刷新哦，@~~^_^！", Toast.LENGTH_LONG);
//				}
//			}
//		};
//		DialogAttrs attrs = new DialogAttrs();
//		attrs.btnVal = new String[]{"取消", "刷新"};
//		attrs.msg = errMsg;
//		attrs.title = "温馨提示";
//		attrs.titleColor = "#FFFFFF";
//		attrs.dialogColor = Utils.GC_MAIN_COLOR;
//		attrs.btnTextColor = "#000000";
//		attrs.leftBtnColor = "#FFFFFF";
//		attrs.rightBtnColor = "#FFFFFF";
//		attrs.sepLineColor = "#CCCCCC";
//
//		attrs.textGravity = Gravity.CENTER_VERTICAL;
//		DialogUtil.alertDialog(context, attrs, click);
//
//		/*DialogUtil.alertDialog(context, "系统提示", errMsg, new String[] { "取消",
//				"刷新" }, false, null, null, click);*/
//	}

    @Deprecated
    public static void alertDialog(final Activity context, String msg,
                                   final LinearLayout view, final AlertButtonClick click) {
        if (context == null || context.isFinishing()) {
            return;
        }
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        Window window = dlg.getWindow();
        LinearLayout baseView = ViewUtil.getLinLayout(context);
        window.setContentView(baseView);

        LayoutParams custParams = ViewUtil
                .getLinearLayoutParams(3);
        custParams.setMargins(10, 0, 10, 0);

        LinearLayout custView = ViewUtil.getLinLayout(context);
        custView.setOrientation(LinearLayout.VERTICAL);
        custView.setGravity(Gravity.CENTER);
        custView.setBackgroundColor(Color.GRAY);
        custView.setBackgroundResource(ViewUtil.getDrawableRs(context,
                "shape_alert_dialog"));
        baseView.addView(custView, custParams);

        LayoutParams imgParams = ViewUtil.getLinearLayoutParams(3);
        custParams.setMargins(10, 10, 10, 10);
        ImageView imagView = ViewUtil.getImageView(context, "shake_logo.png");
        custView.addView(imagView, imgParams);

        TextView msgView = ViewUtil.getTextView(context, msg);
        msgView.setTextColor(Color.WHITE);
        custView.addView(msgView, custParams);

        if (view != null)
            custView.addView(view);

        LinearLayout buttonLl = getBtnView(context, dlg, new String[]{"取消",
                "隐藏"}, click);

        custView.addView(buttonLl);
    }

    /**
     * 加载进度条
     *
     * @param context
     * @return
     */
    public static AlertDialog alertLoading(Activity context) {
        AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setCancelable(false);
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();
        Window window = dlg.getWindow();
        // 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,文件中定义view内容
        LinearLayout lib_base_ll = ViewUtil.getLinLayout(context);
        lib_base_ll.setGravity(Gravity.CENTER);
        lib_base_ll.setOrientation(LinearLayout.VERTICAL);
        window.setContentView(lib_base_ll);
        ProgressBar pb = new ProgressBar(context);
        lib_base_ll.addView(pb);
        return dlg;
    }

    /**
     * 自定义弹出框，该方法不需要定义的shape xml资源文件 DialogAttrs attrs = new DialogAttrs();
     * attrs.msg = "我是测试哦";
     * <p/>
     * //attrs.title = "系统登录"; //attrs.titleColor = "#000000";
     * //attrs.titleGravity = Gravity.CENTER; //attrs.textColor = "#000000";
     * //attrs.textGravity = Gravity.CENTER; attrs.btnVal = new String[]{"取消",
     * "确定"};//可不设置
     * <p/>
     * AlertButtonClick click = new AlertButtonClick(){
     *
     * @param context
     * @param attrs   弹出框的配置属性，里面的每个属性都定义了默认值
     * @param click   按钮响应事件回调，根据按钮属性的设置可为空
     * @Override public void leftBtnClick(AlertDialog dlg) { }
     * @Override public void rightBtnClick(AlertDialog dlg) { } };
     * DialogUtil.alertDialog(this, attrs, click);
     */
    public static void alertDialog(Activity context, DialogAttrs attrs,
                                   AlertButtonClick click) {
        if (context == null || context.isFinishing()) {
            return;
        }
        AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setCancelable(attrs.isCancelable);
        dlg.setCanceledOnTouchOutside(attrs.isCancelable);
        dlg.show();
        Window window = dlg.getWindow();
        // 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,文件中定义view内容

        int wh[] = SysUtil.getScreenDispaly();
        LinearLayout lib_base_ll = ViewUtil.getLinLayout(context);
        lib_base_ll.setGravity(Gravity.CENTER);
        lib_base_ll.setOrientation(LinearLayout.VERTICAL);
        window.setContentView(lib_base_ll);

        // 提示框头部
        LinearLayout title_ll = ViewUtil.getLinLayout(context);
        title_ll.setOrientation(LinearLayout.VERTICAL);
        title_ll.setBackgroundDrawable(ViewUtil
                .createTopRoundCornerShapeDrawable(attrs.dialogConnerSize, 0,
                        attrs.dialogColor));
        title_ll.setGravity(attrs.titleGravity);
        TextView titleView = ViewUtil.getTextView(context, attrs.title);
        titleView.setTextColor(Color.parseColor(attrs.titleColor));
        titleView.setPadding(20, 20, 20, 20);
        title_ll.addView(titleView);

        LayoutParams sepLp = ViewUtil.getLinearLayoutParams(0);
        sepLp.height = 2;
        View seprator = ViewUtil.getView(context, sepLp);
        seprator.setBackgroundColor(Color.parseColor(Utils.GC_MAIN_COLOR));
        title_ll.addView(seprator);
        title_ll.setVisibility(View.GONE);
        lib_base_ll.addView(title_ll);

        // 提示框主体部分
        LinearLayout body = ViewUtil.getLinLayout(context);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setBackgroundDrawable(ViewUtil.createBotRoundCornerShapeDrawable(
                attrs.dialogConnerSize, 2, attrs.conerColor));
        lib_base_ll.addView(body);

        // 内容部分
        LayoutParams lp = ViewUtil.getLinearLayoutParams(0);
        ShapeDrawable bodyDrawable = null;
        if (attrs.btnVal != null) {
            lp.setMargins(2, 0, 2, 0);
            bodyDrawable = ViewUtil.createRoundCornerShapeDrawable(0, 0,
                    "#FFFFFF");
        } else {
            lp.setMargins(2, 0, 2, 2);
            bodyDrawable = ViewUtil.createBotRoundCornerShapeDrawable(
                    attrs.dialogConnerSize, 0, "#FFFFFF");
        }
        LinearLayout body_ll = ViewUtil.getLinLayout(context, lp);
        body_ll.setOrientation(LinearLayout.VERTICAL);
        body_ll.setGravity(attrs.textGravity);
        body_ll.setBackgroundDrawable(bodyDrawable);

        if (attrs.customView == null) {
            TextView msgView = ViewUtil.getTextView(context, attrs.msg);
            msgView.setPadding(10, 10, 10, 10);
            msgView.setTextColor(Color.parseColor(attrs.textColor));
            msgView.setHeight(wh[0] / 2 - 200);
            msgView.setGravity(attrs.textGravity);
            body_ll.addView(msgView);
        } else {
            body_ll.addView(attrs.customView);
        }
        body.addView(body_ll);

        // 提示框按钮部分
        if (attrs.btnVal != null) {
            LayoutParams footsepLp = ViewUtil.getLinearLayoutParams(0);
            footsepLp.height = 2;
            View footseprator = ViewUtil.getView(context, footsepLp);
            footseprator.setBackgroundColor(Color.parseColor(attrs.sepLineColor));
            body.addView(footseprator);

            LinearLayout body_foot = getFootView(context, dlg, attrs, click);
            body_foot.setBackgroundDrawable(ViewUtil
                    .createBotRoundCornerShapeDrawable(Utils.cornersize, 0,
                            "#FFFFFF"));
            LayoutParams flp = ViewUtil.getLinearLayoutParams(0);
            flp.setMargins(2, 0, 2, 2);
            body_foot.setLayoutParams(flp);
            body.addView(body_foot);
        }
    }

    @SuppressWarnings("deprecation")
    public static LinearLayout getFootView(final Activity context,
                                           final AlertDialog dlg, DialogAttrs attrs,
                                           final AlertButtonClick click) {
        LinearLayout buttonLl = ViewUtil.getLinLayout(context);
        buttonLl.setOrientation(LinearLayout.HORIZONTAL);
        buttonLl.setGravity(Gravity.CENTER);

        // 按钮
        TextView leftBtn = ViewUtil.getTextView(context, attrs.btnVal[0]);
//        leftBtn.setBackgroundDrawable(ViewUtil.createRoundCornerShapeDrawable(
//                Utils.cornersize, 0, attrs.leftBtnColor));
        leftBtn.setTextColor(Color.parseColor(attrs.btnTextColor));
        leftBtn.setPadding(5, 5, 5, 5);
        leftBtn.setGravity(Gravity.CENTER);

//        TextView rightBtn = ViewUtil.getTextView(context, attrs.btnVal[1]);
//        rightBtn.setBackgroundDrawable(ViewUtil.createRoundCornerShapeDrawable(
//                Utils.cornersize, 0, attrs.rightBtnColor));
//        rightBtn.setTextColor(Color.parseColor(attrs.btnTextColor));
//        rightBtn.setPadding(5, 5, 5, 5);
//        rightBtn.setGravity(Gravity.CENTER);

        LayoutParams btn1Params = ViewUtil
                .getLinearLayoutParams(1);
        btn1Params.setMargins(10, 10, 10, 10);
        btn1Params.width = 120;
        leftBtn.setLayoutParams(btn1Params);
//        rightBtn.setLayoutParams(btn1Params);

        // 按钮布局
        LayoutParams btnParams = ViewUtil.getLinearLayoutParams(4);
        btnParams.weight = 1;
        LinearLayout buttonLLl = ViewUtil.getLinLayout(context);
        buttonLLl.setGravity(Gravity.CENTER);
        buttonLLl.setLayoutParams(btnParams);
        buttonLLl.addView(leftBtn);

//        LinearLayout buttonRLl = ViewUtil.getLinLayout(context);
//        buttonRLl.setGravity(Gravity.CENTER);
//        buttonRLl.setLayoutParams(btnParams);
//        buttonRLl.addView(rightBtn);

        buttonLl.addView(buttonLLl);

//        LayoutParams footsepLp = ViewUtil.getLinearLayoutParams(3);
//        footsepLp.width = 2;
//        footsepLp.height = 110;
//        View footseprator = ViewUtil.getView(context, footsepLp);
//        footseprator.setBackgroundColor(Color.parseColor(attrs.sepLineColor));
//        buttonLl.addView(footseprator);

//        buttonLl.addView(buttonRLl);

        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click != null)
                    click.leftBtnClick(dlg);
            }
        });

//        rightBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (click != null)
//                    click.rightBtnClick(dlg);
//            }
//        });
        return buttonLl;
    }

    private static LinearLayout getBtnView(final Activity context,
                                           final AlertDialog dlg, String[] btnVal, final AlertButtonClick click) {
        LinearLayout buttonLl = ViewUtil.getLinLayout(context);
        buttonLl.setOrientation(LinearLayout.HORIZONTAL);
        buttonLl.setGravity(Gravity.CENTER);

        // int btnshape = ViewUtil.getDrawableRs(context, "shape_corner");
        // int btnshape2 = ViewUtil.getDrawableRs(context, "shape_corner2");
        TextView leftBtn = ViewUtil.getTextView(context, btnVal[0]);
        // leftBtn.setBackgroundResource(btnshape2);
        leftBtn.setTextColor(Color.BLACK);
        leftBtn.setPadding(0, 30, 0, 30);
        leftBtn.setGravity(Gravity.CENTER);

        TextView rightBtn = ViewUtil.getTextView(context, btnVal[1]);
        // rightBtn.setBackgroundResource(btnshape);
        rightBtn.setTextColor(Color.BLACK);
        rightBtn.setPadding(0, 30, 0, 30);
        rightBtn.setGravity(Gravity.CENTER);

        LayoutParams btn1Params = ViewUtil
                .getLinearLayoutParams(1);
        btn1Params.leftMargin = 20;
        btn1Params.rightMargin = 20;
        // btn1Params.width = 120;
        // btn1Params.setMargins(10, 10, 10, 10);
        // btn1Params.width = 120;
        leftBtn.setLayoutParams(btn1Params);
        rightBtn.setLayoutParams(btn1Params);

        LayoutParams btn_jx = new LayoutParams(2,
                LayoutParams.MATCH_PARENT);
        LinearLayout lin_jx = ViewUtil.getLinLayout(context, btn_jx);
        lin_jx.setBackgroundResource(ViewUtil.getColorRs(context, "ch_gray"));

        LayoutParams btnParams = ViewUtil.getLinearLayoutParams(4);
        btnParams.weight = 1;
        btnParams.leftMargin = 20;
        btnParams.rightMargin = 10;
        LinearLayout buttonLLl = ViewUtil.getLinLayout(context);
        buttonLLl.setGravity(Gravity.CENTER);
        buttonLLl.setLayoutParams(btnParams);
        buttonLLl.addView(leftBtn);
        // buttonLLl.setBackgroundResource(btnshape2);

        LayoutParams btnParams2 = ViewUtil
                .getLinearLayoutParams(4);
        btnParams2.weight = 1;
        btnParams2.leftMargin = 10;
        btnParams2.rightMargin = 20;
        LinearLayout buttonRLl = ViewUtil.getLinLayout(context);
        buttonRLl.setGravity(Gravity.CENTER);
        buttonRLl.setLayoutParams(btnParams2);

        buttonRLl.addView(rightBtn);
        // buttonRLl.setBackgroundResource(btnshape);

        buttonLl.addView(buttonLLl);
        buttonLl.addView(lin_jx);
        buttonLl.addView(buttonRLl);

        buttonLLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click != null)
                    click.leftBtnClick(dlg);
            }
        });

        buttonRLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click != null)
                    click.rightBtnClick(dlg);
            }
        });
        return buttonLl;
    }

    /**
     * 弹出框参数
     *
     * @author xc.li
     * @date 2016年1月30日
     */
    public static class DialogAttrs {
        /**
         * 弹出框的圆角弧度
         */
        public int dialogConnerSize = 5;
        /**
         * 弹出框颜色
         */
        public String dialogColor = "#FFFFFF";
        /**
         * 弹出框标题
         */
        public String title = "系统提示";
        /**
         * 弹出框标题颜色
         */
        public String titleColor = "#000000";
        /**
         * 弹出框标题位置，左侧、居中、靠右，Gravity
         */
        public int titleGravity = Gravity.START;
        /**
         * 消息体，当自定义了customView时，不做该消息体的展示
         */
        public String msg = "";
        /**
         * 文字颜色
         */
        public String textColor = "#000000";
        /**
         * 消息主题边框颜色
         */
        public String conerColor = "#FFFFFF";
        /**
         * 文字位置，左侧、居中、靠右，Gravity
         */
        public int textGravity = Gravity.START;
        /**
         * 按钮标签，数组长度为2，此属性为null时，将不展示下方的按钮
         */
        public String[] btnVal;
        /**
         * 左按钮的颜色
         */
        public String leftBtnColor = "#666666";
        /**
         * 右按钮的颜色
         */
        public String rightBtnColor = "#666666";
        /**
         * 按钮文字颜色
         */
        public String btnTextColor = "#3db4fa";
        /**
         * 按钮文字颜色
         */
        public String sepLineColor = "#FFDDDACF";
        /**
         * 自定义的消息体布局
         */
        public View customView;

        /**
         * 是否可触碰取消
         */
        public boolean isCancelable = true;
    }
}
