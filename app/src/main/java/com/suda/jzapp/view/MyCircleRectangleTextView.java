package com.suda.jzapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import com.suda.jzapp.R;
import com.suda.jzapp.util.ThemeUtil;

/**
 * Created by ghbha on 2016/4/13.
 */
public class MyCircleRectangleTextView extends TextView {

    private Paint mPaint;

    private int width = 0;

    public MyCircleRectangleTextView(Context context) {
        super(context);
    }

    public MyCircleRectangleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyRoundColorView);
        mPaint = new Paint();
        mPaint.setColor(context.getResources().getColor(ThemeUtil.getTheme(context).getMainColorID()));
        mPaint.setAntiAlias(true);
        a.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int textSize = (int) getTextSize() + 2;
        setPadding(textSize / 2, textSize / 4, textSize / 2, textSize / 4);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        int pointX1 = height / 2;
        int pointX2 = width - height / 2;
        int pointY = height / 2;
        int recLength = width - height;

        canvas.drawCircle(pointX1, pointY, pointY, mPaint);
        canvas.drawCircle(pointX2, pointY, pointY, mPaint);
        canvas.drawRect(pointY, 0, pointY + recLength, pointY * 2, mPaint);

        Rect targetRect = new Rect(pointY, 0, pointY + recLength, pointY * 2);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(getTextSize());
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        canvas.drawText(getText().toString(), targetRect.centerX(), baseline, paint);
    }


}
