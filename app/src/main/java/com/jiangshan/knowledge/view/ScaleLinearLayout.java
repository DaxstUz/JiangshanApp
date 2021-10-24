package com.jiangshan.knowledge.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.jiangshan.knowledge.R;

/**
 * 可调节长宽比例的LinearLayout，会自动根据宽度按比例调节高度
 *
 * @author sunzheng
 * @date 2013.10.15
 * @tips xmlns:etion="http://schemas.android.com/apk/res/com.xuanwu.etion"<br>
 * etion:aspectRatio="float"<br>
 * etion:aspectRatioBase="integer":<br>
 * 0=ASPECT_RATION_BASE_X, 1=ASPECT_RATION_BASE_Y<br>
 */
public class ScaleLinearLayout extends LinearLayout {
    /**
     * 以X为基准进行比例缩放
     */
    public static final int ASPECT_RATION_BASE_X = 0;
    /**
     * 以Y为基准进行比例缩放
     */
    public static final int ASPECT_RATION_BASE_Y = 1;

    private float aspectRatio = 0;
    private int aspectRatioBase = 0;

    public ScaleLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        readAttrs(context, attrs);
    }

    public ScaleLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttrs(context, attrs);
    }

    public ScaleLinearLayout(Context context) {
        super(context);
    }

    private void readAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EtionScale);
        aspectRatio = a.getFloat(R.styleable.EtionScale_aspectRatio, 0.0f);
        aspectRatioBase = a.getInteger(R.styleable.EtionScale_aspectRatioBase,
                ASPECT_RATION_BASE_X);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (aspectRatio <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));
        if (aspectRatioBase != ASPECT_RATION_BASE_Y) {
            int childWidthSize = getMeasuredWidth();
            if (aspectRatioBase < -2) { // 宽：高 = 5:2
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        (int) (childWidthSize * 2 / 5 / aspectRatio),
                        MeasureSpec.EXACTLY);
            } else if (aspectRatioBase < -1) { // 宽：高 = 2:1
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        (int) (childWidthSize * 1 / 2 / aspectRatio),
                        MeasureSpec.EXACTLY);
            } else if (aspectRatioBase < 0) { // 宽：高 = 1:1
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        (int) (childWidthSize / aspectRatio),
                        MeasureSpec.EXACTLY);
            } else if (aspectRatioBase < ASPECT_RATION_BASE_Y) { // 宽：高 =4:3
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        (int) (childWidthSize * 3 / 4 / aspectRatio),
                        MeasureSpec.EXACTLY);
            }
        } else {
            int childHeightSize = getMeasuredHeight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    (int) (childHeightSize * aspectRatio), MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((childHeightSize),
                    MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    /**
     * 得到长宽比例默认为1：1，当设置比例小于0的时候，即不使用比例
     *
     * @return the aspectRatio
     */
    public float getAspectRatio() {
        return aspectRatio;
    }

    /**
     * 设置ImageView的长宽只比
     *
     * @param aspectRatio the aspectRatio to set
     */
    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        this.postInvalidate();
    }

    /**
     * @return the aspectRatioBase
     */
    public int getAspectRatioBase() {
        return aspectRatioBase;
    }

    /**
     * @param aspectRatioBase the aspectRatioBase to set
     */
    public void setAspectRatioBase(int aspectRatioBase) {
        this.aspectRatioBase = aspectRatioBase;
    }
}