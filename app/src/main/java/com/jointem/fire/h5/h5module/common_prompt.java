package com.jointem.fire.h5.h5module;


import android.content.Context;

import com.jointem.base.util.AlertDialogHelper;
import com.jointem.base.util.UiUtil;
import com.jointem.fire.h5.ant.JContext;
import com.jointem.fire.h5.ant.Param;

/**
 * @author THC
 * @Title: common_prompt
 * @Package com.jointem.hgp.h5.h5module
 * @Description:
 * @date 2017/5/8 16:32
 */
public class common_prompt {
    public static void toast(@JContext Context context, @Param("content") String content){
        UiUtil.showToast(context, content);
    }

    public static void dialog(@JContext Context context, @Param("content") String content){
        AlertDialogHelper.getInstance().buildConfirmDialog(context, content);
    }

    public static void showLoading(@JContext Context context){
        AlertDialogHelper.getInstance().buildRoundProcessDialog(context, true);
    }

    public static void hideLoading(){
        AlertDialogHelper.getInstance().dismissRoundProcessDialog();
    }
}
