package com.jointem.base.util;

import java.util.regex.Pattern;

/**
 * Description :校验规则的工具类，方便以后修改规则
 * Created by wyd on 2016/11/21.
 */

public class ProofUtil {

    private final static Pattern phoneRule = Pattern.compile("^1[34578]\\d{9}$");
    private final static Pattern telRule = Pattern.compile("^(\\d{3,4}-)\\d{7,8}$");
    private final static Pattern emailRule = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    private final static Pattern passwordRule = Pattern.compile("^[@A-Za-z0-9!#\\$%\\^&\\*\\.~]{6,22}$");
    private final static Pattern codeRule = Pattern.compile("^[0-9]{6}$");
    private final static Pattern IDNumRule = Pattern.compile("^\\d{17}([0-9]|X)$");

    /**
     * 校验是否为合法的手机号
     */
    public static boolean isPhoneValid(String phoneNum) {
        return phoneRule.matcher(phoneNum).matches();
    }

    /**
     * 校验是否为合法的电话号码
     */
    public static boolean isTelValid(String telNum) {
        return telRule.matcher(telNum).matches();
    }

    /**
     * 校验是否为合法的邮箱
     */
    public static boolean isEmailValid(String email) {
        return emailRule.matcher(email).matches();
    }

    /**
     * 校验密码是否符合规则
     */
    public static boolean isPasswordValid(String password) {
        return passwordRule.matcher(password).matches();
    }

    /**
     * 校验验证码的格式
     */
    public static boolean isCodeValid(String identifyCode) {
        return codeRule.matcher(identifyCode).matches();
    }

    /**
     * 校验字符串是否为身份证格式
     */
    public static boolean isIDNumValid(String IDNum) {
        return IDNumRule.matcher(IDNum).matches();
    }

}
