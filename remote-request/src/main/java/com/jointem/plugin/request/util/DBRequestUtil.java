package com.jointem.plugin.request.util;


import android.content.Context;

import com.jointem.dbhelper.DownInfo;
import com.jointem.dbhelper.GreenDaoManager;

/**
 * @author THC
 * @Title: DBRequestUtil
 * @Package com.jointem.plugin.util
 * @Description:
 * @date 2017/4/14 14:46
 */
public class DBRequestUtil {
    public static void updateDownInfo(Context context, DownInfo downInfo){
        GreenDaoManager.getInstance(context).getNewSession().getDownInfoDao().update(downInfo);
    }

    public static void insertOrReplaceDownInfo(Context context, DownInfo downInfo){
        GreenDaoManager.getInstance(context).getNewSession().getDownInfoDao().insertOrReplace(downInfo);
    }

    public static void insertDownInfo(Context context, DownInfo downInfo){
        GreenDaoManager.getInstance(context).getNewSession().getDownInfoDao().insert(downInfo);
    }

    public static void saveDownInfo(Context context, DownInfo downInfo){
        GreenDaoManager.getInstance(context).getNewSession().getDownInfoDao().save(downInfo);
    }

    public static void deleteDownInfo(Context context, DownInfo downInfo){
        GreenDaoManager.getInstance(context).getNewSession().getDownInfoDao().delete(downInfo);
    }
}
