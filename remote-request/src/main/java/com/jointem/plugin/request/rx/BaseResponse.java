package com.jointem.plugin.request.rx;

import android.text.TextUtils;

import com.jointem.plugin.request.NetConstants;


/**
 * @author Kevin.Li
 * @Description: 网络请求响应参数模型实体类--基类
 * @date 2015-10-20 下午3:30:26
 */
public class BaseResponse<T> {
    protected String code; // 响应码
    protected String msg; // 响应消息
    protected T data; // 数据

    BaseResponse(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isOk(){
        return TextUtils.equals(code, NetConstants.SUCCESS);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Response{ ");
        if (code != null) {
            sb.append(code).append("|");
        }
        if (msg != null) {
            sb.append(msg).append("|");
        }
        if (data != null) {
            sb.append(data.toString()).append("|");
        } else {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}");
        return sb.toString();
    }
}
