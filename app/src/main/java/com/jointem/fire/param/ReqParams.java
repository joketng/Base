package com.jointem.fire.param;


import com.jointem.base.BaseApplication;


public class ReqParams {
    protected String system = "01"; // 操作系统 0-pc；1-Android；2-ios
    protected String imei;// Android手机唯一标识码
    protected String currentVersion;//当前客户端版本
    protected String systemVersion;// 操作系统版本号
    protected String model;// 手机型号

    public ReqParams() {
        currentVersion = BaseApplication.appVersionName;
        imei = BaseApplication.imei;
        model = BaseApplication.model;
        systemVersion = BaseApplication.systemVersion;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
