package com.jointem.base.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityOptions;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 改类已被拆分为更细的类
 * <p/>
 * author: Kevin.Li
 * created at 2017/4/14 17:50
 */
@SuppressLint("SimpleDateFormat")
@Deprecated
public final class Utils {
    public static Utils getInstance() {
        return new Utils();
    }

    /**
     * 已过时，请使用{@link SystemTool#getMetaValue(android.content.Context, java.lang.String)}
     * 获取meta值
     *
     * @param context
     * @param metaKey
     * @return
     */
    @Deprecated
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }


    /**
     * 版本名。
     * <p>
     * 已过时，请使用{@link SystemTool#getAppVersionName(android.content.Context)}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 11:12
     */
    @Deprecated
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 版本号
     * <p>
     * 已过时，请使用{@link com.jointem.base.util.SystemTool#getAppVersionCode(Context)}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 11:11
     */
    @Deprecated
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    @Deprecated
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * @param activity
     * @param on
     * @throws
     * @Description: 透明化状态栏
     * @Return_type: void
     * @author JW.Lee
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Deprecated
    private void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }



    /**
     * 去除字符串中的无效字符
     * <p>
     * 已过时，请使用{@link com.jointem.base.util.StringUtils#deleteInvalidCharacter(java.lang.String)}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 9:21
     */
    @Deprecated
    public static String deleteInvalidCharacter(String str) {
        return str == null ? "" : str.replaceAll("\\s*", "");
    }

    /**
     * 已过时，请使用{@link SystemTool#isNetworkConnected(android.content.Context)}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 11:01
     */
    @Deprecated
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 拨打电话
     * <p>
     * 已过时，请使用{@link PhoneUtil#call(android.content.Context, java.lang.String)}
     *
     * @param number 电话号码
     *               <p/>
     *               author: Kevin.Li
     *               created at 2017/4/14 9:23
     */
    @Deprecated
    public static void call(Context context, String number) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 发短信
     * <p>
     * 已过时，请使用{@link PhoneUtil#sendSms(android.content.Context, java.lang.String)}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 9:24
     */
    @Deprecated
    public static void sendSms(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
        context.startActivity(intent);
    }

    /**
     * 已过时，请调用{@link FileOperateUtil#changeImageToBase64(java.io.ByteArrayOutputStream)}
     *
     * @param stream
     * @return
     * @throws
     * @Description: 将字节数组输出流转换为Base64字符串
     * @Return_type: String
     * @author JW.Lee
     */
    @Deprecated
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
     * 已过时，请调用{@link FileOperateUtil#changeBase64ToImage(java.lang.String)}
     *
     * @param strImg
     * @return
     * @throws
     * @Description: 将Base64字符串转换为位图
     * @Return_type: Bitmap
     * @author JW.Lee
     */
    @Deprecated
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
     * 已过时，请调用{@link FileOperateUtil#getSmallBitmap(java.lang.String)}
     *
     * @throws
     * @Description: 根据路径获得图片并压缩，返回bitmap用于显示
     * @Return_type: Bitmap
     * @author JW.Lee
     */
    @Deprecated
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
     * 已过时，请调用{@link FileOperateUtil#calculateInSampleSize(android.graphics.BitmapFactory.Options, int, int)}
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws
     * @Description: 计算图片的缩放值
     * @Return_type: int
     * @author JW.Lee
     */
    @Deprecated
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
     * 已过时，请调用{@link FileOperateUtil#compressPhotoAndGetBase64String(java.lang.String)}
     *
     * @param filePath
     * @return
     * @throws
     * @Description: 将图片按比例进行压缩, 返回由图片转换成Base64编码的字符串，以便将图片上传到服务器
     * @Return_type: String
     * @author JW.Lee
     */
    @Deprecated
    public static String compressPhotoAndGetBase64String(String filePath) {

        Bitmap bm = getSmallBitmap(filePath);
        // 101539b =99kb sd卡中368KB
        // saveMyBitmap(bm);
        return compressBitmapAndGetBase64String(bm);
    }

    /**
     * 已过时，请调用{@link FileOperateUtil#compressBitmapAndGetBase64String(android.graphics.Bitmap)}
     *
     * @Description: 将缩略图按比例进行压缩, 返回由图片转换成Base64编码的字符串，以便将图片上传到服务器
     * @Return_type: String
     * @author JW.Lee
     */
    @Deprecated
    public static String compressBitmapAndGetBase64String(Bitmap bitmap) {
        bitmap = bitmap.copy(Config.RGB_565, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 50 && options > 0) {// 循环判断如果压缩后图片字节流大于80kb，继续压缩
            baos.reset();
            bitmap.compress(CompressFormat.JPEG, options, baos);
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
     * 获取下载到本地的apk的路径
     * <p>已过时，请调用{@link SystemTool#getApkPath(java.lang.String, java.lang.String)}</p>
     *
     * @param appName       名称
     * @param newestVersion 版本号
     *                      <p>
     *                      author: Kevin.Li
     *                      created at 2016/7/12 16:49
     */
    @Deprecated
    public static String getApkPath(String appName, String newestVersion) {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getPath());
        sb.append("/");
        sb.append(appName);
        sb.append("_");
        sb.append(newestVersion);
        sb.append(".apk");
        Logger.i("--------应用最新安装包存放路径：" + sb.toString());
        return sb.toString();
    }

    /**
     * 检测是否有apk，如果有则删除
     * <p>已过时，请调用{@link SystemTool#deleteApk(java.lang.String, java.lang.String)}</p>
     *
     * @param appName       名称
     * @param newestVersion 版本号
     */
    @Deprecated
    public static void deleteApk(String appName, String newestVersion) {
        File file = new File(getApkPath(appName, newestVersion));
        if (file.exists()) {
            file.delete();
            Logger.i("--------成功删除最新安装包----------");
        } else {
            Logger.i("--------最新安装包不存在----------");
        }
    }

    /**
     * 检测是否有apk，如果有则删除
     * <p>不在使用DownloadManager进行下载，将不再调用此方法</p>
     *
     * @param newestVersion apk版本号
     * @param path          apk存放目录
     *                      <p/>
     *                      author: Kevin.Li
     *                      created at 2017/4/13 17:53
     */
    @Deprecated
    public static void deleteApk(Context context, String newestVersion, String path) {
//        String path = PreferenceHelper.readString(context,
//                CommonConstants.DOWNLOAD_FILE_NAME,
//                CommonConstants.DOWNLOAD_PATH_NAME);
        if (StringUtils.isEmpty(path)) {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cursor = downloadManager.query(query);
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    path = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                }
            }
        }
        if (StringUtils.isEmpty(path)) {
            StringBuffer sb = new StringBuffer("/storage/emulated/0/cache/zyb_");
            sb.append(newestVersion);
            sb.append(".apk");
            path = sb.toString();
        }
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 安装APK
     * <P>已过时，请调用{@link SystemTool#installApk(android.content.Context, java.lang.String)}</p>
     *
     * @param urlPath
     * @param context
     */
    @Deprecated
    public static void installApk(Context context, String urlPath) {
        Intent updateIntent = new Intent(Intent.ACTION_VIEW);
        updateIntent.setDataAndType(Uri.fromFile(new File(urlPath)),
                "application/vnd.android.package-archive");
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(updateIntent);
    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的。
     * <p>
     * 已过时，请使用{@link PhoneUtil#isOPenGps(android.content.Context)}
     *
     * @param context
     * @return true 表示开启
     */
    @Deprecated
    public static boolean isOPenGps(final Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 强制帮用户打开GPS
     * <p>
     * 已过时，请使用{@link PhoneUtil#forceOpenGPS(android.content.Context)}
     *
     * @param context
     */
    @Deprecated
    public static void forceOpenGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 已过时，请调用{@link SystemTool#hasNewVersion(java.lang.String, java.lang.String)}
     *
     * @param nativeVersion
     * @param newestAPKVer
     * @return
     * @throws
     * @Description: 检测是否有新版本
     * @Return_type: boolean
     * @author JW.Lee
     */
    @Deprecated
    public static boolean hasNewVersion(String nativeVersion, String newestAPKVer) {
        if (StringUtils.isEmpty(nativeVersion) || StringUtils.isEmpty(newestAPKVer)) {
            return false;
        }
        if (nativeVersion.equals(newestAPKVer)) {
            return false;
        } else if (StringUtils.isEmpty(newestAPKVer)
                || newestAPKVer.equals("0.0.0")) {
            return false;
        } else {
            String nativeStr = nativeVersion.replaceAll("\\D", "");// 除去非数字字符
            String newStr = newestAPKVer.replaceAll("\\D", "");
            if (Long.valueOf(newStr) > Long.valueOf(nativeStr)) {
                return true;
            }
            return false;
        }
    }

    /**
     * 已过时，请使用{@link PhoneUtil#sendEmail(android.content.Context, java.lang.String)}
     *
     * @param context 要发送邮件的activity
     * @param email   email账号
     * @Description: 发送邮件
     */
    @Deprecated
    public static void sendEmail(Context context, String email) {
        Uri uri = Uri.parse("mailto:" + email);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        context.startActivity(Intent.createChooser(intent, "请选择邮箱类应用"));
    }

    /**
     * 获取文件的类型
     * <p/>
     * 已过时，请调用{@link FileOperateUtil#getFileType(java.lang.String)}
     *
     * @param fileName ：文件名
     * @return 文件类型
     */
    @Deprecated
    public static String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    /**
     * 已过时，请调用{@link FileOperateUtil#bitmap2Bytes(android.graphics.Bitmap)}
     *
     * @param bm
     * @return
     * @throws
     * @Description: 将位图转换为byte二进制流
     * @Return_type: byte[]
     * @author JW.Lee
     */
    @Deprecated
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 已过时，请调用{@link FileOperateUtil#bitmap2InputStream(android.graphics.Bitmap, int)}
     *
     * @param bm
     * @param quality
     * @return
     * @throws
     * @Description: 将位图转换为InputStream输入流
     * @Return_type: InputStream
     * @author JW.Lee
     */
    @Deprecated
    public static InputStream bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }


    /**
     * 已过时，请调用{@link DateTimeUtil#dateCompare(java.lang.String, java.lang.String, java.lang.String)}
     *
     * @param dateStr1
     * @param dateStr2
     * @throws Exception
     * @throws
     * @Description: 字符串日期比较
     * @Return_type: int {-1：小于；0：等于；1：大于}
     * @author JW.Lee
     */
    @Deprecated
    public static int dateCompare(String format, String dateStr1,
                                  String dateStr2) throws Exception {
        // 设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // 得到指定模范的时间
        Date d1 = sdf.parse(dateStr1);
        Date d2 = sdf.parse(dateStr2);
        // 比较
        long result = d1.getTime() - d2.getTime();
        if (result > 0) {
            return 1;
        } else if (result < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 已过时，请调用{@link DateTimeUtil#dateCompare2CurrentDate(java.lang.String, java.lang.String)}
     *
     * @param dateStr
     * @return
     * @throws Exception
     * @throws
     * @Description: 自定字符串日期与当前日期比较
     * @Return_type: Stirng {-1：小于；0：等于；1：大于}
     * @author JW.Lee
     */
    @Deprecated
    public static int dateCompareT_currentDate(String format, String dateStr)
            throws Exception {
        String currentDate = SystemTool.getDataTime(format);
        return dateCompare(format, dateStr, currentDate);
    }

    /**
     * 已过时，请调用{@link DateTimeUtil#dateStrConvertMillisecond(java.lang.String, java.lang.String)}
     *
     * @param dateStr
     * @param format
     * @return
     * @throws
     * @Description: 将日期时间转为毫秒
     * @Return_type: long
     * @author JW.Lee
     */
    @Deprecated
    public static long dateStrCompareMillisecond(String dateStr, String format) {
        // 设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // 得到指定模版的时间
        Date d;
        try {
            d = sdf.parse(dateStr);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }

    /**
     * 已过时，请调用{@link DateTimeUtil#getFormatedDateTime(long, java.lang.String)}
     *
     * @param formatter 显示格式
     * @param dateTime  毫秒数
     * @return
     * @throws
     * @Description:
     * @Return_type: String
     * @author JW.Lee
     */
    @Deprecated
    public static String getFormatedDateTime(String formatter, long dateTime) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(formatter);
        return sDateFormat.format(new Date(dateTime));
    }

    /**
     * 已过时，请调用{@link DateTimeUtil#getFormatedDateTime(long, java.lang.String)}
     *
     * @param milliseconds 毫秒数
     * @param format       时间格式
     * @return
     * @throws
     * @Description: 传入毫秒数，返回指定格式的时间
     * @Return_type: String
     * @author JW.Lee
     */
    @Deprecated
    public static String getDataTime(long milliseconds, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date(milliseconds));
    }

    /**
     * 判断服务是否处于运行状态.
     * <p/>
     * 已过时，请调用{@link SystemTool#isServiceRunning(java.lang.String, android.content.Context)}
     *
     * @param serviceName
     * @param context
     * @return
     */
    @Deprecated
    public static boolean isServiceRunning(String serviceName, Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> infos = am.getRunningServices(100);
        for (RunningServiceInfo info : infos) {
            if (serviceName.equals(info.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * SD卡剩余大小
     * <p>
     * 已过时，请使用{@link SystemTool#getSDFreeSize()}
     *
     * @return SD卡剩余大小
     */
    @Deprecated
    public static long getSDFreeSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        return freeBlocks * blockSize; // 单位Byte
        // return (freeBlocks * blockSize)/1024; //单位KB
        // return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 已过时，请调用{@link StringUtils#getSubValue(java.lang.String[], int, int)}
     *
     * @param arr
     * @param startIndex
     * @param endIndex
     * @return
     * @throws
     * @Description: 获取数组指定下标间的值，异常情况，直接返回null
     * @Return_type: String[]
     * @author Kevin.Li
     */
    @Deprecated
    public static ArrayList<String> getSubValue(String[] arr, int startIndex, int endIndex) {
        if (arr == null || arr.length < 1)
            return null;

        if (startIndex >= endIndex || startIndex >= arr.length
                || endIndex > arr.length)
            return null;
        ArrayList<String> newArr = new ArrayList<String>(endIndex - startIndex);
        for (int i = startIndex; i < endIndex; i++) {
            newArr.add(arr[i]);
        }
        return newArr;
    }

    /**
     * 设置星级
     * <p>已过时，请调用{@link UiUtil#setStarLevel(android.content.Context, android.widget.LinearLayout, int, int)}</p>
     *
     * @param context
     * @param layout    父级容器
     * @param starCount 星星个数
     * @param imgRes    星号图标
     */
    @Deprecated
    public static void setStarLevel(Context context, LinearLayout layout, int starCount, int imgRes) {
        if (starCount > 0) {
            layout.setVisibility(View.VISIBLE);
            layout.setGravity(Gravity.CENTER_VERTICAL);
            layout.removeAllViews();
            ImageView imv;
            for (int i = 0; i < starCount; i++) {
                imv = new ImageView(context);
                imv.setPadding(0, 0, 10, 0);
                imv.setImageResource(imgRes);
                layout.addView(imv);
            }
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    /**
     * 将string日期转换成long
     * <p/>
     * 已过时，请调用{@link DateTimeUtil#string2Long(java.lang.String)}
     *
     * @param time
     * @return
     */
    @Deprecated
    public static long String2Long(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(time).getTime();
        } catch (ParseException e) {
            return 0L;
        }
    }

    /**
     * 已过时，请调用{@link StringUtils#inputStream2String(java.io.InputStream)}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 17:37
     */
    @Deprecated
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

    /**
     * 已过时，请调用{@link UiUtil#initIndicator(android.content.Context, android.view.ViewGroup, int, int, int, int, int, int)}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 17:48
     */
    @Deprecated
    public static void initIndicator(Context ct, final ViewGroup parent, int num, int indicatorWidthDP, int indicatorHeightDP, int indicatorMarginDP, final int defaultBackgroudDrawable, final int checkedBackgroudDrawable) {
        int height = DensityUtils.dip2px(ct, indicatorHeightDP);
        int width = DensityUtils.dip2px(ct, indicatorWidthDP);
        int margin = DensityUtils.dip2px(ct, indicatorMarginDP);
        for (int i = 0; i < num; i++) {
            RadioButton rBtn = new RadioButton(ct);
            LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(width, height);
            layoutParams.setMargins(margin, 0, margin, 0);
            rBtn.setLayoutParams(layoutParams);
            rBtn.setButtonDrawable(android.R.color.transparent);
            rBtn.setBackgroundResource(defaultBackgroudDrawable);
            rBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    RadioButton rBtn = (RadioButton) buttonView;
                    if (isChecked) {
                        rBtn.setBackgroundResource(checkedBackgroudDrawable);
                    } else {
                        rBtn.setBackgroundResource(defaultBackgroudDrawable);
                    }
                }
            });
            parent.addView(rBtn, i);
        }
        if (parent.getChildCount() > 0)
            ((RadioButton) parent.getChildAt(0)).setChecked(true);
    }

    /**
     * 转换时间
     * <p/>
     * 已过时，请调用{@link DateTimeUtil#friendDisplayTime(int)}
     *
     * @param second
     * @return
     */
    @Deprecated
    public static String conversionTime(int second) {
        StringBuffer sb = new StringBuffer();
        int mm = 60;
        int hh = mm * 60;
        int dd = hh * 24;
        int mou = dd * 30;
        int yy = mou * 12;

        long year = second / yy;
        long mouth = (second - year * yy) / mou;
        long day = (second - year * yy - mouth * mou) / dd;
        long hour = (second - year * yy - mouth * mou - day * dd) / hh;
        long min = (second - year * yy - mouth * mou - day * dd - hour * hh) / mm;

        if (year > 0) {
            sb.append(year);
            sb.append("年");
        }
        if (mouth > 0) {
            sb.append(mouth);
            sb.append("月");
        }
        if (day > 0) {
            sb.append(day);
            sb.append("天");
        }
        if (hour > 0) {
            if (StringUtils.isEmpty(sb.toString())) {
                sb.append(hour);
                if (min > 0) {
                    sb.append("时");
                } else {
                    sb.append("小时");
                }
            } else {
                sb.append(hour);
                sb.append("时");
            }
        }
        if (min > 0) {
            if (StringUtils.isEmpty(sb.toString())) {
                sb.append(min);
                sb.append("分钟");
            } else {
                sb.append(min);
                sb.append("分");
            }
        }
        if (min == 0) {
            if (StringUtils.isEmpty(sb.toString())) {
                sb.append("小于一分钟");
            }
        }
        return sb.toString();
    }

    /**
     * 比较两个double类型的大小
     * <p>
     * 已过时，请调用{@link StringUtils#compare(java.math.BigDecimal, java.math.BigDecimal)}
     * author: Kevin.Li
     * created at 2015/12/23 20:17
     */
    @Deprecated
    public static int compare(BigDecimal val1, BigDecimal val2) {
        return val1.compareTo(val2);
    }

    /**
     * 获取手机宽度
     * <p>已过时，请调用{@link DensityUtils#getScreenW(android.content.Context)}</p>
     *
     * @param context
     * @return
     */
    @Deprecated
    public static int getPhoneWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

    /**
     * 判断是否有某项权限
     *
     * @param context
     * @param permissionName
     * @return
     */
    public static boolean hasPermission(Context context, String permissionName) {
        PackageManager pm = context.getPackageManager();
        boolean hasPermission = (PackageManager.PERMISSION_GRANTED
                == pm.checkPermission(permissionName, context.getPackageName()));
        return hasPermission;
    }

    /**
     * 将文本中的关键字变色
     * <p/>
     * 已过时，请调用{@link StringUtils#matcherSearchText(int, java.lang.String, java.lang.String)}
     *
     * @param color
     * @param text
     * @param keyword
     * @return
     */
    @Deprecated
    public static SpannableString matcherSearchText(int color, String text, String keyword) {
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    /**
     * 保留一位小数
     * <p/>
     * 已过时，请调用{@link StringUtils#saveFloatScale(java.lang.Float)}
     *
     * @param f
     * @return
     */
    @Deprecated
    public static float saveFloatScale(Float f) {
        return Math.round(f * 10) / 10.0f;
    }

    /**
     * 已过时，请调用{@link UiUtil#setBackgroundAlpha(android.app.Activity, float)}
     * <p>
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} to a fullscreen opaque
     * Activity.
     * <p>
     * Call this whenever the background of a translucent Activity has changed
     * to become opaque. Doing so will allow the {@link android.view.Surface} of
     * the Activity behind to be released.
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    @Deprecated
    public static void convertActivityFromTranslucent(Activity activity) {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
            method.setAccessible(true);
            method.invoke(activity);
        } catch (Throwable t) {
            Logger.e(t.getMessage());
        }
    }

    /**
     * 已过时，请调用{@link SwipeLayoutUtil#convertActivityToTranslucent(android.app.Activity)}
     * <p>
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} back from opaque to
     * translucent following a call to
     * {@link #convertActivityFromTranslucent(Activity)} .
     * <p>
     * Calling this allows the Activity behind this one to be seen again. Once
     * all such Activities have been redrawn
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    @Deprecated
    public static void convertActivityToTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convertActivityToTranslucentAfterL(activity);
        } else {
            convertActivityToTranslucentBeforeL(activity);
        }
    }


    /**
     * 已过时，请调用{@link SwipeLayoutUtil#convertActivityToTranslucentBeforeL(android.app.Activity)}
     * <p>
     * Calling the convertToTranslucent method on platforms before Android 5.0
     */
    @Deprecated
    public static void convertActivityToTranslucentBeforeL(Activity activity) {
        try {
            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                    break;
                }
            }
            Method method = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz);
            method.setAccessible(true);
            method.invoke(activity, new Object[]{null});
        } catch (Throwable t) {
            Logger.e(t.toString());
        }
    }

    /**
     * 已过时，请调用{@link SwipeLayoutUtil#convertActivityToTranslucentAfterL(Activity)}
     * <p>
     * Calling the convertToTranslucent method on platforms after Android 5.0
     */
    @Deprecated
    private static void convertActivityToTranslucentAfterL(Activity activity) {
        try {
            Method getActivityOptions = Activity.class.getDeclaredMethod("getActivityOptions");
            getActivityOptions.setAccessible(true);
            Object options = getActivityOptions.invoke(activity);

            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                    break;
                }
            }
            Method convertToTranslucent = Activity.class.getDeclaredMethod("convertToTranslucent",
                    translucentConversionListenerClazz, ActivityOptions.class);
            convertToTranslucent.setAccessible(true);
            convertToTranslucent.invoke(activity, null, options);
        } catch (Throwable t) {
        }
    }

    /**
     * 已过时，请调用{@link IntentUtil#gotoPermissionSettings(android.content.Context)}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 15:24
     */
    @Deprecated
    public static boolean gotoPermissionSettings(Context context) {
        boolean mark = isMIUI();
        if (mark) {
            // 之兼容miui v5/v6  的应用权限设置页面，否则的话跳转应用设置页面（权限设置上一级页面）
            try {
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", context.getPackageName());
                context.startActivity(localIntent);
            } catch (ActivityNotFoundException e) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        } else {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        }

        return mark;
    }

    /**
     * 已过时，请调用{@link IntentUtil#gotoPermissionSettings(android.app.Activity, int)}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 15:24
     */
    @Deprecated
    public static boolean gotoPermissionSettings(Activity activity, int requestCode) {
        boolean mark = isMIUI();
        if (mark) {
            // 之兼容miui v5/v6  的应用权限设置页面，否则的话跳转应用设置页面（权限设置上一级页面）
            try {
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", activity.getPackageName());
                activity.startActivityForResult(localIntent, requestCode);
            } catch (ActivityNotFoundException e) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivityForResult(intent, requestCode);
            }
        } else {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            activity.startActivityForResult(intent, requestCode);
        }

        return mark;
    }

    /**
     * 已过时请调用{@link IntentUtil#isMIUI()}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 15:24
     */
    @Deprecated
    public static boolean isMIUI() {
        String device = Build.MANUFACTURER;
        if (device.equals("Xiaomi")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 已过时，请调用{@link SystemTool#copy(android.content.Context, java.lang.String)}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 15:29
     */
    @Deprecated
    public static void copy(Context context, String content) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText(null, content));
    }

    /**
     * 已过时，请调用{@link SystemTool#getText4ClipboardManager(android.content.Context)}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 15:33
     */
    @Deprecated
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb.hasPrimaryClip()) {
            return cmb.getPrimaryClip().getItemAt(0).getText().toString();
        } else {
            return null;
        }
    }
}
