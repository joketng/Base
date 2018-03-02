package com.jointem.base.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.target.Target;
import com.jointem.base.BaseApplication;
import com.jointem.base.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Description: 图片加载库的工具类
 * Date 2016/3/1 10:14
 */
public class ImageUtil {

    private static RoundedBitmapDrawable roundedBitmapDrawable, circleBitmapDrawable;

    //正在加载时的过渡图片，失败时展示此图片
    static {
        int defaultRectangleTransitImg = R.mipmap.ic_hgp_loading_rectangle;
        int defaultSquareTransitImg = R.mipmap.ic_hgp_loading;

        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(BaseApplication.getContextFromApplication().getResources(),
                BitmapFactory.decodeResource(BaseApplication.getContextFromApplication().getResources(), defaultRectangleTransitImg));
        roundedBitmapDrawable.setCornerRadius(UiUtil.dip2px(4));

        circleBitmapDrawable = RoundedBitmapDrawableFactory.create(BaseApplication.getContextFromApplication().getResources(),
                BitmapFactory.decodeResource(BaseApplication.getContextFromApplication().getResources(), defaultSquareTransitImg));
        circleBitmapDrawable.setCircular(true);
    }


    /**
     * 加载图片到相应的控件上
     *
     * @param context   直接使用Context将不受生命周期控制，使用Activity，Fragment将跟随当前Activity/Fragment的生命周期
     * @param uri       图片来源（包括http资源，SD卡资源， ContentProvider资源，assets资源）
     * @param imageView 展示图片的控件（ImageView）
     */
    public static void displayImage(Context context, String uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .placeholder(R.mipmap.ic_hgp_loading_rectangle)
                .error(R.mipmap.ic_hgp_loading_rectangle)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载图片到相应的控件上(圆形裁剪)
     *
     * @param context     直接使用Context将不受生命周期控制，使用Activity，Fragment将跟随当前Activity/Fragment的生命周期
     * @param uri         图片来源（包括http资源，SD卡资源， ContentProvider资源，assets资源）
     * @param imageView   展示图片的控件（ImageView）
     * @param strokeWidth 圆形边框的宽度 -1为默认值 1dp
     * @param strokeColor 圆形边框的颜色 -1为默认值 #d9d9d9
     */
    public static void displayCircleImage(Context context, String uri, ImageView imageView, int strokeColor, int strokeWidth) {
        Glide.with(context)
                .load(uri)
                .placeholder(circleBitmapDrawable)
                .error(circleBitmapDrawable)
                .crossFade()
                .bitmapTransform(new GlideCircleLineTransform(context, strokeWidth, strokeColor))
                .into(imageView);
    }


    /**
     * 加载图片到相应的控件上(圆形裁剪)
     *
     * @param context   直接使用Context将不受生命周期控制，使用Activity，Fragment将跟随当前Activity/Fragment的生命周期
     * @param uri       图片来源（包括http资源，SD卡资源， ContentProvider资源，assets资源）
     * @param imageView 展示图片的控件（ImageView）
     */
    public static void displayCircleImage(final Context context, String uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .placeholder(circleBitmapDrawable)
                .error(circleBitmapDrawable)
                .crossFade()
                .bitmapTransform(new CropCircleTransformation(context))
                .into(imageView);

//        Glide.with(context)
//                .load(uri)
//                .asBitmap()
//                .placeholder(circleBitmapDrawable)
//                .centerCrop()
//                .error(circleBitmapDrawable)
//                .animate(R.anim.drawable_alpha_in)
//                .into(new BitmapImageViewTarget(imageView) {
//                    @Override
//                    protected void setResource(Bitmap resource) {
//                        RoundedBitmapDrawable circularBitmapDrawable =
//                                RoundedBitmapDrawableFactory.create(HgpApplication.getContextFromApplication().getResources(),
//                                        resource);
//                        circularBitmapDrawable.setCircular(true);
//                        view.setImageDrawable(circularBitmapDrawable);
//                    }
//                });
    }


    /**
     * 加载图片到相应的控件上(圆角处理)
     *
     * @param context   直接使用Context将不受生命周期控制，使用Activity，Fragment将跟随当前Activity/Fragment的生命周期
     * @param uri       图片来源（包括http资源，SD卡资源， ContentProvider资源，assets资源）
     * @param imageView 展示图片的控件（ImageView）
     * @param radius    圆角的半径 默认圆角的半径为 4
     */
    public static void displayRoundCornerImage(final Context context, String uri, ImageView imageView, final int radius) {
        Glide.with(context)
                .load(uri)
                .placeholder(roundedBitmapDrawable)
                .crossFade()
                .error(roundedBitmapDrawable)
                .bitmapTransform(new GlideRoundTransform(context, radius))
                .into(imageView);
//
//        Glide.with(context)
//                .load(uri)
//                .asBitmap()
//                .placeholder(roundedBitmapDrawable)
//                .centerCrop()
//                .error(roundedBitmapDrawable)
//                .animate(R.anim.drawable_alpha_in)
//                .into(new BitmapImageViewTarget(imageView) {
//                    @Override
//                    protected void setResource(Bitmap resource) {
//                        RoundedBitmapDrawable circularBitmapDrawable =
//                                RoundedBitmapDrawableFactory.create(HgpApplication.getContextFromApplication().getResources(), resource);
//                        circularBitmapDrawable.setCornerRadius(radius);
//                        view.setImageDrawable(circularBitmapDrawable);
//                    }
//                });
    }

    /**
     * 加载图片到相应的控件上(圆角处理)
     *
     * @param context   直接使用Context将不受生命周期控制，使用Activity，Fragment将跟随当前Activity/Fragment的生命周期
     * @param uri       图片来源（包括http资源，SD卡资源， ContentProvider资源，assets资源）
     * @param imageView 展示图片的控件（ImageView）
     *                  radius 默认圆角的半径为 4
     */
    public static void displayRoundCornerImage(final Context context, String uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .placeholder(roundedBitmapDrawable)
                .crossFade()
                .error(roundedBitmapDrawable)
                .bitmapTransform(new GlideRoundTransform(context))
                .into(imageView);

//        Glide.with(context)
//                .load(uri)
//                .asBitmap()
//                .placeholder(roundedBitmapDrawable)
//                .centerCrop()
//                .error(roundedBitmapDrawable)
//                .animate(R.anim.drawable_alpha_in)
//                .into(new BitmapImageViewTarget(imageView) {
//                    @Override
//                    protected void setResource(Bitmap resource) {
//                        RoundedBitmapDrawable circularBitmapDrawable =
//                                RoundedBitmapDrawableFactory.create(HgpApplication.getContextFromApplication().getResources(), resource);
//                        circularBitmapDrawable.setCornerRadius(UiUtil.dip2px(4));
//                        view.setImageDrawable(circularBitmapDrawable);
//                    }
//                });
    }

    /**
     * 保存图片（imgUri）到本地
     */
    public static void saveImage(final Context context, final String imgUri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(context)
                            .load(imgUri)
                            .asBitmap()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if (bitmap != null) {
                    saveImageToGallery(context, bitmap);
                }
            }
        }).start();
        UiUtil.showToast(context, "文件已保存到" + CommonConstants.PICTURE_PATH + "目录下...");
    }

    /**
     * 保存图片（Bitmap）到本地
     */
    public static void saveImageToGallery(Context context, @NonNull Bitmap bmp) {
        File folder = new File(CommonConstants.PICTURE_PATH);
        if (!folder.exists()) {//判断是否已存在该文件夹
            folder.mkdirs();
        }
        String fileName = "hgp" + System.currentTimeMillis() + ".jpg";
        File file = new File(CommonConstants.PICTURE_PATH, fileName);
        if (!file.exists()) {//判断是否已存在该文件
            try {
                if (!file.createNewFile()) {
                    return;
                }
                FileOutputStream out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //将位图插入系统图库的方法
//        MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, fileName, "");

        // 最后通知图库更新
        Uri uri = Uri.fromFile(file);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }

    /**
     * 使用系统当前日期加以调整作为照片的名称
     *
     * @return eg. hgp_20170114_1036.jpg
     */
    public static String getPicName() {
        File folder = new File(CommonConstants.PICTURE_PATH);
        if (!folder.exists()) {//判断是否已存在该文件夹
            folder.mkdir();
        }
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'hgp'_yyyyMMdd_HHmmss", Locale.getDefault());
        return dateFormat.format(date) + ".jpg";
    }

    /**
     * 图片压缩（质量压缩方法——适用于压缩之后上传到服务器）
     * <p/>
     * 它是在保持像素的前提下改变图片的位深及透明度等，来达到压缩图片的目的。
     * 进过它压缩的图片文件大小会有改变，但是导入成bitmap后占得内存是不变的。
     * 因为要保持像素不变，所以它就无法无限压缩，到达一个值之后就不会继续变小了。
     */
    public static void compressBitmap(String picPath) throws IOException {
        //打开图片放到bitmap中
        FileInputStream fis = new FileInputStream(picPath);
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        fis.close();

        //压缩bitmap放到二进制数组输出流中
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os); //质量压缩方法,这里100表示不压缩,把压缩后的数据存放到os中
        int options = 100;
        while (os.toByteArray().length / 1024 > 100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            os.reset(); //重置os,即清空os
            //第一个参数:图片格式,第二个参数:图片质量,100为最高，0为最差,第三个参数:保存压缩后的数据的流
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, os);   //这里压缩(100-options)%，把压缩后的数据存放到os中
            if (options > 10) {
                options -= 10;
            } else {
                options = 80;
            }
        }

        //输出到文件流中
        FileOutputStream fos = new FileOutputStream(picPath);
        os.writeTo(fos);
        os.flush();
        os.close();
        fos.close();

//        byte[] bytes = os.toByteArray();
//        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        return bitmap;
    }


    /**
     * 获取Glide造成的缓存大小（默认是内存的缓存，经过四舍五入，保留两位小数而得）
     *
     * @return 缓存大小，单位为MB
     */
    public static String getCacheSize() {
//        BigDecimal bigCacheSize = new BigDecimal(getFolderSize(new File(HgpApplication.getContextFromApplication().getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));
        BigDecimal bigCacheSize = new BigDecimal(getFolderSize(new File(CommonConstants.PICTURE_PATH, "imgCache")));
        return bigCacheSize.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
    }

    /**
     * 获取指定文件夹的大小
     *
     * @param file 文件或者文件夹
     * @return 具体的大小，单位为MB
     */
    private static double getFolderSize(File file) {
        double size = 0.00;
        try {
            File[] fileList = file.listFiles();
            if (fileList == null || fileList.length == 0)
                return 0;
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size += getFolderSize(aFileList); //递归计算
                } else {
                    size += aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size / 1024 / 1024;
    }

    /**
     * 清空缓存
     */
    public static void clearCache() {
        final Context context = BaseApplication.getContextFromApplication();
        //判断是否在主线程
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(context).clearDiskCache();  //必须运行在子线程
                }
            }).start();
            Glide.get(context).clearMemory(); //必须运行在主线程
        } else {
            Glide.get(context).clearDiskCache();  //必须运行在子线程
        }

    }

    private static class GlideCircleLineTransform implements Transformation<Bitmap> {

        private BitmapPool mBitmapPool;
        private int strokeWidth = 5;
        private int strokeColor = Color.GRAY;
        private Paint boardPaint;
        private int dia;


        public GlideCircleLineTransform(Context context, int strokeWidth, int strokeColor) {
            this(Glide.get(context).getBitmapPool());
            if (strokeColor == -1 || strokeWidth == -1) {
                strokeColor = ContextCompat.getColor(context, R.color.c_divider);
                strokeWidth = UiUtil.dip2px(0.5f);
            } else {
                this.strokeWidth = strokeWidth;
                this.strokeColor = strokeColor;
            }
            boardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            boardPaint.setStyle(Paint.Style.STROKE);
            boardPaint.setStrokeWidth(strokeWidth);
            boardPaint.setColor(strokeColor);
            boardPaint.setDither(true);
            dia = dip2px(context, 55);
        }

        public GlideCircleLineTransform(BitmapPool pool) {
            this.mBitmapPool = pool;
        }

        @Override
        public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
            Bitmap source = resource.get();
            int a, b;//a大，b小
//        if (source.getHeight() > source.getWidth()) {
            a = source.getHeight();
            b = source.getWidth();
//        } else {
//            a = source.getWidth();
//            b = source.getHeight();
//        }
            float f = b * 1f / a;

            int x = (int) Math.sqrt(dia * dia / (1 + f * f));
            Bitmap bitmapOut = mBitmapPool.get(dia, dia, Bitmap.Config.ARGB_8888);
            if (bitmapOut == null) {
                bitmapOut = Bitmap.createBitmap(dia, dia, Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(bitmapOut);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawCircle(dia / 2, dia / 2, dia / 2, paint);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(dia / 2, dia / 2, dia / 2 - 2, paint);

            int left, top, right, bottom;
            left = (int) (dia / 2 - x * f / 2);
            top = dia / 2 - x / 2;
            right = (int) (left + x * f);
            bottom = top + x;
            Rect rect = new Rect(left, top, right, bottom);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
            Matrix matrix = new Matrix();
            //matrix.postScale(0.5f, 0.5f);
            // matrix.setTranslate(-width, -height);
            canvas.drawBitmap(source, null, rect, paint);

            return BitmapResource.obtain(bitmapOut, mBitmapPool);


//
//        Bitmap bitmap = mBitmapPool.get(size, size, Bitmap.Config.ARGB_8888);
//        if (bitmap == null) {
//            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
//        }
//
//        Canvas canvas = new Canvas(bitmap);
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//
//        BitmapShader shader =
//                new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
//        if (width != 0 || height != 0) {
//            // source isn't square, move viewport to center
//            Matrix matrix = new Matrix();
//            matrix.setTranslate(-width, -height);
//            shader.setLocalMatrix(matrix);
//        }
//        paint.setShader(shader);
//        paint.setAntiAlias(true);
//
//        float r = size / 2f;
//        canvas.drawCircle(r, r, r, paint);
//        float boardR = r - strokeWidth / 2.0f;
//        canvas.drawCircle(r, r, boardR, boardPaint);
            //  return BitmapResource.obtain(bitmap, mBitmapPool);
        }

        @Override
        public String getId() {
            return "GlideCircleLineTransform()";
        }

        public int dip2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }
    }

    private static class GlideRoundTransform extends BitmapTransformation {
        private float radius = 0f;
        private Matrix mMatrix;

        public GlideRoundTransform(Context context) {
            this(context, 4);
        }

        public GlideRoundTransform(Context context, int dp) {
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
            mMatrix = new Matrix();
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform, outWidth, outHeight);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
            if (source == null) return null;
            float scale;
            float dx = 0, dy = 0;


            Bitmap result = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
            }
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            BitmapShader bitmapShader = new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
//        scale = Math.max(outWidth * 1.0f / source.getWidth(), outHeight * 1.0f / source.getHeight());
            if (source.getWidth() * outHeight > outWidth * source.getHeight()) {
                scale = (float) outHeight / (float) source.getHeight();
                dx = (outWidth - source.getWidth() * scale) * 0.5f;
            } else {
                scale = (float) outWidth / (float) source.getWidth();
                dy = (outHeight - source.getHeight() * scale) * 0.5f;
            }
            mMatrix.setScale(scale, scale);
            mMatrix.postTranslate(dx, dy);
            bitmapShader.setLocalMatrix(mMatrix);

            paint.setShader(bitmapShader);

            Canvas canvas = new Canvas(result);
            RectF rectF = new RectF(0f, 0f, outWidth, outHeight);
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public String getId() {
            return "GlideRoundTransform" + Math.round(radius);
        }
    }
}
