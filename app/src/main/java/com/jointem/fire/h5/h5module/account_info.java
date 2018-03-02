package com.jointem.fire.h5.h5module;

import android.content.Context;
import android.text.TextUtils;

import com.jointem.dbhelper.GreenDaoManager;
import com.jointem.dbhelper.UserInfo;
import com.jointem.plugin.request.util.GsonUtils;
import com.jointem.fire.UserInfoHelper;
import com.jointem.fire.activity.LoginActivity;
import com.jointem.fire.bean.UserInfoForH5;
import com.jointem.fire.h5.CallBack4H5;
import com.jointem.fire.h5.ant.JCallBack;
import com.jointem.fire.h5.ant.JContext;
import com.jointem.fire.h5.ant.Param;

import java.util.HashMap;

/**
 * Created by thc on 2017/5/15.
 */

public class account_info {
    public static void login(@JContext Context context, @Param("account") String account){
       LoginActivity.startLoginActivity(context, account, true);
    }

    public static void logout(@JContext Context context, @JCallBack CallBack4H5 callBack4H5){
//        new MoreZybPresenter(context).loginOut();
//        String jsonStr = buildJsonStr("0", "退出成功");
//        callBack4H5.onSuccess(jsonStr);
    }

    public static void getUserInfo(@JContext Context context,@Param("account") String account, @Param("forceLogin") Integer forceLogin, @JCallBack CallBack4H5 callBack4H5){
        UserInfo userInfo = UserInfoHelper.getInstance().getUserInfo(context);
        String forceLoginStr = String.valueOf(forceLogin);
        if(userInfo == null){
            if (TextUtils.equals(forceLoginStr, "1")) {//强制登录
                LoginActivity.startLoginActivity(context, account, true);
            } else if (TextUtils.equals(forceLoginStr, "0")) { //非强制登录
                callBack4H5.onFailed(buildJsonStr("-1", "用户未登录"));
            }
        } else {
            userInfoSuccess(userInfo, callBack4H5);
        }
    }

    private static void userInfoSuccess(UserInfo userInfo, CallBack4H5 callBack4H5) {
        if (userInfo == null) return;
        UserInfoForH5 userInfoForH5 = new UserInfoForH5();
        userInfoForH5.setId(userInfo.getUserId());
        userInfoForH5.setAccessToken(userInfo.getToken());
        userInfoForH5.setHeadImg(userInfo.getHeadImg());
        userInfoForH5.setUserName(userInfo.getUserName());
        userInfoForH5.setNickName(userInfo.getNickName());
        String userInfoJsonStr = GsonUtils.getInstance().toJsonString(userInfoForH5);
//      "javascript:"+js方法的名字＋方法的参数值拼接成一个字符串
//       doJavaScript("javascript: " + h5InfoBean.getSuccess() + "('" + s + "')");
        callBack4H5.onSuccess(userInfoJsonStr);
    }

    public static void cleanUserInfo(@JContext Context context, @JCallBack CallBack4H5 callBack4H5){
        UserInfoHelper.getInstance().setUserInfo(null);
        GreenDaoManager.getInstance(context).getDaoSession().getUserInfoDao().deleteAll();
        callBack4H5.onSuccess(buildJsonStr("0", "调用成功"));
    }

    private static String buildJsonStr(String code, String message) {
        HashMap<String, String> stringHashMap = new HashMap<>();
        if (!TextUtils.isEmpty(code)) {
            stringHashMap.put("code", code);
        }
        if (!TextUtils.isEmpty(message)) {
            stringHashMap.put("message", message);
        }
        String jsonString = null;
        if(!stringHashMap.isEmpty()){
            jsonString = GsonUtils.getInstance().toJsonString(stringHashMap);
        }
        return jsonString;
    }
}
