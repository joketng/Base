package com.jointem.fire.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jointem.base.MvpBaseActivity;
import com.jointem.base.bean.Event;
import com.jointem.base.constant.EventConstants;
import com.jointem.base.iView.IView;
import com.jointem.base.util.CacheRecoveryPersistentData;
import com.jointem.base.util.CommonConstants;
import com.jointem.base.util.PreferenceHelper;
import com.jointem.base.util.StringUtils;
import com.jointem.base.util.SystemTool;
import com.jointem.base.util.UiUtil;
import com.jointem.base.util.Utils;
import com.jointem.base.view.AlwaysMarqueeTextView;
import com.jointem.plugin.request.util.NetUtil;
import com.jointem.fire.IView.StatusCodeView;
import com.jointem.fire.R;
import com.jointem.fire.adapter.MoreAdapter;
import com.jointem.fire.h5.JSBridgeManager;
import com.jointem.fire.h5.JSInterAction;
import com.jointem.fire.present.StatusCodePresent;
import com.jointem.fire.present.WebViewPresenter;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * @Description:
 * @Author:  joketng
 * @Time:  2018/3/2
 */

@SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
public class WebViewActivity extends MvpBaseActivity<IView, WebViewPresenter> implements IView , StatusCodeView {

    @BindView(R.id.imv_back)
    public ImageView imvBack;
    @BindView(R.id.tv_back)
    public TextView tvBack;
    @BindView(R.id.tv_sub_title)
    AlwaysMarqueeTextView tvTitle;// ActionBar标题
    @BindView(R.id.tv_operation)
    TextView tvOperation;
    @BindView(R.id.web_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_loading)
    TextView tvLoading;
    @BindView(R.id.wv_web)
    WebView wvWeb;
    @BindView(R.id.rl_empty_view_net)
    View netView;
    @BindView(R.id.fl_web)
    FrameLayout rlWeb;
    @BindView(R.id.ll_com_link_failed)
    View linkFailedView;
    @BindView(R.id.rl_web_content_view)
    RelativeLayout rootView;
    @BindView(R.id.rl_operation)
    RelativeLayout rlOption;

    /**************
     * 属性
     **********************/
    private String url;
    private String currentUrl;
    private String title;
    private String siteId;
    private int[] icon;
    private String[] text;
    private boolean isFromOnline = false;
    //    private WebViewPresenter webViewPresenter;
    private Context context;
    private boolean isDynamicSite = true;
    private boolean isPush = false;
    private PopupWindow listPopupWindow;
    private StatusCodePresent statusCodePresent;


    public static void startWebViewActivity(Context context, String url, String title, String contentTitle, String contentDes, boolean isDynamic) {
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        bundle.putString("TITLE", title);
        bundle.putString("SHARE_TITLE", contentTitle);
        bundle.putBoolean("ISDYNAMIC", isDynamic);
        bundle.putString("SHARE_DES", contentDes);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startWebViewActivity(Context context, String url, String title, String siteId, boolean isDynamic, String from) {
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        bundle.putString("TITLE", title);
        bundle.putString("SITE_ID", siteId);
        bundle.putBoolean("ISDYNAMIC", isDynamic);
        bundle.putString("FROM", from);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startWebViewActivity(Context context, String url, String title, String siteId) {
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        bundle.putString("TITLE", title);
        bundle.putString("SITE_ID", siteId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startWebViewActivity(Context context, String url, String title, boolean isFromOnline) {
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        bundle.putString("TITLE", title);
        bundle.putBoolean("IS_FROM_ONLINE", isFromOnline);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startWebViewActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        bundle.putString("TITLE", title);
        bundle.putBoolean("ISDYNAMIC", false);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startWebViewActivity(Context context, String url, boolean isDynamic) {
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        bundle.putBoolean("ISDYNAMIC", isDynamic);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void pushWebViewActivity(Context context, String url, String title) {
        Intent webIntent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        bundle.putString("TITLE", title);
        bundle.putBoolean("ISDYNAMIC", false);
        bundle.putBoolean("PUSH", true);
        webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        webIntent.putExtras(bundle);
        context.startActivity(webIntent);
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.aty_webview);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        super.initData();
        context = WebViewActivity.this;
        tvTitle.setText(getString(R.string.loading));
        statusCodePresent = new StatusCodePresent(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            receiveData(bundle);
        }
    }

    /**
     * 接收intent传来的数据
     */
    private void receiveData(Bundle bundle) {

        url = bundle.getString("URL");
        title = bundle.getString("TITLE");
        siteId = bundle.getString("SITE_ID");
        isDynamicSite = bundle.getBoolean("ISDYNAMIC", true);
        isFromOnline = bundle.getBoolean("IS_FROM_ONLINE");
        isPush = bundle.getBoolean("PUSH", false);
        if (isFromOnline) {
            isDynamicSite = false;
        }
    }

    @Override
    public void initWidget() {
        super.initWidget();
        WebSettings settings = wvWeb.getSettings();
        initWebSettings(context, settings);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        wvWeb.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        if (!NetUtil.isNetworkConnected(context)) {
            netView.setVisibility(View.VISIBLE);
        } else {
            netView.setVisibility(View.GONE);
        }

        wvWeb.setWebChromeClient(new CustomWebChromeClient());

        wvWeb.setWebViewClient(new CustomWebViewClient());

        wvWeb.loadUrl(url);
        if (url.startsWith("http:") || url.startsWith("https:")) {
            statusCodeThread(url);
            startProgress90();
        } else {
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Intent intent = new Intent();
                intent.setData(Uri.parse(url));
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setComponent(null);
                context.startActivity(i);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
        javaScriptInterAction();
    }

    public static void initWebSettings(Context context, WebSettings settings) {
        settings.setJavaScriptEnabled(true);//支持js
        settings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setSupportZoom(true);//支持缩放，默认为true。是下面那个的前提。
        settings.setBuiltInZoomControls(false);//设置内置的缩放控件。若为false，则该WebView不可缩放
        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        settings.setDefaultTextEncodingName("utf-8");//设置编码格式
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL); //支持内容重新布局
        settings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        settings.setDatabaseEnabled(true);   //开启 database storage API 功能
        settings.setAppCacheEnabled(true);//开启 Application Caches 功能
        settings.setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
        settings.setGeolocationEnabled(true);//启用地理定位
        //设置定位的数据库路径
        String dir = context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        settings.setGeolocationDatabasePath(dir);
        if (NetUtil.isNetworkConnected(context)) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        }
    }

    private void statusCodeThread(String url) {
        statusCodePresent.getStatusCode(url);
    }

    private ValueCallback mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private String androidMethod = "";
    private JSInterAction jsi;

    private void javaScriptInterAction() {
        jsi = new JSInterAction(activity, wvWeb, rootView) {

//            @JavascriptInterface
//            public void doShare() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        bottomShareWindow();
//                    }
//                });
//            }

            @JavascriptInterface
            public void doCall(String telNumber) {
                Utils.call(context, telNumber);
            }

        };
        // 把JavaScriptInterface对象注入WebView中，同时起一个别名
        // TODO: 2017/10/12  
        wvWeb.addJavascriptInterface(jsi, "ZybJSInterface");
    }

    @Override
    public void setPresenter() {
        mPresenter = new WebViewPresenter(activity);
    }


    @Override
    public void statusCodeSuccess() {

    }

    @Override
    public void statusCodeFailed(int statusCode) {
        if (wvWeb != null) {
            final String url = wvWeb.getUrl();
            if (statusCode != 302 && statusCode != 0) {
                wvWeb.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                if (url.contains("jointem") || url.contains("115.29") || url.contains("172.31")) {
                    linkFailedView.setVisibility(View.VISIBLE);
                } else {
                    linkFailedView.setVisibility(View.VISIBLE);
                    TextView tvLink = (TextView) linkFailedView.findViewById(R.id.tv_com_link_failed);
                    tvLink.setText(getString(R.string.external_link_failed));
                    ImageView imgLink = (ImageView) linkFailedView.findViewById(R.id.img_com_link_failed);
                    imgLink.setImageResource(R.mipmap.ic_external_link_failed);
                    linkFailedView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            wvWeb.loadUrl(url);
                            if (url.startsWith("http:") || url.startsWith("https:")) {
                                statusCodeThread(url);
                                startProgress90();
                            } else {
                                try {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    Intent intent = new Intent();
                                    intent.setData(Uri.parse(url));
                                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                    intent.setComponent(null);
                                    context.startActivity(i);
                                } catch (ActivityNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            } else {
                linkFailedView.setVisibility(View.GONE);
                wvWeb.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PreferenceHelper.write(context, CacheRecoveryPersistentData.PREF_CACHE_FILE_NAME, "WebViewAty_url", url);
        PreferenceHelper.write(context, CacheRecoveryPersistentData.PREF_CACHE_FILE_NAME, "WebViewAty_shareUrl", currentUrl);
        PreferenceHelper.write(context, CacheRecoveryPersistentData.PREF_CACHE_FILE_NAME, "WebViewAty_title", title);
        PreferenceHelper.write(context, CacheRecoveryPersistentData.PREF_CACHE_FILE_NAME, "WebViewAty_siteId", siteId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        url = PreferenceHelper.readString(this, CacheRecoveryPersistentData.PREF_CACHE_FILE_NAME, "WebViewAty_url");
        currentUrl = PreferenceHelper.readString(this, CacheRecoveryPersistentData.PREF_CACHE_FILE_NAME, "WebViewAty_shareUrl");
        title = PreferenceHelper.readString(this, CacheRecoveryPersistentData.PREF_CACHE_FILE_NAME, "WebViewAty_title");
        siteId = PreferenceHelper.readString(this, CacheRecoveryPersistentData.PREF_CACHE_FILE_NAME, "WebViewAty_siteId");
    }


    @OnClick({R.id.imv_back, R.id.tv_back,R.id.tv_operation})
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
            case R.id.tv_back:
                if (wvWeb.canGoBack()) {
                    wvWeb.goBack();
                } else {
                    this.finish();
                }
                break;
            case R.id.tv_close:
                this.finish();
                break;
            case R.id.tv_operation:
                UiUtil.setBackgroundAlpha(WebViewActivity.this, 0.7f);
                Utils.convertActivityFromTranslucent(activity);
                if (listPopupWindow == null) {
                    listPopupWindow = new PopupWindow(UiUtil.getScreenW(context) / 3, LinearLayout.LayoutParams.WRAP_CONTENT);
                    View contentView = View.inflate(WebViewActivity.this, R.layout.popupwindow, null);
                    listPopupWindow.setContentView(contentView);
                    listPopupWindow.setFocusable(true);
                    listPopupWindow.setOutsideTouchable(true);
                    listPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    listPopupWindow.setAnimationStyle(R.style.popupWindowAnimationScale);

                    listPopupWindow.showAtLocation(tvOperation, Gravity.TOP | Gravity.END, UiUtil.dip2px(5), UiUtil.getStatusBarHeight(context) + tvOperation.getHeight());

                    ListView listView = (ListView) contentView.findViewById(R.id.pop_list);
                    listView.setDivider(ContextCompat.getDrawable(context, R.mipmap.divider));
                    MoreAdapter adapter = new MoreAdapter(context, icon, text);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new ItemClickListener());
                    listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                                UiUtil.setBackgroundAlpha(WebViewActivity.this, 1f);
                        }
                    });
                } else {
                    listPopupWindow.showAtLocation(tvOperation, Gravity.TOP | Gravity.END, UiUtil.dip2px(5), UiUtil.getStatusBarHeight(context) + tvOperation.getHeight());
                }

                break;
            default:
                break;
        }
    }

    private void haveSiteWindowClick(int position) {
        switch (position) {
            case 0:
                if (NetUtil.getNetworkState(WebViewActivity.this) == NetUtil.NETWORN_NONE) {
                    UiUtil.showToast(context, getString(R.string.no_network));
                }
                UiUtil.setBackgroundAlpha(WebViewActivity.this, 1f);
                break;
            case 1:
                // 刷新
                currentUrl = wvWeb.getUrl();
                if (wvWeb != null) {
                    startProgress90();
                    wvWeb.reload();
                }
                UiUtil.setBackgroundAlpha(WebViewActivity.this, 1f);
                break;
            case 2:

                break;
            case 4:
                Utils.copy(context, wvWeb.getUrl());
                UiUtil.showToast(context, "复制链接成功");
                break;

            default:
                break;
        }
    }

    private void haveNoSiteWindowClick(int position) {
        switch (position) {
            case 0:
                // 刷新
                currentUrl = wvWeb.getUrl();
                if (wvWeb != null) {
//                    if (currentUrl != null) {
//                        wvWeb.loadUrl(currentUrl);
//                    } else {
//                        wvWeb.loadUrl(url);
//                    }
                    startProgress90();
                    wvWeb.reload();
                }
                UiUtil.setBackgroundAlpha(WebViewActivity.this, 1f);
                break;
            case 2:
                Utils.copy(context, wvWeb.getUrl());
                UiUtil.showToast(context, "复制链接成功");
                break;
            default:
                break;
        }
    }


    private class ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            haveNoSiteWindowClick(position);
            listPopupWindow.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (mUploadMessage == null)
                    return;
                mUploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                mUploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = data == null || resultCode != MainActivity.RESULT_OK ? null : data.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        wvWeb.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wvWeb.onResume();
    }

    @Override
    public void onBackPressed() {
        goFrontLink();
    }

    private void goFrontLink() {
        if (wvWeb.canGoBack()) {
            wvWeb.goBack();
        } else {
            activity.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wvWeb != null) {
            wvWeb.clearHistory();
            ((ViewGroup) wvWeb.getParent()).removeView(wvWeb);
            wvWeb.loadUrl("about:blank");
            wvWeb.stopLoading();
            wvWeb.setWebChromeClient(null);
            wvWeb.setWebViewClient(null);
            wvWeb.destroy();
            wvWeb = null;
        }
        if (isPush) {
            if (SystemTool.hasMainActivity(context)) {

            } else {
                SystemTool.startApp(context);
            }
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event event) {
        if (event == null) {
            return;
        }
        switch (event.getEventFlag()) {
            case CommonConstants.EVENT_NET_WORK_ON:
//                netView.setVisibility(View.GONE);
                break;
            case CommonConstants.EVENT_NET_WORK_OFF:
                UiUtil.showToast(context, getString(R.string.no_network));
//                netView.setVisibility(View.VISIBLE);
                break;
            case EventConstants.LOGIN_IN_SUCCESS:
                switch (JSBridgeManager.METHOD_NAME) {
                    case "getUserInfo":
                        jsi.getUserInfo();
                        break;
                    case "login":
                        jsi.doJavaScript(jsi.codeMessageJsonStr("0", "登录成功", false));
                        break;
                }
                break;
            case EventConstants.LOGIN_IN_FAILED:
                switch (JSBridgeManager.METHOD_NAME) {
                    case "getUserInfo":
                        jsi.doJavaScript(jsi.codeMessageJsonStr("-3", "用户取消", true));
                        break;
                    case "login":
                        jsi.doJavaScript(jsi.codeMessageJsonStr("-1", "登录失败", true));
                        break;
                }
                break;
        }
    }

    private void startProgress90() {
        progressBar.setVisibility(View.VISIBLE);
        if (url.contains("jointem") || url.contains("115.29") || url.contains("172.31")) {
            tvLoading.setVisibility(View.GONE);
        } else {
            tvLoading.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < 900; i++) {
            final int progress = i + 1;
            progressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (progressBar != null) {
                        progressBar.setProgress(progress);
                    }
                }
            }, (i + 1) * 2);
        }
    }

    private class CustomWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onReceivedTitle(WebView view, String receivedTitle) {
            super.onReceivedTitle(view, receivedTitle);
            if (wvWeb != null) {
                if (TextUtils.isEmpty(title)) {
                    title = context.getString(R.string.app_name);
                }
                if (isDynamicSite) {
                    title = receivedTitle;
                    tvTitle.setText(receivedTitle);
                } else {
                    tvTitle.setText(title);
                }

            }
        }

        // The undocumented magic method override
        // Eclipse will swear at you if you try to put @Override here
        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg) {

            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            activity.startActivityForResult(
                    Intent.createChooser(i, "File Chooser"),
                    FILECHOOSER_RESULTCODE);
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg,
                                    String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            activity.startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    FILECHOOSER_RESULTCODE);
        }

        // For Android 4.1
        public void openFileChooser(ValueCallback uploadMsg,
                                    String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            activity.startActivityForResult(
                    Intent.createChooser(i, "File Chooser"),
                    FILECHOOSER_RESULTCODE);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            {
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                    mUploadMessage = null;
                }

                mUploadMessage = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, FILECHOOSER_RESULTCODE);
                } catch (ActivityNotFoundException e) {
                    mUploadMessage = null;
                    Toast.makeText(getBaseContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }


        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (wvWeb != null) {
                progressBar.setProgress(1000);
                progressBar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, 300);
                tvTitle.setText(title);
                wvWeb.setVisibility(View.VISIBLE);
            }
//            if (jsi != null) {
//                jsi.doJavaScript("javascript:Utils.hiddenTitle()");
//            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String webUrl) {
            currentUrl = webUrl;
            if (webUrl.startsWith("tel:")) {
                webUrl = webUrl.replace("tel:", "").replace(" ", "").trim();
                Utils.call(WebViewActivity.this, webUrl);
                return true;
            } else if (webUrl.startsWith("mailto:")) {
                webUrl = webUrl.replace("mailto:", "");
            }
            if (StringUtils.isEmail(webUrl)) {
                Utils.sendEmail(WebViewActivity.this, webUrl);
                return true;
            } else if (webUrl.startsWith("sms:")) {
                webUrl = webUrl.replace("sms:", "");
                Utils.sendSms(context, webUrl);
                return true;
            }
            Logger.d(webUrl);
            statusCodeThread(webUrl);
//            view.loadUrl(webUrl);
            return false;
        }
    }

    public String getWebUrl() {
        return wvWeb.getUrl();
    }

    public String getWebTitle() {
        return tvTitle.getText().toString();
    }

    public String getWebContent() {
        return currentUrl;
    }
}
