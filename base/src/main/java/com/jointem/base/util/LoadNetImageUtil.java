package com.jointem.base.util;

import android.content.Context;
import android.widget.ImageView;

import com.jointem.plugin.request.GetInterfaceConfig;

/**
 * Description:加载网络图片
 * Created by Kevin.Li on 2017/6/2.
 */
public class LoadNetImageUtil {
    public static String transformationUrl(String url) {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(url) && !url.startsWith("http")) {
            sb.append(GetInterfaceConfig.URL_IMAGE_SERVER);
        }
        sb.append(url);
        return sb.toString();
    }

    /**
     * 加载图片到相应的控件上
     *
     * @param context   直接使用Context将不受生命周期控制，使用Activity，Fragment将跟随当前Activity/Fragment的生命周期
     * @param netImgUrl 网络图片地址
     * @param imageView 展示图片的控件（ImageView）
     */
    public static void displayImage(Context context, String netImgUrl, ImageView imageView) {
        ImageUtil.displayImage(context, transformationUrl(netImgUrl), imageView);
    }

    /**
     * 加载图片到相应的控件上(圆形裁剪)
     *
     * @param context     直接使用Context将不受生命周期控制，使用Activity，Fragment将跟随当前Activity/Fragment的生命周期
     * @param netImgUrl   网络图片地址
     * @param imageView   展示图片的控件（ImageView）
     * @param strokeWidth 圆形边框的宽度 -1为默认值 1dp
     * @param strokeColor 圆形边框的颜色 -1为默认值 #d9d9d9
     */
    public static void displayCircleImage(Context context, String netImgUrl, ImageView imageView, int strokeColor, int strokeWidth) {
        ImageUtil.displayCircleImage(context, transformationUrl(netImgUrl), imageView, strokeColor, strokeWidth);
    }

    /**
     * 加载图片到相应的控件上(圆形裁剪)
     *
     * @param context   直接使用Context将不受生命周期控制，使用Activity，Fragment将跟随当前Activity/Fragment的生命周期
     * @param netImgUrl 网络图片地址
     * @param imageView 展示图片的控件（ImageView）
     */
    public static void displayCircleImage(final Context context, String netImgUrl, ImageView imageView) {
        ImageUtil.displayCircleImage(context, transformationUrl(netImgUrl), imageView);
    }


    /**
     * 加载图片到相应的控件上(圆角处理)
     *
     * @param context   直接使用Context将不受生命周期控制，使用Activity，Fragment将跟随当前Activity/Fragment的生命周期
     * @param netImgUrl 网络图片地址
     * @param imageView 展示图片的控件（ImageView）
     * @param radius    圆角的半径 默认圆角的半径为 4
     */
    public static void displayRoundCornerImage(final Context context, String netImgUrl, ImageView imageView, final int radius) {
        ImageUtil.displayRoundCornerImage(context, transformationUrl(netImgUrl), imageView, radius);
    }

    /**
     * 加载图片到相应的控件上(圆角处理)
     *
     * @param context   直接使用Context将不受生命周期控制，使用Activity，Fragment将跟随当前Activity/Fragment的生命周期
     * @param netImgUrl 网络图片地址
     * @param imageView 展示图片的控件（ImageView）
     *                  radius 默认圆角的半径为 4
     */
    public static void displayRoundCornerImage(final Context context, String netImgUrl, ImageView imageView) {
        ImageUtil.displayRoundCornerImage(context, transformationUrl(netImgUrl), imageView);
    }
}
