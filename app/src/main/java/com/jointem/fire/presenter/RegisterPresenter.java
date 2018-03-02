package com.jointem.fire.presenter;

import android.content.Context;

import com.jointem.base.BaseActivity;
import com.jointem.base.presenter.BasePresenter;
import com.jointem.plugin.request.RetrofitClient;
import com.jointem.plugin.request.rx.BaseSubscriber;
import com.jointem.plugin.request.rx.RxHelper;
import com.jointem.fire.IView.IViewRegister;
import com.jointem.fire.param.ReqParamsFindPassword;
import com.jointem.fire.param.ReqParamsGainCode;
import com.jointem.fire.param.ReqParamsRegister;
import com.jointem.fire.request.APIStores;


public class RegisterPresenter extends BasePresenter<IViewRegister> {
    private IViewRegister iViewRegister;
    private APIStores.RegisterService registerService;

    public RegisterPresenter(Context mContext) {
        super(mContext);
    }

    @Override
    public void initData() {
        iViewRegister = getView();
        registerService = RetrofitClient.getInstance().create(APIStores.RegisterService.class);
    }

    public void register(ReqParamsRegister reqParamsRegister) {
        registerService.register(reqParamsRegister)
                .compose(RxHelper.<Object>handleResult(((BaseActivity) context).lifecycleSubject))
                .subscribe(new BaseSubscriber<Object>(context) {
                    @Override
                    protected void _onNext(Object obj) {
                        iViewRegister.registerSuccess();
                    }

                    @Override
                    protected void _onError(String code, String message) {
                        super._onError(code, message);
                        iViewRegister.registerFailure(code, message);
                    }
                }.showProgressDialog(context));
    }

    public void findPassword(ReqParamsFindPassword findPassword) {
        registerService.findPassword(findPassword)
                .compose(RxHelper.<Object>handleResult(((BaseActivity) context).lifecycleSubject))
                .subscribe(new BaseSubscriber<Object>(context) {
                    @Override
                    protected void _onNext(Object obj) {
                        iViewRegister.registerSuccess();
                    }

                    @Override
                    protected void _onError(String code, String message) {
                        super._onError(code, message);
                        iViewRegister.registerFailure(code, message);
                    }
                }.showProgressDialog(context));
    }

    public void gainIdentifyCode(ReqParamsGainCode reqParamsGainCode) {
        registerService.gainIdentifyCode(reqParamsGainCode)
                .compose(RxHelper.<Object>handleResult(((BaseActivity) context).lifecycleSubject))
                .subscribe(new BaseSubscriber<Object>(context) {
                    @Override
                    protected void _onNext(Object identifyCode) {
                        iViewRegister.identifyCodeButton(null);
                    }

                    @Override
                    protected void _onError(String code, String message) {
                        super._onError(code, message);
                        iViewRegister.identifyCodeButton(code);
                    }
                }.showProgressDialog(context));
    }
}
