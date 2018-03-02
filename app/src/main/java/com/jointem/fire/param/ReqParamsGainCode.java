package com.jointem.fire.param;

import android.support.annotation.StringDef;

import com.jointem.base.param.ReqParams;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class ReqParamsGainCode extends ReqParams {
    public static final String TYPE_REGISTER = "0";
    public static final String TYPE_FIND_PASSWORD = "1";

    private String mobile;       //手机号码
    private String type;         //短信类型(0-注册用户 ;1-找回密码)

    public ReqParamsGainCode(@SmsType String type, String mobile) {
        super();
        this.type = type;
        this.mobile = mobile;
    }

    @StringDef({TYPE_REGISTER, TYPE_FIND_PASSWORD})
    @Retention(RetentionPolicy.SOURCE)
    @interface SmsType {
    }
}
