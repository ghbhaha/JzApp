/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 bboyfeiyu@gmail.com ( mr.simple )
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.suda.jzapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.suda.jzapp.R;
import com.suda.jzapp.util.LogUtils;


/**
 * MaterialLayout是模拟Android 5.0中View被点击的波纹效果的布局，与其他的模拟Material
 * Desigin效果的View不同，所有在MaterialLayout布局下的子视图被点击时都会产生波纹效果,而不是某个特定的View才会有这样的效果.
 *
 * @author mrsimple
 */
public class MaterialLayout extends LinearLayout {

    private static final int DEFAULT_RADIUS = 10;
    private static final int DEFAULT_FRAME_RATE = 10;
    private static final int DEFAULT_DURATION = 100;
    private static final int DEFAULT_ALPHA = 255;
    private static final float DEFAULT_SCALE = 0.8f;
    private static final int DEFAULT_ALPHA_STEP = 5;

    /**
     * 动画帧率
     */
    private int mFrameRate = DEFAULT_FRAME_RATE;
    /**
     * 渐变动画持续时间
     */
    private int mDuration = DEFAULT_DURATION;
    /**
     *
     */
    private Paint mPaint = new Paint();
    /**
     * 被点击的视图的中心点
     */
    private Point mCenterPoint = null;
    /**
     * 视图的Rect
     */
    private RectF mTargetRectf;
    /**
     * 起始的圆形背景半径
     */
    private int mRadius = DEFAULT_RADIUS;
    /**
     * 最大的半径
     */
    private int mMaxRadius = DEFAULT_RADIUS;

    /**
     * 渐变的背景色
     */
    private int mCirclelColor = Color.LTGRAY;
    /**
     * 每次重绘时半径的增幅
     */
    private int mRadiusStep = 1;
    /**
     * 保存用户设置的alpha值
     */
    private int mBackupAlpha;

    /**
     * 圆形半径针对于被点击视图的缩放比例,默认为0.8
     */
    private float mCircleScale = DEFAULT_SCALE;
    /**
     * 颜色的alpha值, (0, 255)
     */
    private int mColorAlpha = DEFAULT_ALPHA;
    /**
     * 每次动画Alpha的渐变递减值
     */
    private int mAlphaStep = DEFAULT_ALPHA_STEP;

    private View mTargetView;

    /**
     * @param context
     */
    public MaterialLayout(Context context) {
        this(context, null);
    }

    public MaterialLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MaterialLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }

        if (attrs != null) {
            initTypedArray(context, attrs);
        }

        initPaint();

        this.setWillNotDraw(false);
        this.setDrawingCacheEnabled(true);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MaterialLayout);
        mCirclelColor = typedArray.getColor(R.styleable.MaterialLayout_custom_color, Color.LTGRAY);
        mDuration = typedArray.getInteger(R.styleable.MaterialLayout_duration,
                DEFAULT_DURATION);
        mFrameRate = typedArray
                .getInteger(R.styleable.MaterialLayout_framerate, DEFAULT_FRAME_RATE);
        mColorAlpha = typedArray.getInteger(R.styleable.MaterialLayout_alpha, DEFAULT_ALPHA);
        mCircleScale = typedArray.getFloat(R.styleable.MaterialLayout_scale, DEFAULT_SCALE);

        typedArray.recycle();

    }

    private void initPaint() {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCirclelColor);
        mPaint.setAlpha(mColorAlpha);

        // 备份alpha属性用于动画完成时重置
        mBackupAlpha = mColorAlpha;
    }

    /**
     * 点击的某个坐标点是否在View的内部
     *
     * @param touchView
     * @param x         被点击的x坐标
     * @param y         被点击的y坐标
     * @return 如果点击的坐标在该view内则返回true, 否则返回false
     */
    private boolean isInFrame(View touchView, float x, float y) {
        initViewRect(touchView);
        return mTargetRectf.contains(x, y);
    }

    /**
     * 获取点中的区域,屏幕绝对坐标值,这个高度值也包含了状态栏和标题栏高度
     *
     * @param touchView
     */
    private void initViewRect(View touchView) {
        int[] location = new int[2];
        touchView.getLocationOnScreen(location);
        // 视图的区域
        mTargetRectf = new RectF(location[0], location[1], location[0]
                + touchView.getWidth(), location[1] + touchView.getHeight());

    }

    /**
     * 减去状态栏和标题栏的高度
     */
    private void removeExtraHeight(int touchX, int touchY) {
        int[] location = new int[2];
        this.getLocationOnScreen(location);
        // 减去两个该布局的top,这个top值就是状态栏的高度
        mTargetRectf.top -= location[1];
        mTargetRectf.bottom -= location[1];

        // 计算中心点坐标
        int centerHorizontal = (int) (mTargetRectf.left + mTargetRectf.right) / 2;
        int centerVertical = (int) ((mTargetRectf.top + mTargetRectf.bottom) / 2);


      //  LogUtils.e("@@@@@@@@", "centerHorizontal" + centerHorizontal + "centerVertical" + centerVertical + "touchX" + touchX + "touchY" + touchY);


        // 获取中心点
        mCenterPoint = new Point(touchX, centerVertical);

    }

    private View findTargetView(ViewGroup viewGroup, float x, float y) {

        initViewRect(viewGroup);

        return viewGroup;
//
//        int childCount = viewGroup.getChildCount();
//        // 迭代查找被点击的目标视图
//        for (int i = 0; i < childCount; i++) {
//            View childView = viewGroup.getChildAt(i);
//            if (childView instanceof ViewGroup) {
//                return findTargetView((ViewGroup) childView, x, y);
//            } else if (isInFrame(childView, x, y)) { // 否则判断该点是否在该View的frame内
//                return childView;
//            }
//        }
//
//        return null;
    }

    private boolean isAnimEnd() {
        return mRadius >= mMaxRadius;
    }

    private void calculateMaxRadius(View view) {
        // 取视图的最长边
        int maxLength = Math.max(view.getWidth(), view.getHeight());
        // 计算Ripple圆形的半径
        mMaxRadius = (int) ((maxLength / 2) * mCircleScale);

        int redrawCount = mDuration / mFrameRate;
        // 计算每次动画半径的增值
        mRadiusStep = (mMaxRadius - DEFAULT_RADIUS) / redrawCount;
        // 计算每次alpha递减的值
        mAlphaStep = (mColorAlpha - 100) / redrawCount;
    }

    /**
     * 处理ACTION_DOWN触摸事件, 注意这里获取的是Raw x, y,
     * 即屏幕的绝对坐标,但是这个当屏幕中有状态栏和标题栏时就需要去掉这些高度,因此得到mTargetRectf后其高度需要减去该布局的top起点
     * ，也就是标题栏和状态栏的总高度.
     *
     * @param event
     */
    private void deliveryTouchDownEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mTargetView = findTargetView(this, event.getRawX(), event.getRawY());
            if (mTargetView != null) {
                removeExtraHeight((int) event.getRawX(), (int) event.getRawY());
                // 计算相关数据
                calculateMaxRadius(mTargetView);
                // 重绘视图
                invalidate();
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        deliveryTouchDownEvent(event);
        return super.onInterceptTouchEvent(event);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        // 绘制Circle
        drawRippleIfNecessary(canvas);
    }

    private void drawRippleIfNecessary(Canvas canvas) {
        if (isFoundTouchedSubView()) {
            // 计算新的半径和alpha值
            mRadius += mRadiusStep;
            mColorAlpha -= mAlphaStep;

            // 裁剪一块区域,这块区域就是被点击的View的区域.通过clipRect来获取这块区域,使得绘制操作只能在这个区域范围内的进行,
            // 即使绘制的内容大于这块区域,那么大于这块区域的绘制内容将不可见. 这样保证了背景层只能绘制在被点击的视图的区域
            canvas.clipRect(mTargetRectf);
            mPaint.setAlpha(mColorAlpha);
            // 绘制背景圆形,也就是
            canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius, mPaint);
        }

        if (isAnimEnd()) {
            reset();
        } else {
            invalidateDelayed();
        }
    }

    /**
     * 发送重绘消息
     */
    private void invalidateDelayed() {
        this.postDelayed(new Runnable() {

            @Override
            public void run() {
                invalidate();
            }
        }, mFrameRate);
    }

    /**
     * 判断是否找到被点击的子视图
     *
     * @return
     */
    private boolean isFoundTouchedSubView() {
        return mCenterPoint != null && mTargetView != null;
    }

    private void reset() {
        mCenterPoint = null;
        mTargetRectf = null;
        mRadius = DEFAULT_RADIUS;
        mColorAlpha = mBackupAlpha;
        mTargetView = null;
        invalidate();
    }

}
