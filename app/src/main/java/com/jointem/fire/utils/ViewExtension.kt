package com.jointem.fire.utils

import android.support.annotation.LayoutRes
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.layoutInflater

/**
 * Created by thc on 2017/10/12.
 */

fun View.singleClick(l: (android.view.View?) -> Unit) {
    setOnClickListener(l)
}

fun View.visible(show: Boolean = true) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return context.layoutInflater.inflate(layoutRes, this, attachToRoot)
}