package com.suda.jzapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.suda.jzapp.R;
import com.suda.jzapp.util.LogUtils;

/**
 * Created by suda on 16-1-9.
 */
public class MyRoundColorView extends ImageView {


    private int myRoundColor;

    private int myRoundSize;

    private Paint mPaint;
    private int pointX;
    private int pointY;

    public MyRoundColorView(Context context) {
        super(context);
    }

    public MyRoundColorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyRoundColorView);

        myRoundColor = a.getColor(R.styleable.MyRoundColorView_myRoundColor, Color.LTGRAY);
        myRoundSize = a.getInt(R.styleable.MyRoundColorView_myRoundSize, 46);

        mPaint = new Paint();
        mPaint.setColor(myRoundColor);
        a.recycle();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pointX = (int) canvas.getHeight() / 2;
        pointY = (int) canvas.getWidth() / 2;

        mPaint.setAntiAlias(true);

        canvas.drawCircle(pointX, pointY, myRoundSize, mPaint);

    }
}
