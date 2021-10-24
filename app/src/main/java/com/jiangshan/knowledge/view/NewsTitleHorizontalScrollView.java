package com.jiangshan.knowledge.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiangshan.knowledge.R;

import java.util.ArrayList;
import java.util.List;

public class NewsTitleHorizontalScrollView extends HorizontalScrollView implements View.OnClickListener{
    private LayoutInflater inflater;//加载布局进来用的
    private View view;//布局View
    private LinearLayout titleAllTxt;//标题布局
    private List<TextView> titleScroll = new ArrayList<>();//标题数组
    private ImageView iv_nav_indicator;//下划线
    private static int mLastPosition = 0;//当前选中标题索引
    private OnItemClickListener mOnClickListener;//监听函数

    private boolean needScroll=true;
    @Override
    public void onClick(View v) {
        needScroll=false;
        mOnClickListener.onClick(titleAllTxt.indexOfChild(v));
    }

    public interface OnItemClickListener {
        void onClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }


    public NewsTitleHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    /**
     * 初始化加载进来的布局
     */
    private void initView() {
        this.inflater = LayoutInflater.from(getContext());
        view = this.inflater.inflate(R.layout.news_title_horizontalscrollview_layout, null);
        this.titleAllTxt = (LinearLayout) view.findViewById(R.id.news_title_horizontalscrollview_titletxt_layout);
//        for (int i = 0; i < this.titleAllTxt.getChildCount(); i++) {
//            this.titleScroll.add((TextView) this.titleAllTxt.getChildAt(i));
//            ((TextView) this.titleAllTxt.getChildAt(i)).setOnClickListener(this);
//        }
        this.iv_nav_indicator = (ImageView) view.findViewById(R.id.iv_nav_indicator);

        View titleView=inflater.inflate(R.layout.item_title,null);
        TextView textView= titleView.findViewById(R.id.tv_item_title);

        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        textView.measure(w, h);
        LinearLayout.LayoutParams imageParams = (LinearLayout.LayoutParams) this.iv_nav_indicator.getLayoutParams();
        imageParams.width = textView.getMeasuredWidth();
        imageParams.height = 5;
        this.iv_nav_indicator.setLayoutParams(imageParams);
        addView(view);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }


    /**
     * viewPager跳转之下划线联动方法
     * @param position 跳转后的索引
     */
    public void setPagerChangeListenerToTextView(final int position) {
        int scrollStartX = 0;//动画其实位置
        int scrollEndX = 0;//动画结束位置
        for (int i = 0; i < this.titleScroll.size(); i++) {//循环是为了设置其他标题为黑色
            if (i == position) {
                if (mLastPosition < i) {//判断滑动方向,里面为左向右
                    for (int j = 0; j < mLastPosition; j++) {
//                        scrollStartX += this.titleScroll.get(j).getWidth() + ApplyUtils.dip2px(getContext(), 20);
                        scrollStartX += this.titleScroll.get(j).getWidth();
                    }
                    for (int j=0;j<i;j++){
//                        scrollEndX=scrollEndX + this.titleScroll.get(j).getWidth() + ApplyUtils.dip2px(getContext(), 20);
                        scrollEndX=scrollEndX + this.titleScroll.get(j).getWidth();
                    }
                    Log.i("liyuanjinglyj", "scrollStartX=" + String.valueOf(scrollStartX) + "----scrollEndX" + String.valueOf(scrollEndX));
                    slideview(scrollStartX, scrollEndX);
                } else {//判断滑动方向,里面为右向左
                    for (int j = 0; j <mLastPosition; j++) {
//                        scrollStartX += this.titleScroll.get(j).getWidth() + ApplyUtils.dip2px(getContext(), 20);
                        scrollStartX += this.titleScroll.get(j).getWidth();
                    }
                    for (int j=0;j<i;j++){
//                        scrollEndX = scrollEndX + this.titleScroll.get(j).getWidth() + ApplyUtils.dip2px(getContext(), 20);
                        scrollEndX = scrollEndX + this.titleScroll.get(j).getWidth();
                    }
                    Log.i("liyuanjinglyjfanxiang", "scrollStartX=" + String.valueOf(scrollStartX) + "----scrollEndX" + String.valueOf(scrollEndX));
                    slideview(scrollStartX, scrollEndX);
                }
                LinearLayout.LayoutParams imageParams = (LinearLayout.LayoutParams) this.iv_nav_indicator.getLayoutParams();
//                imageParams.width = this.titleScroll.get(i).getWidth() + ApplyUtils.dip2px(getContext(), 20);
                imageParams.width = this.titleScroll.get(i).getWidth();
                imageParams.height = 5;
                this.iv_nav_indicator.setLayoutParams(imageParams);
//                this.titleScroll.get(i).setTextColor(Color.RED);
                this.titleScroll.get(i).setTextColor(getResources().getColor(R.color.b1));
                this.mLastPosition = position;//设置当前的标题索引
            } else {
//                this.titleScroll.get(i).setTextColor(Color.BLACK);
                this.titleScroll.get(i).setTextColor(getResources().getColor(R.color.b3));
            }
        }


        if(needScroll){
            new Handler().post(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(10);

//                    int left = titleAllTxt.getMeasuredWidth() - getWidth();
//                    int left = titleScroll.get(position).getLeft() - getWidth();
                        int left = titleScroll.get(mLastPosition).getLeft()+ titleScroll.get(mLastPosition).getWidth() - getWidth();
//                    int left = titleScroll.get(position).getLeft();
                        if (left < 0) {
//                        left = 0;
                        }else{
                            left= titleScroll.get(mLastPosition).getLeft()+titleScroll.get(mLastPosition).getWidth()- getWidth();
                        }
                        scrollTo(left, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        needScroll=true;

    }

    /**
     * 滑动动画
     * @param p1 起始
     * @param p2 终点
     */
    public void slideview(float p1, float p2) {
        TranslateAnimation animation = new TranslateAnimation(p1, p2, 0, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(300);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        iv_nav_indicator.startAnimation(animation);

    }

    /**
     * 动态添加标题
     * @param text 标题文字
     * @param context 上下文
     */
    public void addTextViewTitle(String text, Context context) {

        View titleView=inflater.inflate(R.layout.item_title,null);
        TextView textView= titleView.findViewById(R.id.tv_item_title);

//        TextView textView = new TextView(context);
        textView.setText(text);
//        textView.setTextColor(Color.BLACK);
//        textView.setClickable(true);
//        textView.setTextSize(20.0f);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.setMargins(10, 0, 10, 0);
//        textView.setLayoutParams(params);
//        textView.setOnClickListener(this);
        textView.setOnClickListener(this);
        this.titleAllTxt.addView(textView);
        this.titleScroll.add(textView);

        if(this.titleAllTxt.getChildCount()==1){
            textView.setTextColor(Color.WHITE);
        }else{
            textView.setTextColor(getResources().getColor(R.color.b3));
        }



//        new Handler().post(new Runnable() {
//            public void run() {
//                try {
//                    Thread.sleep(10);
//                    int left = titleAllTxt.getMeasuredWidth() - getWidth();
//                    if (left < 0) {
//                        left = 0;
//                    }
//                    scrollTo(left, 0);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

}
