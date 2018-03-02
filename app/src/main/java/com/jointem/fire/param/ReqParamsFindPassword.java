package com.jointem.fire.param;

import com.jointem.base.param.ReqParams;



public class ReqParamsFindPassword extends ReqParams {
    private String mobile;       //手机号码
    private String smsCode;      //手机短信验证号码
    private String password;     //新密码

    public ReqParamsFindPassword(String mobile, String smsCode, String password) {
        super();
        this.mobile = mobile;
        this.smsCode = smsCode;
        this.password = password;
    }
}
