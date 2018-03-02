package com.jointem.fire;

import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.jointem.base.BaseApplication;
import com.jointem.fire.bean.LocateCity;
import com.jointem.fire.utils.LocationWrap;
import com.jointem.plugin.request.GetInterfaceConfig;
import com.jointem.plugin.request.RetrofitClient;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;


public class FireApplication extends BaseApplication {
    private static Context applicationContext;
    public static LocateCity locateCity;// 当前定位到的城市

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this.getApplicationContext();
        LocationWrap.getInstance();
        SDKInitializer.initialize(getApplicationContext());
        initLog();
        RetrofitClient.initContext(getContextFromApplication(), !GetInterfaceConfig.isReleaseEnvironment);

    }

    public static Context getContextFromApplication() {
        return applicationContext;
    }

    private void initLog() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodOffset(0)
                .tag("hgp")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return !GetInterfaceConfig.isReleaseEnvironment;
            }
        });
    }
}
