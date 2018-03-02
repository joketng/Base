package com.jointem.fire.presenter;

import android.app.NotificationManager;
import android.content.Context;

import com.jointem.base.BaseActivity;
import com.jointem.base.presenter.BasePresenter;
import com.jointem.dbhelper.UserInfo;
import com.jointem.plugin.request.RetrofitClient;
import com.jointem.plugin.request.rx.BaseSubscriber;
import com.jointem.plugin.request.rx.RxHelper;
import com.jointem.fire.IView.IViewSet;
import com.jointem.fire.UserInfoHelper;
import com.jointem.fire.activity.LoginActivity;
import com.jointem.fire.param.ReqParamsToken;
import com.jointem.fire.request.APIStores;


public class SetPresenter extends BasePresenter<IViewSet> {

    public SetPresenter(Context context) {
        super(context);
    }

    @Override
    public void initData() {
    }

    /**
     * 退出登录
     */
    public void loginOut() {
        // 移除通知栏的通知
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        ReqParamsToken reqParamsToken = new ReqParamsToken();
        String token = "";
        UserInfo userInfo = UserInfoHelper.getInstance().getUserInfo(context);
        if (userInfo != null) {
            token = userInfo.getToken();
            reqParamsToken.setAccessToken(token);

            RetrofitClient.getInstance().create(APIStores.UserInfoService.class).logOut(reqParamsToken)
                    .compose(RxHelper.<Object>handleResult(((BaseActivity) context).lifecycleSubject))
                    .subscribe(new BaseSubscriber<Object>(context) {
                        @Override
                        protected void _onNext(Object obj) {
                            LoginActivity.startLoginActivity(context);
                        }
                    });
            //删除内存和持久化的用户数据
            UserInfoHelper.getInstance().cleanLocalUserInfo(context, userInfo.getUserId());
        }
    }

}
