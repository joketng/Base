package com.jointem.fire.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.jointem.fire.R
import com.jointem.fire.view.CustomToast
import kotlinx.android.synthetic.main.v_dialog.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.layoutInflater

/**
 * Created by thc on 2017/9/26.
 */

fun Context.showToast(resId: Int) {
    val toast = CustomToast(this, resId)
    toast.show()
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    val toast = CustomToast(this, message)
    toast.show(duration)
}

fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
            .load(url)
            .placeholder(R.mipmap.ic_hgp_loading_rectangle)
            .error(R.mipmap.ic_hgp_loading_rectangle)
            .centerCrop()
            .crossFade()
            .into(this)
}

fun Context.getScreenH(): Int {
    val dm = resources.displayMetrics
    return dm.heightPixels
}

fun Context.getScreenW(): Int {
    val dm = resources.displayMetrics
    return dm.widthPixels
}

fun Context.showDialog(msg: String, onSure: View.OnClickListener? = null, onCancel: View.OnClickListener? = null) {
    var view = layoutInflater.inflate(R.layout.v_dialog, null, false)
    var d = alert {
        customView = view
    }.show()
    view.tv_dialog_content.text = msg
    view.tv_dialog_cancel.singleClick { d.dismiss() }
    if (onSure != null) {
        view.tv_dialog_sure.setOnClickListener(onSure)
    }
    if (onCancel != null) {
        view.tv_dialog_cancel.setOnClickListener(onCancel)
    }
}







