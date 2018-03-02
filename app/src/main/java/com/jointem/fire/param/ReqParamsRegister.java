package com.jointem.fire.param;



public class ReqParamsRegister extends ReqParamsLogin {
    private String smsCode;   //手机验证码


    public ReqParamsRegister(String userName, String password, @UserType String type, String smsCode) {
        super(userName, password, type);
        this.smsCode = smsCode;
    }
}
