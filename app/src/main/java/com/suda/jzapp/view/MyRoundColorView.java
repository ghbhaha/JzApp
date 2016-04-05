package com.suda.jzapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.suda.jzapp.R;
import com.suda.jzapp.util.LogUtils;
import com.suda.jzapp.util.ThemeUtil;

/**
 * Created by suda on 16-1-9.
 */
public class MyRoundColorView extends ImageView {


    private int myRoundColor;
    private Drawable myRoundImage;

    private boolean changeColorWithTheme = false;

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
        myRoundImage = a.getDrawable(R.styleable.MyRoundColorView_myRoundImage);
        changeColorWithTheme = a.getBoolean(R.styleable.MyRoundColorView_changeColorWithTheme, false);
        mPaint = new Paint();
        mPaint.setColor((myRoundImage != null || changeColorWithTheme) ? context.getResources().getColor(ThemeUtil.getTheme(context).getMainColorID())
                : myRoundColor);

        a.recycle();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pointX = (int) canvas.getHeight() / 2;
        pointY = (int) canvas.getWidth() / 2;

        mPaint.setAntiAlias(true);
        canvas.drawCircle(pointX, pointY, pointX, mPaint);
        if (myRoundImage != null) {
            Bitmap bitmap = drawableToBitmap(myRoundImage);
            int size = bitmap.getHeight();
            canvas.drawBitmap(bitmap, pointX - size / 2, pointY - size / 2, mPaint);
        }

    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }
}
