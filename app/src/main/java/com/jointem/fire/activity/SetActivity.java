package com.jointem.fire.activity;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jointem.base.MvpBaseActivity;
import com.jointem.base.util.AlertDialogHelper;
import com.jointem.base.util.DialogCallBack;
import com.jointem.fire.IView.IViewSet;
import com.jointem.fire.R;
import com.jointem.fire.UserInfoHelper;
import com.jointem.fire.presenter.SetPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置
 */
public class SetActivity extends MvpBaseActivity<IViewSet, SetPresenter> implements IViewSet {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_sub_title)
    TextView tvTitle;
    @BindView(R.id.btn_login)
    Button btnLoginOut;

    @Override
    public void setPresenter() {
        mPresenter = new SetPresenter(context);
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.aty_set);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        tvTitle.setText(R.string.setting);
        btnLoginOut.setText("退出登录");
        btnLoginOut.setEnabled(true);
        btnLoginOut.setVisibility(UserInfoHelper.getInstance().getUserInfo(context) == null ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnLoginOut.setVisibility(UserInfoHelper.getInstance().getUserInfo(context) == null ? View.GONE : View.VISIBLE);
    }

    @Override
    @OnClick({R.id.rl_back, R.id.btn_login})
    protected void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.btn_login://退出登录
                AlertDialogHelper adh = AlertDialogHelper.getInstance();
                adh.buildSimpleDialog(context, "确定要退出登录吗？", new DialogCallBack() {
                    @Override
                    public void onSure(String tag) {
                        mPresenter.loginOut();
                    }
                });
                adh.setSureText(getString(R.string.exit));
                adh.setCancelText(getString(R.string.cancel));
                break;
        }
    }

}
