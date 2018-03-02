package com.jointem.base.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Description:文件、图片及流操作工具
 * Created by Kevin.Li on 2017/4/14.
 */
public final class FileOperateUtil {
    /**
     * 将字节数组输出流转换为Base64字符串
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 16:47
     */
    public static String changeImageToBase64(ByteArrayOutputStream stream) {

        byte[] bytes = null;
        try {
            bytes = new byte[stream.size()];
            bytes = stream.toByteArray();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 将Base64字符串转换为位图
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 16:47
     */
    public static Bitmap changeBase64ToImage(String strImg) {
        Bitmap bp = null;
        if (strImg == null || "".equals(strImg))
            return null;
        try {
            byte[] data;
            data = Base64.decode(strImg, Base64.DEFAULT);
            for (int i = 0; i < data.length; i++) {
                if (data[i] < 0) {
                    data[i] += 256;
                }
            }
            bp = BitmapFactory.decodeByteArray(data, 0, data.length);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bp;
    }

    /**
     * 根据路径获得图片并压缩，返回bitmap用于显示
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 16:47
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片的缩放值
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 16:48
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 将图片按比例进行压缩, 并返回由图片转换成Base64编码的字符串
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 16:49
     */
    public static String compressPhotoAndGetBase64String(String filePath) {

        Bitmap bm = getSmallBitmap(filePath);
        // 101539b =99kb sd卡中368KB
        // saveMyBitmap(bm);
        return compressBitmapAndGetBase64String(bm);
    }

    /**
     * 将缩略图按比例进行压缩, 返回由图片转换成Base64编码的字符串
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 16:50
     */
    public static String compressBitmapAndGetBase64String(Bitmap bitmap) {
        bitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 50 && options > 0) {// 循环判断如果压缩后图片字节流大于80kb，继续压缩
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            if (options > 10) {
                options -= 10;
            } else {
                options = 80;
            }
        }
        // 101539b =99kb sd卡中368KB
        // saveMyBitmap(bm);
        return changeImageToBase64(baos);
    }

    /**
     * 将压缩后的图片保存到sd卡的bptemp文件夹下
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 16:50
     */
    public static void saveMyBitmap(Bitmap bitmap, String fileName) {
        File dir = new File(Environment.getExternalStorageDirectory()
                + "/bptemp");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File imageFile = new File(dir, fileName + ".jpg");
        if (imageFile.exists()) {
            if (imageFile.isFile()) {
                imageFile.delete();// 先删掉原来的，在另外创建一张图片
                imageFile = new File(dir, fileName + ".jpg");
            }
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(imageFile);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)) {
                try {
                    fOut.flush();
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Drawable转换成Bitmap
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 16:54
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 获取文件的类型
     *
     * @param fileName 文件名
     *                 <p/>
     *                 author: Kevin.Li
     *                 created at 2017/4/14 17:06
     */
    public static String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    /**
     * 将位图转换为byte二进制流
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 17:06
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 将位图转换为InputStream输入流
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 17:07
     */
    public static InputStream bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = -1;
        byte[] buffer = new byte[1024];
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        is.close();
        return baos.toString();
    }
}
