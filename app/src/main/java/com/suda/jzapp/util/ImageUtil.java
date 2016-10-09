package com.suda.jzapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ghbha on 2016/4/6.
 */
public class ImageUtil {

    public static File getPathByImageName(Context context, String imgName) {
        String path = context.getApplicationContext().getFilesDir().getAbsolutePath();
        File fp = new File(path);
        if (!fp.exists())
            fp.mkdir();
        return new File(path, imgName);
    }

    public static void saveBitmap(Context context, String imgName, Bitmap bm) {
        String path = context.getApplicationContext().getFilesDir().getAbsolutePath();
        File fp = new File(path);
        if (!fp.exists())
            fp.mkdir();

        File f = new File(path, imgName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapByImgName(Context context, String imgName) {
        String path = context.getApplicationContext().getFilesDir().getAbsolutePath();
        File f = new File(path, imgName);
        if (f.exists()) {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            // 这个isjustdecodebounds很重要
            opt.inJustDecodeBounds = true;
            Bitmap bm = null;
            bm = BitmapFactory.decodeFile(f.getAbsolutePath(), opt);
            // 获取到这个图片的原始宽度和高度
            int picWidth = opt.outWidth;
            int picHeight = opt.outHeight;

            // 获取屏的宽度和高度
            WindowManager windowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            int screenWidth = display.getWidth();
            int screenHeight = display.getHeight();

            // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
            opt.inSampleSize = 1;
            // 根据屏的大小和图片大小计算出缩放比例
            if (picWidth > picHeight) {
                if (picWidth > screenWidth)
                    opt.inSampleSize = picWidth / screenWidth;
            } else {
                if (picHeight > screenHeight)
                    opt.inSampleSize = picHeight / screenHeight;
            }
            // 这次再真正地生成一个有像素的，经过缩放了的bitmap
            opt.inJustDecodeBounds = false;
            if (bm != null && !bm.isRecycled())
                bm.recycle();

            bm = BitmapFactory.decodeFile(f.getAbsolutePath(), opt);
            return bm;
        }

        return null;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] image = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
