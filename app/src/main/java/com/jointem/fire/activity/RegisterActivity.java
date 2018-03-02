package com.jointem.fire.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.IntDef;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jointem.base.MvpBaseActivity;
import com.jointem.base.util.AlertDialogHelper;
import com.jointem.base.util.DensityUtils;
import com.jointem.base.util.IDialogCallBack;
import com.jointem.base.util.ProofUtil;
import com.jointem.base.util.SystemTool;
import com.jointem.base.util.UiUtil;
import com.jointem.base.view.ClearEditText;
import com.jointem.fire.IView.IViewRegister;
import com.jointem.fire.R;
import com.jointem.fire.param.ReqParamsFindPassword;
import com.jointem.fire.param.ReqParamsGainCode;
import com.jointem.fire.param.ReqParamsLogin;
import com.jointem.fire.param.ReqParamsRegister;
import com.jointem.fire.presenter.RegisterPresenter;
import com.jointem.fire.utils.CipherUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.OnClick;




public class RegisterActivity extends MvpBaseActivity<IViewRegister, RegisterPresenter> implements IViewRegister {
    private static final String CHOOSE_JUMP = "choose_jump";
    private static final String TAG_GO_LOGIN = "tag_go_login";
    public static final int FLAG_REGISTER = 0;
    public static final int FLAG_FIND_PASSWORD = 1;

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_sub_title)
    TextView tvTitle;
    @BindView(R.id.et_phone)
    ClearEditText etPhone;
    @BindView(R.id.et_identify_code)
    ClearEditText etIdentifyCode;
    @BindView(R.id.btn_gain_code)
    Button btnGainCode;
    @BindView(R.id.et_password)
    ClearEditText etPassword;
    @BindView(R.id.cb_history_account)
    CheckBox cbHistoryAccount;
    @BindView(R.id.cb_eye_password)
    CheckBox cbEyePassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private String mPhone;
    private String mPassword;
    private String mCode;
    private boolean isCountDown; //获取验证码是否还在倒计时，在倒计时生效的时候不应该监听textChange
    private int flag; //用于区分注册和找回密码(0--注册,1--找回密码)
    private MyCountDownTimer countDownTimer;

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.aty_register);
    }

    @Override
    public void initData() {
        super.initData();
        flag = getIntent().getIntExtra(CHOOSE_JUMP, FLAG_REGISTER);
        etPhone.addTextChangedListener(new MyTextWatcher());
        etPassword.addTextChangedListener(new MyTextWatcher());
        etIdentifyCode.addTextChangedListener(new MyTextWatcher());
    }

    @Override
    public void initWidget() {
        super.initWidget();
        if (flag == FLAG_FIND_PASSWORD) {//找回密码
            tvTitle.setText(R.string.find_password);
            btnLogin.setText(R.string.complete);
        } else {//注册
            tvTitle.setText(R.string.register_free);
            btnLogin.setText(R.string.register_immediately);
        }
        cbHistoryAccount.setVisibility(View.GONE);
        etPhone.setPadding(0, 0, DensityUtils.dip2px(context, 16), 0);
    }

    @OnClick(R.id.rl_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.et_phone)
    public void cursorVisible() {
        etPhone.setCursorVisible(true);
    }

    @OnClick(R.id.btn_gain_code)
    public void gainCode() {//获取验证码
        if (!ProofUtil.isPhoneValid(mPhone)) {
            AlertDialogHelper.getInstance().buildConfirmDialog(context, getString(R.string.invalid_mobile));
            return;
        }
        if (SystemTool.isNetworkConnected(this)) {
            btnGainCode.setText(R.string.gaining);
            btnGainCode.setEnabled(false);
        }
        ReqParamsGainCode reqParamsCode;
        if (flag == FLAG_REGISTER) {
            reqParamsCode = new ReqParamsGainCode(ReqParamsGainCode.TYPE_REGISTER, mPhone);
        } else {
            reqParamsCode = new ReqParamsGainCode(ReqParamsGainCode.TYPE_FIND_PASSWORD, mPhone);
        }
        mPresenter.gainIdentifyCode(reqParamsCode);
    }

    @OnClick(R.id.cb_eye_password)
    public void transformPassword() {
        if (cbEyePassword.isChecked())
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance()); //明文
        else
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance()); //密文
        etPassword.setSelection(etPassword.getText().length());
    }

    @OnClick(R.id.btn_login)
    public void register() {//立即注册/ 找回密码
        if (!ProofUtil.isPhoneValid(mPhone)) {
            AlertDialogHelper.getInstance().buildConfirmDialog(context, getString(R.string.invalid_mobile));
            return;
        }
        if (!ProofUtil.isCodeValid(mCode)) {
            AlertDialogHelper.getInstance().buildConfirmDialog(context, getString(R.string.invalid_identify_code));
            return;
        }
        if (!ProofUtil.isPasswordValid(mPassword)) {
            AlertDialogHelper.getInstance().buildConfirmDialog(context, getString(R.string.invalid_password));
            return;
        }
        String encryptionPW = CipherUtils.md5(mPassword);
        if (flag == FLAG_REGISTER) {
            ReqParamsRegister reqParamsRegister = new ReqParamsRegister(mPhone, encryptionPW, ReqParamsLogin.UserType.TYPE_MOBILE, mCode);
            mPresenter.register(reqParamsRegister);
        } else {
            ReqParamsFindPassword findPassword = new ReqParamsFindPassword(mPhone, mCode, encryptionPW);
            mPresenter.findPassword(findPassword);
        }
    }

    /**
     * @param activity 上下文
     * @param flag     0—— 启动注册界面 ， 1——启动找回密码界面
     */
    public static void startRegisterActivity(Activity activity, @JumpFlag int flag) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        intent.putExtra(CHOOSE_JUMP, flag);
        activity.startActivity(intent);
    }

    /**
     * @param activity    上下文
     * @param flag        0—— 启动注册界面 ， 1——启动找回密码界面
     * @param requestCode 界面跳转请求码
     */
    public static void startRegisterActivityForResult(Activity activity, @JumpFlag int flag, int requestCode) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        intent.putExtra(CHOOSE_JUMP, flag);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void registerSuccess() {
        setResult(Activity.RESULT_OK, new Intent().putExtra("account", etPhone.getText().toString()));
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        UiUtil.hideSoftInputFromWindow(this);
        finish();
    }

    @Override
    public void registerFailure(String errorCode, String msg) {
        switch (errorCode) {
            case "100003":
                AlertDialogHelper.getInstance().buildSimpleDialog(context, getString(R.string.account_exit), TAG_GO_LOGIN, mDialogCallBack);
                AlertDialogHelper.getInstance().setCancelText(getString(R.string.go_login));
                break;
            case "100005":
                AlertDialogHelper.getInstance().buildConfirmDialog(context, getString(R.string.account_no_exit));
                break;
            case "300002":
                AlertDialogHelper.getInstance().buildConfirmDialog(context, getString(R.string.identify_code_error));
                break;
            case "300003":
                AlertDialogHelper.getInstance().buildConfirmDialog(context, getString(R.string.identify_code_past));
                break;
            default:
                if (!TextUtils.isEmpty(msg))
                    AlertDialogHelper.getInstance().buildConfirmDialog(context, msg);
                break;
        }
    }


    private IDialogCallBack mDialogCallBack = new IDialogCallBack() {

        @Override
        public void onSure(String tag) {

        }

        @Override
        public void onCancel(String tag) {
            if (tag.equals(TAG_GO_LOGIN)) {
                setResult(Activity.RESULT_OK, getIntent().putExtra("account", etPhone.getText().toString()));
                UiUtil.hideSoftInputFromWindow(RegisterActivity.this);
                finish();
            }
        }
    };

    @Override
    public void identifyCodeButton(String errorCode) {
        if (errorCode != null) {//请求失败
            btnGainCode.setText(R.string.gain);
            if (!TextUtils.isEmpty(mPhone)) {
                btnGainCode.setEnabled(true);
            }
            switch (errorCode) {
                case "100006":
                    AlertDialogHelper.getInstance().buildSimpleDialog(context, getString(R.string.account_exit), TAG_GO_LOGIN, mDialogCallBack);
                    AlertDialogHelper.getInstance().setCancelText(getString(R.string.go_login));
                    break;
                case "100003":
                    AlertDialogHelper.getInstance().buildSimpleDialog(context, getString(R.string.account_exit_website), TAG_GO_LOGIN, mDialogCallBack);
                    AlertDialogHelper.getInstance().setCancelText(getString(R.string.go_login));
                    break;
                case "100001":
                    AlertDialogHelper.getInstance().buildConfirmDialog(context, "账户不存在");
                    break;
                case "502":
                    AlertDialogHelper.getInstance().buildConfirmDialog(context, getString(R.string.gain_identify_code_failure));
                    break;
                default:
                    AlertDialogHelper.getInstance().buildConfirmDialog(context, "请求失败，请稍后再试。");
                    break;
            }
            return;
        }
        if (countDownTimer == null) {
            countDownTimer = new MyCountDownTimer(60000, 1000);
        } else {
            countDownTimer.cancel();
        }
        isCountDown = true;
        countDownTimer.start();
    }

    @Override
    public void setPresenter() {
        mPresenter = new RegisterPresenter(this);
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mPhone = etPhone.getText().toString().trim();
            mPassword = etPassword.getText().toString();
            mCode = etIdentifyCode.getText().toString().trim();

            //注册(完成)按钮是否可以点击的逻辑处理
            if (TextUtils.isEmpty(mPhone) || TextUtils.isEmpty(mCode) || TextUtils.isEmpty(mPassword)) {
                btnLogin.setEnabled(false);
            } else {
                btnLogin.setEnabled(true);
            }

            //获取验证码按钮是否可以点击的逻辑处理
            if (TextUtils.isEmpty(mPhone)) {
                btnGainCode.setEnabled(false);
            } else if (!isCountDown) {
                btnGainCode.setEnabled(true);
            }
        }
    }

    private class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (btnGainCode != null) {
                btnGainCode.setText(MessageFormat.format("{0}s", millisUntilFinished / 1000));
            }
        }

        @Override
        public void onFinish() {
            isCountDown = false;
            btnGainCode.setEnabled(true);
            btnGainCode.setText(R.string.gain);
        }
    }

    @Target(ElementType.PARAMETER)
    @IntDef(value = {FLAG_REGISTER, FLAG_FIND_PASSWORD})
    @Retention(RetentionPolicy.SOURCE)
    @interface JumpFlag {
    }
}