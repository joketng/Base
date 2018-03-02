package com.jointem.fire.view

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bigkoo.convenientbanner.holder.Holder
import com.jointem.fire.utils.loadImage

/**
 * Created by wuht on 2017/10/12.
 */
class ImageHolderView : Holder<String> {
    lateinit var imageView: ImageView
    override fun createView(context: Context?): View {
        imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return imageView
    }

    override fun UpdateUI(context: Context?, position: Int, data: String) {
        imageView.loadImage(data)
    }
}