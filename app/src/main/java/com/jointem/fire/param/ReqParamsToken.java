package com.jointem.fire.param;

import android.content.Context;

import com.jointem.base.param.ReqParams;
import com.jointem.dbhelper.UserInfo;
import com.jointem.fire.UserInfoHelper;


public class ReqParamsToken extends ReqParams {
    protected String accessToken;

    public ReqParamsToken() {
    }

    public ReqParamsToken(Context context) {
        UserInfo userInfo = UserInfoHelper.getInstance().getUserInfo(context);
        if (userInfo != null) {
            this.accessToken = userInfo.getToken();
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
