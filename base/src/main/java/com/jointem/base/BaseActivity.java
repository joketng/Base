package com.jointem.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.jointem.base.iView.I_initializer;
import com.jointem.base.util.SystemTool;
import com.jointem.base.view.swiplayoutview.SwipeBackActivity;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.processors.BehaviorProcessor;


public class BaseActivity extends SwipeBackActivity implements I_initializer, View.OnClickListener {
    public Context context;
    public Activity activity;
    public final static Integer ACTIVITY_EVENT = 998;
    public final BehaviorProcessor<Integer> lifecycleSubject = BehaviorProcessor.create();
    private Unbinder mBind;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // // 隐藏状态栏
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(view, params);
    }

    @Override
    public void setContentView(View view) {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        activity = this;
        ActivityHelper.getInstance().pushActivity(this);
        setRootView();
        mBind = ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        initialize();
        Logger.d(SystemTool.getRunningActivityName(this));
    }

    @Override
    protected void onDestroy() {
        mBind.unbind();
        lifecycleSubject.onNext(ACTIVITY_EVENT);
        super.onDestroy();
//        ActivityHelper.getInstance().popActivity();
        context = null;
        activity = null;
    }

    protected void initialize() {
        initData();
        initWidget();
    }

    @Override
    public void setRootView() {
    }

    @Override
    public void initData() {
    }

    @Override
    public void initWidget() {
    }

    protected void widgetClick(View v) {
    }

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            ImmerseHelper.setSystemBarTransparent(this);
//        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setSwipeBackEnable(false);
        }
    }
}
