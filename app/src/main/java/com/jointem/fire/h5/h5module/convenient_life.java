package com.jointem.fire.h5.h5module;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jointem.base.util.IntentUtil;
import com.jointem.fire.R;
import com.jointem.fire.activity.WebViewActivity;
import com.jointem.fire.h5.ant.JContext;
import com.jointem.fire.h5.ant.JView;
import com.jointem.fire.h5.ant.Param;


/**
 * @author THC
 * @Title: convenient_life
 * @Package com.jointem.hgp.h5.h5module
 * @Description:
 * @date 2017/4/18 14:05
 */
public class convenient_life {
    public static void hideTitleRightView(@JView View contentView){
        View view = contentView.findViewById(R.id.rl_operation);
        if (view != null) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void showTitleRightView(@JView View contentView){
        View view = contentView.findViewById(R.id.rl_operation);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void setTitleText(@JView View contentView, @Param("subTitle") String subTitle){
        TextView view = (TextView) contentView.findViewById(R.id.tv_sub_title);
        if (view != null) {
            view.setText(subTitle);
        }
    }

    public static void setTitleRightText(@JView View contentView, @Param("subTitle") String subTitle){
        TextView view = (TextView) contentView.findViewById(R.id.tv_operation);
        if (view != null) {
            view.setText(subTitle);
        }
    }

    public static void setTitleTextColor(@JView View contentView, @Param("titleColor") int titleColor){
        TextView view = (TextView) contentView.findViewById(R.id.tv_sub_title);
        if (view != null) {
            view.setTextColor(titleColor);
        }
    }

    public static void hideBackView(@JView View contentView){
        View view = contentView.findViewById(R.id.rl_from_other);
        if (view != null) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void showTitleView(@JView View contentView){
        View view = contentView.findViewById(R.id.rl_from_other);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void gotoSecondWebView(@JContext Context context, @Param("url") String url){
        Intent i = new Intent(context, WebViewActivity.class);
        if (url.startsWith("http")) {

        } else {

//            url = HgpApplication.bmurl2 + url;
//            url = "http://172.31.61.10:8081/www/index.html" + url;
//            url = "http://172.31.60.66:8081/servant/www/index.html" + url;
        }

        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        i.putExtras(bundle);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(i);
    }

    public static void callNum(@JContext Context context, @Param("num") String number){
        IntentUtil.call(context, number);
    }
}
