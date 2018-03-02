package com.jointem.fire.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jointem.base.MvpBaseActivity;
import com.jointem.base.bean.Event;
import com.jointem.base.constant.EventConstants;
import com.jointem.base.util.AlertDialogHelper;
import com.jointem.base.util.DensityUtils;
import com.jointem.base.util.DialogCallBack;
import com.jointem.base.util.ProofUtil;
import com.jointem.base.util.SharedPreferencesHelper;
import com.jointem.base.util.StringUtils;
import com.jointem.base.util.SystemTool;
import com.jointem.base.util.UiUtil;
import com.jointem.base.view.ClearEditText;
import com.jointem.fire.IView.IViewLogin;
import com.jointem.fire.R;
import com.jointem.fire.param.ReqParamsLogin;
import com.jointem.fire.presenter.LoginPresenter;
import com.jointem.fire.utils.CipherUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends MvpBaseActivity<IViewLogin, LoginPresenter> implements IViewLogin {
    public static final int REQUEST_REGISTER = 0x01;
    public static final int REQUEST_FIND_PW = 0x02;
    /**
     * 重新登录请求码
     */
    public static final int REQUEST_RE_LOGIN = 0x08;
    private static final String TAG_GO_REGISTER = "tag_go_register";
    private static final String TAG_FIND_PASSWORD = "tag_find_password";

    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_sub_title)
    TextView tvTitle;
    @BindView(R.id.et_phone)
    ClearEditText etPhone;
    @BindView(R.id.divider_line_anchor)
    View dividerLineAnchor;
    @BindView(R.id.et_password)
    ClearEditText etPassword;
    @BindView(R.id.cb_history_account)
    CheckBox cbHistoryAccount;
    @BindView(R.id.cb_eye_password)
    CheckBox cbEyePassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_find_password)
    TextView tvFindPassword;

    private PopupWindow historyAccountPop;
    private List<String> historyAccount;
    private String mPhone;
    private String mPassword;
    private boolean isLoginSuccess;
    private boolean isPostEventBus;

    public static void startLoginActivity(Context context) {
        Intent loginIntent = new Intent(context, LoginActivity.class);
        context.startActivity(loginIntent);
    }

    /**
     * @param account 账号
     *                <p/>
     *                author: Kevin.Li
     *                created at 2017/5/8 14:16
     */
    public static void startLoginActivity(Context context, @Nullable String account) {
        Intent loginIntent = new Intent(context, LoginActivity.class);
        context.startActivity(loginIntent);
    }

    /**
     * @param account        账号
     * @param isPostEventBus 是否发出Eventbus消息
     *                       <p/>
     *                       author: Kevin.Li
     *                       created at 2017/5/8 14:14
     */
    public static void startLoginActivity(Context context, @Nullable String account, boolean isPostEventBus) {
        Intent loginIntent = new Intent(context, LoginActivity.class);
        loginIntent.putExtra("account", account);
        loginIntent.putExtra("isPostEventBus", isPostEventBus);
        context.startActivity(loginIntent);
    }

    /**
     * @param requestCode 请求码
     *                    <p/>
     *                    author: Kevin.Li
     *                    created at 2017/5/8 14:53
     */
    public static void startLoginActivityForResult(Activity aty, int requestCode) {
        Intent loginIntent = new Intent(aty, LoginActivity.class);
        aty.startActivityForResult(loginIntent, requestCode);
    }

    /**
     * 主要是用于账号异常下线需重新登录的情况
     *
     * @param account     账号
     * @param requestCode 请求码
     */
    public static void reLogin(Activity activity, @Nullable String account, int requestCode) {
        Intent loginIntent = new Intent(activity, LoginActivity.class);
        loginIntent.putExtra("account", account);
        loginIntent.putExtra("isReLogin", true);
        activity.startActivityForResult(loginIntent, requestCode);
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.aty_login);
    }

    @Override
    public void initData() {
        super.initData();
        etPhone.addTextChangedListener(new MyTextWatcher());
        etPassword.addTextChangedListener(new MyTextWatcher());
        isPostEventBus = getIntent().getBooleanExtra("isPostEventBus", false);
        initHistoryAccount();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        tvTitle.setText(R.string.login);
        etPhone.setHint(R.string.prompt_login_mobile);
    }

    @OnClick(R.id.ll_root)
    void dismissPopupWindow() {
        if (historyAccountPop != null) {
            historyAccountPop.dismiss();
        }
    }

    @OnClick(R.id.rl_back)
    void back() {
        if (!isLoginSuccess) {
            EventBus.getDefault().post(new Event(EventConstants.LOGIN_IN_FAILED, null));
        }
        finish();
    }

    @OnClick(R.id.et_phone)
    void cursorVisible() {
        etPhone.setCursorVisible(true);
    }

    /**
     * 触发历史登录账号的弹窗
     */
    @OnClick(R.id.cb_history_account)
    void triggerPopupWindow() {
        if (cbHistoryAccount.isChecked()) {
            if (historyAccountPop == null) {
                View view = getLayoutInflater().inflate(R.layout.pw_user_account_history, rlBack, false);
                ListView lvAccountHistory = (ListView) view.findViewById(R.id.lv_account_history);
                ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.item_user_account_history, R.id.tv_account_history, historyAccount);
                lvAccountHistory.setAdapter(adapter);
                lvAccountHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        etPhone.setText(historyAccount.get(position));
                        etPhone.setSelection(etPhone.getText().length());
                        historyAccountPop.dismiss();
                    }
                });
                historyAccountPop = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                historyAccountPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        cbHistoryAccount.setChecked(false);
                    }
                });
            }
            historyAccountPop.showAsDropDown(dividerLineAnchor);
        } else if (historyAccountPop != null) {
            historyAccountPop.dismiss();
        }
    }

    @OnClick(R.id.cb_eye_password)
    public void transformPassword() {
        if (cbEyePassword.isChecked())
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance()); //明文
        else
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance()); //密文
        etPassword.setSelection(etPassword.getText().length());
    }

    @OnClick(R.id.tv_register)
    void goRegister() {
        RegisterActivity.startRegisterActivity(this, RegisterActivity.FLAG_REGISTER);
    }

    @OnClick(R.id.tv_find_password)
    void goFindPassword() {
        RegisterActivity.startRegisterActivity(this, RegisterActivity.FLAG_FIND_PASSWORD);
    }


    /**
     * 处理登录的逻辑
     */
    @OnClick(R.id.btn_login)
    public void dealLogin() {
        if (!ProofUtil.isPhoneValid(mPhone)) {
            AlertDialogHelper.getInstance().buildConfirmDialog(context, getString(R.string.invalid_mobile));
            return;
        }
        if (!ProofUtil.isPasswordValid(mPassword)) {
            AlertDialogHelper.getInstance().buildConfirmDialog(context, getString(R.string.invalid_password));
            return;
        }
        if (!SystemTool.isNetworkConnected(context)) {
            AlertDialogHelper.getInstance().buildConfirmDialog(context, context.getString(R.string.no_network));
            return;
        }
        String encryptionPW = CipherUtils.md5(mPassword);
        ReqParamsLogin reqParamsLogin = new ReqParamsLogin(mPhone, encryptionPW, ReqParamsLogin.UserType.TYPE_MOBILE);
        mPresenter.login(reqParamsLogin);
    }

    /**
     * 初始化历史登录账号的信息
     */
    private void initHistoryAccount() {
        historyAccount = (List<String>) SharedPreferencesHelper.readObject(context, SharedPreferencesHelper.KEY_HISTORY_ACCOUNT_INFO);
        if (historyAccount == null || historyAccount.size() < 1) {//不存在成功登录的历史账号
            cbHistoryAccount.setVisibility(View.GONE);
            etPhone.setPadding(0, 0, DensityUtils.dip2px(context, 16), 0);
        } else {
            cbHistoryAccount.setVisibility(View.VISIBLE);
            etPhone.setPadding(0, 0, 0, 0);
            etPhone.setText(historyAccount.get(0));
        }

        //获取web端传过来的参数
        String account = getIntent().getStringExtra("account");
        if (!StringUtils.isEmpty(account)) {
            etPhone.setText(account);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSwipeBackEnable(false);
    }

    @Override
    public void onBackPressed() {
        if (!isLoginSuccess) {
            EventBus.getDefault().post(new Event(EventConstants.LOGIN_IN_FAILED, null));
        }
        super.onBackPressed();
    }

    @Override
    public void loginSuccess() {
        isLoginSuccess = true;
        UiUtil.hideSoftInputFromWindow(this);
        if (isPostEventBus) {//H5调用登录
            EventBus.getDefault().post(new Event(EventConstants.LOGIN_IN_SUCCESS, null));
        }

        if (getIntent().getBooleanExtra("isReLogin", false) && historyAccount != null && !mPhone.equals(historyAccount.get(0))) {
            //如果重新登录的账号不是上次登录的账号
//            startActivity(new Intent(this, MainActivity.class));
            Intent intent = new Intent();
            intent.setClassName(this, "com.jointem.hgp.activity.MainActivity");
            startActivity(intent);
        } else {
            //默认情况下关闭当前页面
            setResult(RESULT_OK);
            finish();
        }
        //默认情况下关闭当前页面
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void loginFailure(String errorCode, String message) {
        isLoginSuccess = false;
        AlertDialogHelper.getInstance().setCancelOutSide(false);
        if (isPostEventBus) {//H5调用登录
            EventBus.getDefault().post(new Event(EventConstants.LOGIN_IN_FAILED, null));
        }
        switch (errorCode) {
            case "100001"://账户不存在
                AlertDialogHelper.getInstance().buildSimpleDialog(context, message, TAG_GO_REGISTER, mDialogCallback);
                AlertDialogHelper.getInstance().setCancelText(getString(R.string.go_register));
                break;
            case "100002"://用户名或密码错误
                AlertDialogHelper.getInstance().buildSimpleDialog(context, message, TAG_FIND_PASSWORD, mDialogCallback);
                AlertDialogHelper.getInstance().setCancelText(getString(R.string.find_password));
                break;
            default:
                if (!TextUtils.isEmpty(message))
                    AlertDialogHelper.getInstance().buildConfirmDialog(context, message);
                break;
        }
    }

    private DialogCallBack mDialogCallback = new DialogCallBack() {

        @Override
        public void onSure(String tag) {
        }

        @Override
        public void onCancel(String tag) {
            super.onCancel(tag);
            if (tag.equals(TAG_GO_REGISTER)) {
                RegisterActivity.startRegisterActivityForResult(LoginActivity.this, RegisterActivity.FLAG_REGISTER, REQUEST_REGISTER);
            }
            if (tag.equals(TAG_FIND_PASSWORD)) {
                RegisterActivity.startRegisterActivityForResult(LoginActivity.this, RegisterActivity.FLAG_FIND_PASSWORD, REQUEST_FIND_PW);
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_REGISTER || requestCode == REQUEST_FIND_PW) {
                //携带注册成功时的账号到登录界面
                etPhone.setText(data.getStringExtra("account"));
                etPassword.setText("");
            }
        }
    }

    @Override
    public void setPresenter() {
        mPresenter = new LoginPresenter(this);
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
            if (!TextUtils.isEmpty(mPhone) && !TextUtils.isEmpty(mPassword)) {
                btnLogin.setEnabled(true);
            } else if (btnLogin.isEnabled()) {
                btnLogin.setEnabled(false);
            }
        }
    }
}

