package com.jointem.fire;

import android.content.Context;
import android.support.annotation.Nullable;

import com.jointem.dbhelper.UserInfo;
import com.jointem.fire.model.UserInfoModel;
import com.jointem.fire.model.UserInfoModelImpl;

public final class UserInfoHelper {
    private static UserInfoHelper helper = null;
    private static UserInfo mUserInfo;

    private UserInfoHelper() {
    }

    public static UserInfoHelper getInstance() {
        if (helper == null) {
            synchronized (UserInfoHelper.class) {
                helper = new UserInfoHelper();
            }
        }
        return helper;
    }

    /**
     * 判断用户是否处于登录状态,并自动处理跳转逻辑和UI变化
     *
     * @return true——登录  false——离线
     */
    public boolean isOnline(final Context context) {
        return getUserInfo(context) != null;
    }

    /**
     * @param context 上下文
     * @return 用户信息
     */
    public
    @Nullable
    UserInfo getUserInfo(Context context) {
        if (mUserInfo != null) {
            return mUserInfo;
        }
        UserInfoModel userInfoModel = new UserInfoModelImpl(context);
        mUserInfo = userInfoModel.queryUserInfos();
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    /**
     * 退出登录
     */
    public void cleanLocalUserInfo(Context context, String uId) {
        //删除内存和持久化的用户数据
        UserInfoModel userInfoModel = new UserInfoModelImpl(context);
        if (uId == null) {
            userInfoModel.cleanUserInfo();
        } else {
            userInfoModel.deleteUserInfo(uId);
        }
        mUserInfo = null;
    }
}
