package com.jointem.base.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;

/**
 * Description:手机信息
 * Created by Kevin.Li on 2017/4/13.
 */
public final class PhoneUtil {
    /**
     * 拨打电话
     *
     * @param number 电话号码
     *               <p/>
     *               author: Kevin.Li
     *               created at 2017/4/14 9:23
     */
    public static void call(Context context, String number) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 发短信
     *
     * @param number 电话号码
     *               <p/>
     *               author: Kevin.Li
     *               created at 2017/4/14 9:24
     */
    public static void sendSms(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
        context.startActivity(intent);
    }

    /**
     * 发短信
     *
     * @param number  电话号码
     * @param smsBody 短信内容
     *                <p/>
     *                author: Kevin.Li
     *                created at 2017/4/14 9:24
     */
    public static void sendSms(Context context, String number, String smsBody) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
        intent.putExtra("sms_body", smsBody);
        context.startActivity(intent);
    }

    /**
     * 发送邮件
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 11:23
     */
    public static void sendEmail(Context context, String email) {
        Uri uri = Uri.parse("mailto:" + email);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        context.startActivity(Intent.createChooser(intent, "请选择邮箱类应用"));
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
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
     *
     * @param context
     */
    public static void forceOpenGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
