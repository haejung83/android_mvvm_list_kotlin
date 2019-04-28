package com.haejung.template

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object GlideImageBindings {

    @BindingAdapter("app:glideImageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url: String) {
        Glide.with(view).load(url).into(view)
    }

}