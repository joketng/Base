package com.jointem.fire.request;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by thc on 2017/10/12.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface APIS {
    /**
     * 登录
     */
    String LOGIN = "login";
    /**
     * 退出登录
     */
    String LOGOUT = "logout";
    /**
     * 注册
     */
    String REGISTER = "registerUser";

    /**
     * 获取短信验证码
     */
    String GAIN_IDENTIFY_CODE = "getSmsCode";
    /**
     * 找回密码/忘记密码
     */
    String FIND_PASSWORD = "resetPassword";




}
