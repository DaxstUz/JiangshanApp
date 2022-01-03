package com.jiangshan.knowledge.uitl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.jiangshan.knowledge.activity.home.AnswerActivity;

/**
 * auth s_yz  2022/1/3
 */
public class FloatingWindowUtils {

    private Context context;
    private int screenWidth;
    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;
    private View floatView;

    private FloatingWindowUtils() {

    }

    private static class InstanceHolder {

        @SuppressLint("StaticFieldLeak")

        private static final FloatingWindowUtils sInstance = new FloatingWindowUtils();

        private InstanceHolder() {
        }

    }

    public static FloatingWindowUtils getInstance() {
        return FloatingWindowUtils.InstanceHolder.sInstance;
    }

    public void init(Context context) {
        this.context = context;
        if (windowManager != null) return;

        // 获取WindowManager服务
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //获取屏宽
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
//        screenWidth = DensityUtils.getScreenSize(false).x;
    }

    /**
     * 展示悬浮窗
     *
     * @param layoutId 悬浮窗布局文件id
     */

    @SuppressLint("RtlHardcoded")

    public void showFloatingWindow(@LayoutRes int layoutId) {
        // 新建悬浮窗控件
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        // View floatView = layoutInflater.inflate(R.layout.floating_view, null);
        View floatView = layoutInflater.inflate(layoutId, null);

        if (floatView == null) {
            throw new NullPointerException("悬浮窗view为null 检查布局文件是否可用");
        }
        showFloatingWindow(floatView);
    }

    /**
     * 展示悬浮窗
     *
     * @param floatView 悬浮窗view
     */
    @SuppressLint("RtlHardcoded")
    public void showFloatingWindow(@NonNull View floatView) {
        if (this.floatView != null) return;//有悬浮窗在显示 不再显示新的悬浮窗
        // 新建悬浮窗控件
        if (floatView == null) {
            throw new NullPointerException("悬浮窗view为null 确认view不为null");
        }

        this.floatView = floatView;
        //设置触摸事件
        floatView.setOnTouchListener(new FloatingOnTouchListener());
        //悬浮窗设置点击事件
        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "点击了悬浮窗", Toast.LENGTH_SHORT).show();
                boolean showAnalysis = ((AnswerActivity)context).getIntent().getBooleanExtra("showAnalysis", false);
                ((AnswerActivity)context).getIntent().putExtra("showAnalysis", !showAnalysis);
                ((AnswerActivity)context).getAnswer().notifyDataSetChanged();
            }
        });

        // 设置LayoutParam
        layoutParams = new WindowManager.LayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        layoutParams.gravity = Gravity.RIGHT;
        //设置flags 不然悬浮窗出来后整个屏幕都无法获取焦点，
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.x = 0;
        layoutParams.y = 100;

        // 将悬浮窗控件添加到WindowManager
        windowManager.addView(floatView, layoutParams);
    }

    /**
     * 隐藏悬浮窗
     */
    public void hideFloatWindow() {
        if (floatView != null) {
            windowManager.removeViewImmediate(floatView);
            floatView = null;
        }
    }

    public void unInit() {
        hideFloatWindow();
        this.context = null;
        // 获取WindowManager服务
        windowManager = null;
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;
        //标记是否执行move事件 如果执行了move事件 在up事件的时候判断悬浮窗的位置让悬浮窗处于屏幕左边或者右边
        private boolean isScroll;
        //标记悬浮窗口是否移动了 防止设置点击事件的时候 窗口移动松手后触发点击事件
        private boolean isMoved;

        //事件开始时和结束的时候 X和Y坐标位置
        private int startX;
        private int startY;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    isMoved = false;
                    isScroll = false;
                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;

                    // 更新悬浮窗控件布局
                    windowManager.updateViewLayout(view, layoutParams);
                    isScroll = true;
                    break;
                case MotionEvent.ACTION_UP:
                    int stopX = (int) event.getRawX();
                    int stopY = (int) event.getRawY();
                    if (Math.abs(startX - stopX) >= 1 || Math.abs(startY - stopY) >= 1) {
                        isMoved = true;
                    }
                    if (isScroll) {
                        autoView(view);
                    }
                    break;
            }
            return isMoved;
        }

        //悬浮窗view自动停靠在屏幕左边或者右边
        private void autoView(View view) {
            // 得到view在屏幕中的位置
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            if (location[0] > 0) {
                layoutParams.x = 0;
            } else {
                DisplayMetrics dm = new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(dm);
                screenWidth = dm.widthPixels;
                layoutParams.x = screenWidth - view.getWidth();
//            layoutParams.x = DensityUtils.getScreenSize(false).x - view.getWidth();
            }
            windowManager.updateViewLayout(view, layoutParams);
        }
    }

    public View getFloatView() {
        return floatView;
    }
}


