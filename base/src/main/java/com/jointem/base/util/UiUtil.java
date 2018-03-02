package com.jointem.base.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jointem.base.BaseApplication;
import com.jointem.base.R;
import com.jointem.base.view.CustomToast;
import com.jointem.base.view.EmptyViewBase;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;


public class UiUtil {

    //    public static void setRefreshLayout(Context context, ILoadingLayout loadingLayout) {
//        loadingLayout.setPullLabel(context.getString(R.string.is_pull_down_refresh));
//        loadingLayout.setReleaseLabel(context.getString(R.string.is_refresh_start));
//        loadingLayout.setRefreshingLabel(context.getString(R.string.is_refreshing));
//        loadingLayout.setLoadingDrawable(context.getResources().getDrawable(R.drawable.iconfont_downgrey));
//    }
//
//    public static void setLoadingLayout(Context context, ILoadingLayout loadingLayout) {
//        loadingLayout.setPullLabel(context.getString(R.string.is_pull_down_load));
//        loadingLayout.setReleaseLabel(context.getString(R.string.is_pull_load_more));
//        loadingLayout.setRefreshingLabel(context.getString(R.string.is_loading));
//        loadingLayout.setLoadingDrawable(context.getResources().getDrawable(R.drawable.iconfont_downgrey));
//    }
//
//    public static void setFinishLayout(Context context, ILoadingLayout loadingLayout) {
//        loadingLayout.setReleaseLabel(context.getString(R.string.is_loaded_all));
//        loadingLayout.setPullLabel(context.getString(R.string.is_loaded_all));
//        loadingLayout.setRefreshingLabel(context.getString(R.string.is_loaded_all));
//        loadingLayout.setLoadingDrawable(null);
//    }
//
//    public static void setCustomLayout(Context context, ILoadingLayout loadingLayout, int img, String... data) {
//        loadingLayout.setReleaseLabel(data[0]);
//        loadingLayout.setPullLabel(data[1]);
//        loadingLayout.setRefreshingLabel(data[2]);
//        loadingLayout.setLoadingDrawable(context.getResources().getDrawable(img));
//    }
//
//    public static void initPullToRefreshText(Context context, PullToRefreshBase listView) {
//        // 设置下拉刷新的文字
//        ILoadingLayout refreshLayout = listView.getLoadingLayoutProxy(true, false);
//        UiUtil.setRefreshLayout(context, refreshLayout);
//        // 设置上拉加载的文字
//        ILoadingLayout loadingLayout = listView.getLoadingLayoutProxy(false, true);
//        UiUtil.setLoadingLayout(context, loadingLayout);
//    }
//
//    public static void setFinishLayout(Context context, PullToRefreshBase listView) {
//        ILoadingLayout loadingLayoutProxy = listView.getLoadingLayoutProxy(false, true);
//        UiUtil.setFinishLayout(context, loadingLayoutProxy);
//    }

    /**
     * 已过时，请使用{@link DensityUtils#sp2px(android.content.Context, float)}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 10:35
     */
    @Deprecated
    public static int sp2px(float spValue) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, BaseApplication.getContextFromApplication().getResources().getDisplayMetrics()) + 0.5f);
    }

    /**
     * 已过时，请使用{@link DensityUtils#dip2px(android.content.Context, float)}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 10:36
     */
    @Deprecated
    public static int dip2px(float dpValue) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, BaseApplication.getContextFromApplication().getResources().getDisplayMetrics()) + 0.5f);
    }

    /**
     * 已过时，请使用{@link DensityUtils#px2dip(android.content.Context, float)}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 10:38
     */
    @Deprecated
    public static int px2dip(int px) {
        float scale = BaseApplication.getContextFromApplication().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    /**
     * 已过时，请使用{@link DensityUtils#px2sp(android.content.Context, float)}
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 10:39
     */
    @Deprecated
    public static int px2sp(float pxValue) {
        final float fontScale = BaseApplication.getContextFromApplication().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static void showToast(Context context, String message) {
        CustomToast toast = new CustomToast(context, message);
        toast.show();
    }

    public static void showToast(Context context, int resId) {
        CustomToast toast = new CustomToast(context, resId);
        toast.show();
    }

    /**
     * 获取状态栏高度
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/13 17:07
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int x, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 设置背景透明度
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/13 17:08
     */
    public static void setBackgroundAlpha(Activity aty, float alpha) {
        convertActivityFromTranslucent(aty);//设置窗体不透明

        WindowManager.LayoutParams params = aty.getWindow().getAttributes();
        params.alpha = alpha;
        aty.getWindow().setAttributes(params);
    }

    /**
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
    public static void convertActivityFromTranslucent(Activity activity) {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
            method.setAccessible(true);
            method.invoke(activity);
        } catch (Throwable t) {
            Logger.i(t.getMessage());
        }
    }

    /**
     * 设置item的背景色
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/13 17:08
     */
    public static void setItemBg(View view) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && view != null) {
//            view.setBackgroundResource(R.drawable.bg_item_light);
//        }
        view.setBackgroundResource(R.drawable.ripple_item_clicked_with_mask);
    }

    /**
     * 透明化状态栏
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/13 17:10
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void setTranslucentStatus(Activity activity, boolean on) {
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
     * 初始化列表空视图控件
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 10:11
     */
    public static void initEmptyView(Context context, EmptyViewBase emptyViewBase) {
        if (!SystemTool.isNetworkConnected(context)) {
            emptyViewBase.setNoNet();
        } else {
            emptyViewBase.setHaveNet();
        }
    }

    /**
     * 设置星级
     *
     * @param context
     * @param layout    父级容器
     * @param starCount 星星个数
     * @param imgRes    星号图标
     */
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

    public static void setLifeStarLevel(Context context, LinearLayout layout, String starCount, int imgRes, int imgResHalf) {
        BigDecimal bg = new BigDecimal(starCount);
        String[] startCounts = starCount.split("\\.");
        String stringValue = null;
        Double starDouble = Double.valueOf(starCount);
        if(starDouble > 4.9){
            stringValue = bg.setScale(1, BigDecimal.ROUND_FLOOR).toString();
        } else {
            stringValue = bg.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
        }
        String[] strings = stringValue.split("\\.");
        Integer integer = Integer.valueOf(strings[0]);
        Integer point = Integer.valueOf(strings[1]);
        ImageView imv;
        layout.removeAllViews();
        if (integer > 0) {
            layout.setVisibility(View.VISIBLE);
            layout.setGravity(Gravity.CENTER_VERTICAL);
            for (int i = 0; i < integer; i++) {
                imv = new ImageView(context);
                imv.setPadding(0, 0, 5, 0);
                imv.setImageResource(imgRes);
                layout.addView(imv);
            }
        }
        if(point > 0){
            imv = new ImageView(context);
            imv.setPadding(0, 0, 5, 0);
            imv.setImageResource(imgResHalf);
            layout.addView(imv);
        }
//        else if(point > 0 && point <= 5){
//            imv = new ImageView(context);
//            imv.setPadding(0, 0, 10, 0);
//            imv.setImageResource(imgResHalf);
//            layout.addView(imv);
//        }
        TextView tvPoint = new TextView(context);
        tvPoint.setGravity(Gravity.CENTER);
        tvPoint.setText(stringValue);
        layout.addView(tvPoint);
    }

    /**
     * 初始胡指示器
     * <p/>
     * author: Kevin.Li
     * created at 2017/4/14 17:47
     */
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
     * 关闭软键盘
     */
    public static void hideSoftInputFromWindow(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 打开软件盘
     */
    public static void openSoftKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 获取屏幕的寬
     * @param aty
     * @return
     */
    public static int getScreenW(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高
     * @param aty
     * @return
     */
    public static int getScreenH(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
    /**
     * 获取当前屏幕截图，包含状态栏
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenW(activity);
        int height = getScreenH(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenW(activity);
        int height = getScreenH(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获得状态栏的高度
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}