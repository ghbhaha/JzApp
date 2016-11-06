package com.suda.jzapp.view.gesturelockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.suda.jzapp.R;
import com.suda.jzapp.util.MD5Util;
import com.suda.jzapp.util.ThemeUtil;
import com.suda.jzapp.view.gesturelockview.GestureLockView.Mode;

import java.util.ArrayList;
import java.util.List;

/**
 * 整体包含n*n个GestureLockView,每个GestureLockView间间隔mMarginBetweenLockView，
 * 最外层的GestureLockView与容器存在mMarginBetweenLockView的外边距
 * <p/>
 * 关于GestureLockView的边长（n*n）： n * mGestureLockViewWidth + ( n + 1 ) *
 * mMarginBetweenLockView = mWidth ; 得：mGestureLockViewWidth = 4 * mWidth / ( 5
 * * mCount + 1 ) 注：mMarginBetweenLockView = mGestureLockViewWidth * 0.25 ;
 *
 * @author zhy
 */
public class GestureLockViewGroup extends RelativeLayout {

    private static final String TAG = "GestureLockViewGroup";
    /**
     * 保存所有的GestureLockView
     */
    private GestureLockView[] mGestureLockViews;
    /**
     * 每个边上的GestureLockView的个数
     */
    private int mCount = 4;
    /**
     * 存储答案
     */
    private String mAnswer = "";
    /**
     * 保存用户选中的GestureLockView的id
     */
    private List<Integer> mChoose = new ArrayList<Integer>();

    private Paint mPaint;
    /**
     * 每个GestureLockView中间的间距 设置为：mGestureLockViewWidth * 25%
     */
    private int mMarginBetweenLockView = 30;
    /**
     * GestureLockView的边长 4 * mWidth / ( 5 * mCount + 1 )
     */
    private int mGestureLockViewWidth;

    /**
     * GestureLockView无手指触摸的状态下内圆的颜色
     */
    private int mNoFingerInnerCircleColor = 0xFF939090;
    /**
     * GestureLockView无手指触摸的状态下外圆的颜色
     */
    private int mNoFingerOuterCircleColor = 0xFFE0DBDB;
    /**
     * GestureLockView手指触摸的状态下内圆和外圆的颜色
     */
    private int mFingerOnColor = 0xFF378FC9;
    /**
     * GestureLockView手指抬起的状态下内圆和外圆的颜色
     */
    private int mFingerUpColor = 0xFFFF0000;

    /**
     * 宽度
     */
    private int mWidth;
    /**
     * 高度
     */
    private int mHeight;

    private Path mPath;
    /**
     * 指引线的开始位置x
     */
    private int mLastPathX;
    /**
     * 指引线的开始位置y
     */
    private int mLastPathY;
    /**
     * 指引下的结束位置
     */
    private Point mTmpTarget = new Point();

    /**
     * 最大尝试次数
     */
    private int mTryTimes = 4;

    /**
     * 设置密码状态
     */
    private boolean isSetting = false;

    private Context mContext;

    /**
     * 回调接口
     */
    private OnGestureLockViewListener mOnGestureLockViewListener;

    public GestureLockViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLockViewGroup(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);

        this.mContext = context;
        /**
         * 获得所有自定义的参数的值
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.GestureLockViewGroup, defStyle, 0);
        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.GestureLockViewGroup_color_no_finger_inner_circle:
                    mNoFingerInnerCircleColor = a.getColor(attr,
                            mNoFingerInnerCircleColor);
                    break;
                case R.styleable.GestureLockViewGroup_color_no_finger_outer_circle:
                    mNoFingerOuterCircleColor = a.getColor(attr,
                            mNoFingerOuterCircleColor);
                    break;
                case R.styleable.GestureLockViewGroup_color_finger_on:
                    mFingerOnColor = a.getColor(attr, mFingerOnColor);
                    break;
                case R.styleable.GestureLockViewGroup_color_finger_up:
                    mFingerUpColor = a.getColor(attr, mFingerUpColor);
                    break;
                case R.styleable.GestureLockViewGroup_count:
                    mCount = a.getInt(attr, 3);
                    break;
                case R.styleable.GestureLockViewGroup_tryTimes:
                    mTryTimes = a.getInt(attr, 5);
                default:
                    break;
            }
        }

        a.recycle();

        // 初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        // mPaint.setStrokeWidth(20);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        // mPaint.setColor(Color.parseColor("#aaffffff"));
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        // Log.e(TAG, mWidth + "");
        // Log.e(TAG, mHeight + "");

        mHeight = mWidth = mWidth < mHeight ? mWidth : mHeight;

        // setMeasuredDimension(mWidth, mHeight);

        // 初始化mGestureLockViews
        if (mGestureLockViews == null) {
            mGestureLockViews = new GestureLockView[mCount * mCount];
            // 计算每个GestureLockView的宽度
            mGestureLockViewWidth = (int) (4 * mWidth * 1.0f / (5 * mCount + 1));
            //计算每个GestureLockView的间距
            mMarginBetweenLockView = (int) (mGestureLockViewWidth * 0.25);
            // 设置画笔的宽度为GestureLockView的内圆直径稍微小点（不喜欢的话，随便设）
            mPaint.setStrokeWidth(mGestureLockViewWidth * 0.29f);

            for (int i = 0; i < mGestureLockViews.length; i++) {
                //初始化每个GestureLockView
                mGestureLockViews[i] = new GestureLockView(getContext(),
                        mNoFingerInnerCircleColor, mNoFingerOuterCircleColor,
                        mContext.getResources().getColor(ThemeUtil.getTheme(mContext).getMainColorID()), mFingerUpColor);
                mGestureLockViews[i].setId(i + 1);
                //设置参数，主要是定位GestureLockView间的位置
                LayoutParams lockerParams = new LayoutParams(
                        mGestureLockViewWidth, mGestureLockViewWidth);

                // 不是每行的第一个，则设置位置为前一个的右边
                if (i % mCount != 0) {
                    lockerParams.addRule(RelativeLayout.RIGHT_OF,
                            mGestureLockViews[i - 1].getId());
                }
                // 从第二行开始，设置为上一行同一位置View的下面
                if (i > mCount - 1) {
                    lockerParams.addRule(RelativeLayout.BELOW,
                            mGestureLockViews[i - mCount].getId());
                }
                //设置右下左上的边距
                int rightMargin = mMarginBetweenLockView;
                int bottomMargin = mMarginBetweenLockView;
                int leftMagin = 0;
                int topMargin = 0;
                /**
                 * 每个View都有右外边距和底外边距 第一行的有上外边距 第一列的有左外边距
                 */
                if (i >= 0 && i < mCount)// 第一行
                {
                    topMargin = mMarginBetweenLockView;
                }
                if (i % mCount == 0)// 第一列
                {
                    leftMagin = mMarginBetweenLockView;
                }

                lockerParams.setMargins(leftMagin, topMargin, rightMargin,
                        bottomMargin);
                mGestureLockViews[i].setMode(Mode.STATUS_NO_FINGER);
                addView(mGestureLockViews[i], lockerParams);
            }

            Log.e(TAG, "mWidth = " + mWidth + " ,  mGestureViewWidth = "
                    + mGestureLockViewWidth + " , mMarginBetweenLockView = "
                    + mMarginBetweenLockView);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 重置
                reset();
                break;
            case MotionEvent.ACTION_MOVE:
                mPaint.setColor(mFingerOnColor);
                mPaint.setAlpha(50);
                GestureLockView child = getChildIdByPos(x, y);
                if (child != null) {
                    int cId = child.getId();
                    if (!mChoose.contains(cId)) {
                        child.setMode(Mode.STATUS_FINGER_ON);
                        if (mOnGestureLockViewListener != null)
                            mOnGestureLockViewListener.onBlockSelected(cId);
                        // 设置指引线的起点
                        mLastPathX = child.getLeft() / 2 + child.getRight() / 2;
                        mLastPathY = child.getTop() / 2 + child.getBottom() / 2;

                        if (mChoose.size() == 0)// 当前添加为第一个
                        {
                            mPath.moveTo(mLastPathX, mLastPathY);
                        } else
                        // 非第一个，将两者使用线连上
                        {
                            //判断经过点
                            //求二维数组坐标
                            int before = mChoose.get(mChoose.size() - 1);
                            int x1 = (before - 1) / mCount;
                            int y1 = (before - 1) % mCount;
                            int x2 = (cId - 1) / mCount;
                            int y2 = (cId - 1) % mCount;
                            //三种情况 一条直线 一条竖线 45度角
                            int xP = x1;
                            int yP = y1;
                            int startX = x1 - x2 < 0 ? x1 : x2;
                            int endX = x1 - x2 < 0 ? x2 : x1;
                            int startY = y1 - y2 < 0 ? y1 : y2;
                            int endY = y1 - y2 < 0 ? y2 : y1;
                            if (x1 == x2) {
                                for (int i = startY + 1; i < endY; i++) {
                                    yP = yP + (y1 - y2 > 0 ? -1 : 1);
                                    addPoint(xP, yP);
                                }
                            } else if (y1 == y2) {
                                for (int i = startX + 1; i < endX; i++) {
                                    xP = xP + (x1 - x2 > 0 ? -1 : 1);
                                    addPoint(xP, yP);
                                }
                            } else if ((Math.abs(y1 - y2) == Math.abs(x1 - x2))) {
                                for (int i = startX + 1; i < endX; i++) {
                                    xP = xP + (x1 - x2 > 0 ? -1 : 1);
                                    yP = yP + (y1 - y2 > 0 ? -1 : 1);
                                    addPoint(xP, yP);
                                }
                            }
                            mPath.lineTo(mLastPathX, mLastPathY);
                        }
                        mChoose.add(cId);
                    }
                }
                // 指引线的终点
                mTmpTarget.x = x;
                mTmpTarget.y = y;
                break;
            case MotionEvent.ACTION_UP:

                mPaint.setColor(mFingerUpColor);
                mPaint.setAlpha(50);
                this.mTryTimes--;

                // 回调是否成功
                if (mOnGestureLockViewListener != null && mChoose.size() > 0) {
                    if (isSetting) {
                        if (mChoose.size() < 4) {
                            mOnGestureLockViewListener.onUnmatchedExceedBoundary(CODE_IN_VALID);
                        } else {
                            mOnGestureLockViewListener.showSelect(getSelect());
                        }
                    } else {
                        if (this.mTryTimes == 0 && !checkAnswer()) {
                            mOnGestureLockViewListener.onUnmatchedExceedBoundary(CODE_NOT_SAME);
                        } else {
                            if (!checkAnswer()) {
                                // 改变子元素的状态为UP
                                changeItemMode();
                            }
                            mOnGestureLockViewListener.onGestureEvent(checkAnswer());
                        }
                    }
                }

                //Log.e(TAG, "mUnMatchExceedBoundary = " + mTryTimes);
                //Log.e(TAG, "mChoose = " + mChoose);
                // 将终点设置位置为起点，即取消指引线
                mTmpTarget.x = mLastPathX;
                mTmpTarget.y = mLastPathY;


                // 计算每个元素中箭头需要旋转的角度
                for (int i = 0; i + 1 < mChoose.size(); i++) {
                    int childId = mChoose.get(i);
                    int nextChildId = mChoose.get(i + 1);

                    GestureLockView startChild = (GestureLockView) findViewById(childId);
                    GestureLockView nextChild = (GestureLockView) findViewById(nextChildId);

                    int dx = nextChild.getLeft() - startChild.getLeft();
                    int dy = nextChild.getTop() - startChild.getTop();
                    // 计算角度
                    int angle = (int) Math.toDegrees(Math.atan2(dy, dx)) + 90;
                    startChild.setArrowDegree(angle);
                }
                break;

        }
        invalidate();
        return true;
    }

    private void addPoint(int x, int y) {
        int point = x * mCount + y + 1;
        if (!mChoose.contains(point)) {
            mChoose.add(point);
            mGestureLockViews[point - 1].setMode(Mode.STATUS_FINGER_ON);
            if (mOnGestureLockViewListener != null)
                mOnGestureLockViewListener.onBlockSelected(point);
        }
    }

    private void changeItemMode() {
        for (GestureLockView gestureLockView : mGestureLockViews) {
            if (mChoose.contains(gestureLockView.getId())) {
                gestureLockView.setMode(Mode.STATUS_FINGER_UP);
            }
        }
    }

    /**
     * 做一些必要的重置
     */
    private void reset() {
        mChoose.clear();
        mPath.reset();
        for (GestureLockView gestureLockView : mGestureLockViews) {
            gestureLockView.setMode(Mode.STATUS_NO_FINGER);
            gestureLockView.setArrowDegree(-1);
        }
    }

    /**
     * 检查用户绘制的手势是否正确
     *
     * @return
     */
    private boolean checkAnswer() {
        return getSelect().equals(mAnswer);
    }

    private String getSelect() {
        String choose = "";
        for (int i = 0; i < mChoose.size(); i++) {
            choose += mChoose.get(i);
        }
        return MD5Util.getMD5(choose);
    }

    /**
     * 检查当前左边是否在child中
     *
     * @param child
     * @param x
     * @param y
     * @return
     */
    private boolean checkPositionInChild(View child, int x, int y) {

        //设置了内边距，即x,y必须落入下GestureLockView的内部中间的小区域中，可以通过调整padding使得x,y落入范围不变大，或者不设置padding
        int padding = (int) (mGestureLockViewWidth * 0.15);

        if (x >= child.getLeft() + padding && x <= child.getRight() - padding
                && y >= child.getTop() + padding
                && y <= child.getBottom() - padding) {
            return true;
        }
        return false;
    }

    /**
     * 通过x,y获得落入的GestureLockView
     *
     * @param x
     * @param y
     * @return
     */
    private GestureLockView getChildIdByPos(int x, int y) {
        for (GestureLockView gestureLockView : mGestureLockViews) {
            if (checkPositionInChild(gestureLockView, x, y)) {
                return gestureLockView;
            }
        }

        return null;

    }

    /**
     * 设置回调接口
     *
     * @param listener
     */
    public void setOnGestureLockViewListener(OnGestureLockViewListener listener) {
        this.mOnGestureLockViewListener = listener;
    }

    /**
     * 对外公布设置答案的方法
     *
     * @param answer
     */
    public void setAnswer(String answer) {
        this.mAnswer = answer;
    }

    /**
     * 设置最大实验次数
     *
     * @param boundary
     */
    public void setUnMatchExceedBoundary(int boundary) {
        this.mTryTimes = boundary;
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //绘制GestureLockView间的连线
        if (mPath != null) {
            canvas.drawPath(mPath, mPaint);
        }
        //绘制指引线
        if (mChoose.size() > 0) {
            if (mLastPathX != 0 && mLastPathY != 0)
                canvas.drawLine(mLastPathX, mLastPathY, mTmpTarget.x,
                        mTmpTarget.y, mPaint);
        }

    }

    public void setIsSetting(boolean isSetting) {
        this.isSetting = isSetting;
    }

    public interface OnGestureLockViewListener {
        /**
         * 单独选中元素的Id
         *
         * @param
         */
        public void onBlockSelected(int cId);

        /**
         * 是否匹配
         *
         * @param matched
         */
        public void onGestureEvent(boolean matched);

        /**
         * 超过尝试次数
         */
        public void onUnmatchedExceedBoundary(int errorCode);

        /**
         * @param value
         */
        public void showSelect(String value);
    }

    public static final int CODE_NOT_SAME = 0;
    public static final int CODE_IN_VALID = 1;

}
