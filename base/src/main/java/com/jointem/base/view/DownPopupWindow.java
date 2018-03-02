package com.jointem.base.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jointem.base.R;
import com.jointem.base.util.DensityUtils;


/**
 * Created by THC on 2015/12/10.
 */
public class DownPopupWindow extends PopupWindow {
    private static final float POPUP_WINDOW_RATIO = 0.6f;
    private boolean isHeightFixed;
    private View view;
    private Context context;
    private CustomController controller;
    private LinearLayout parent = null;
    private OnDismiss onDismiss;
    private View backGroundView;

    /**
     * @param context       上下文对象
     * @param view          popupWindow中展示的视图
     * @param controller    控制视图的显示及其代码逻辑
     * @param isHeightFixed (TRUE)设置popupWindow的高度是否为屏幕高度的 3/5  (FALSE)否则为WRAP_CONTENT
     */
    public DownPopupWindow(Context context, View view, CustomController controller, boolean isHeightFixed) {
        super(context);
        this.isHeightFixed = isHeightFixed;
        this.view = view;
        this.context = context;
        this.controller = controller;
        initPopupWindow();
    }

    /**
     * @param context       上下文对象
     * @param view          popupWindow中展示的视图
     * @param controller    控制视图的显示及其代码逻辑
     * @param isHeightFixed (TRUE)设置popupWindow的高度是否为屏幕高度的 3/5  (FALSE)否则为WRAP_CONTENT
     * @param anchor        设置popupWindow的锚点视图  默认显示时popupWindow在Y方向上偏离 1dp
     */
    public DownPopupWindow(Context context, View view, CustomController controller, boolean isHeightFixed, View anchor) {
        super(context);
        this.isHeightFixed = isHeightFixed;
        this.view = view;
        this.context = context;
        this.controller = controller;
        initPopupWindow(anchor);
    }

    /**
     * @param context               上下文对象
     * @param view                  popupWindow中展示的视图
     * @param controller            控制视图的显示及其代码逻辑
     * @param isHeightFixed         (TRUE)设置popupWindow的高度是否为屏幕高度的 3/5  (FALSE)否则为WRAP_CONTENT
     * @param anchor                设置popupWindow的锚点视图 默认显示时popupWindow在Y方向上偏离 1dp
     * @param popupWindowBackground 设置popupWindow在显示之后它的背景视图
     */
    public DownPopupWindow(Context context, View view, CustomController controller, boolean isHeightFixed, View anchor, View popupWindowBackground) {
        super(context);
        backGroundView = popupWindowBackground;
        this.isHeightFixed = isHeightFixed;
        this.view = view;
        this.context = context;
        this.controller = controller;
        initPopupWindow(anchor, popupWindowBackground);
    }

    private void initPopupWindow() {
        initPopupWindow(null);
    }

    private void initPopupWindow(View anchor) {
        initPopupWindow(anchor, null);
    }

    @Override
    public boolean isShowing() {
        return super.isShowing();
    }

    private void initPopupWindow(final View anchor, final View popupWindowBackground) {
        if (parent == null) {
            parent = (LinearLayout) View.inflate(context, R.layout.popup_window_parent_view, null);
            setFocusable(true);
            setOutsideTouchable(true);
            setBackgroundDrawable(new BitmapDrawable());
            setAnimationStyle(R.style.popupWindowAnimation);
            setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            if (isHeightFixed) {
                setHeight((int) (DensityUtils.getScreenH(context) * POPUP_WINDOW_RATIO + 0.5));
            } else {
                setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        }
        parent.addView(view);
        setContentView(parent);
        if (!isShowing()) {
            if (popupWindowBackground != null) {
                popupWindowBackground.setVisibility(View.VISIBLE);
            }
        }

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (backGroundView != null) {
                    backGroundView.setVisibility(View.GONE);
                }
                if (anchor != null) {
                    RadioGroup radioGroup = anchor instanceof RadioGroup ? ((RadioGroup) anchor) : null;
                    if (radioGroup != null) {
                        for (int i = 0; i < radioGroup.getChildCount(); i++) {
                            ((RadioButton) radioGroup.getChildAt(i)).setChecked(false);
                        }
                    }
                }
                if (onDismiss != null) {
                    onDismiss.dismiss();
                }
            }
        });
        if (anchor != null) {
            showAsDropDown(anchor);
        }
        controller.showView();
    }

    /**
     * @param anchor                显示的锚点视图
     * @param popupWindowBackground popupWindow 显示的背景视图
     * @param checkedPosition       弹框显示时，让第 checkedPosition个控件为选中状态
     * @description 在重新点击的时候复用同一个视图
     */
    public void showAsDefine(View anchor, final View popupWindowBackground, int checkedPosition) {
        RadioGroup radioGroup = anchor instanceof RadioGroup ? ((RadioGroup) anchor) : null;
        if (popupWindowBackground != null) {
            popupWindowBackground.setVisibility(View.VISIBLE);

//            setOnDismissListener(new OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    popupWindowBackground.setVisibility(View.GONE);
//
//                    if (radioGroup != null) {
//                        for (int i = 0; i < radioGroup.getChildCount(); i++) {
//                            ((RadioButton) radioGroup.getChildAt(i)).setChecked(false);
//                        }
//                    }
//                }
//            });
        }
        if (radioGroup != null) {
            radioGroup.clearCheck();
            ((RadioButton) radioGroup.getChildAt(checkedPosition)).setChecked(true);
        }
        if (anchor != null) {
            showAsDropDown(anchor);
        }
    }

    public interface OnDismiss {
        void dismiss();
    }

    /**
     * @param anchor                显示的锚点视图
     * @param popupWindowBackground popupWindow 显示的背景视图
     * @description 在重新点击的时候复用同一个视图
     */
    public void showAsDefine(View anchor, final View popupWindowBackground) {
        RadioGroup radioGroup = anchor instanceof RadioGroup ? ((RadioGroup) anchor) : null;
        if (popupWindowBackground != null) {
            popupWindowBackground.setVisibility(View.VISIBLE);

//            setOnDismissListener(new OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    popupWindowBackground.setVisibility(View.GONE);
//
//                    if (radioGroup != null) {
//                        for (int i = 0; i < radioGroup.getChildCount(); i++) {
//                            ((RadioButton) radioGroup.getChildAt(i)).setChecked(false);
//                        }
//                    }
//                    if(onDismiss != null){
//                        onDismiss.dismiss();
//                    }
//                }
//            });
        }
        if (anchor != null) {
            showAsDropDown(anchor);
        }
    }

    public boolean isHeightFixed() {
        return isHeightFixed;
    }

    public void setIsHeightDefine(boolean isHeightDefine) {
        this.isHeightFixed = isHeightDefine;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Context getContext() {
        return context;
    }

    public CustomController getController() {
        return controller;
    }

    public void setController(CustomController controller) {
        this.controller = controller;
    }

    public void setOnWindowDismissListener(OnDismiss onDismissListener) {
        this.onDismiss = onDismissListener;
    }
}
