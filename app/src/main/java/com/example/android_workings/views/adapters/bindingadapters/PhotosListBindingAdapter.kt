package com.example.android_workings.views.adapters.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.LazyHeaders

import com.bumptech.glide.load.model.GlideUrl

object PhotosListBindingAdapter {
    @BindingAdapter("app:thumbnail")
    @JvmStatic
    fun setThumbnail(imageView: ImageView, thumbUri: String) {
        val url = GlideUrl(
            thumbUri, LazyHeaders.Builder()
                .addHeader("User-Agent", "your-user-agent")
                .build()
        )
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
    }
}