package com.suda.jzapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ghbha on 2016/4/6.
 */
public class ImageUtil {
    public static void saveBitmap(String imgName, Bitmap bm) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/com.suda.jzapp";
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

    public static Bitmap getBitmapByImgName(String imgName) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/com.suda.jzapp";
        File f = new File(path, imgName);
        if (f.exists())
            return BitmapFactory.decodeFile(f.getAbsolutePath());
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
