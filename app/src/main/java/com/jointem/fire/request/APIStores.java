package com.jointem.fire.request;

import com.jointem.dbhelper.UserInfo;
import com.jointem.plugin.request.rx.BaseResponse;
import com.jointem.fire.param.ReqParamsFindPassword;
import com.jointem.fire.param.ReqParamsGainCode;
import com.jointem.fire.param.ReqParamsLogin;
import com.jointem.fire.param.ReqParamsRegister;
import com.jointem.fire.param.ReqParamsToken;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by thc on 2017/10/12.
 */

public class APIStores {
    public static final String HGP_PROJECT = "zyb/public/user/system/";
    private static final String USER_PROJECT_TOKEN = HGP_PROJECT + "token/";

    public interface UserInfoService{
        @POST(HGP_PROJECT + APIS.LOGIN)
        Flowable<BaseResponse<UserInfo>> login(@Body ReqParamsLogin reqParamsLogin);

        @POST(USER_PROJECT_TOKEN + APIS.LOGOUT)
        Flowable<BaseResponse<Object>> logOut(@Body ReqParamsToken reqParamsToken);
    }

    public interface RegisterService {

        @POST(HGP_PROJECT + APIS.REGISTER)
        Flowable<BaseResponse<Object>> register(@Body ReqParamsRegister reqParamsRegister);

        @POST(HGP_PROJECT + APIS.GAIN_IDENTIFY_CODE)
        Flowable<BaseResponse<Object>> gainIdentifyCode(@Body ReqParamsGainCode reqParamsGainCode);

        @POST(HGP_PROJECT + APIS.FIND_PASSWORD)
        Flowable<BaseResponse<Object>> findPassword(@Body ReqParamsFindPassword reqParamsFindPassword);
    }


}
