package com.erayerarslan.t_vac_kotlin.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.erayerarslan.t_vac_kotlin.R

fun ImageView.loadImage(url: String?, placeholder: Int = R.drawable.ic_acacia, error: Int = R.drawable.ic_add) {
    Glide.with(this.context)
        .load(url)
        .apply(
            RequestOptions()
                .placeholder(placeholder)
                .error(error)
                .centerCrop()
        )
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun ImageView.loadImage(resourceId: Int, placeholder: Int = R.drawable.ic_willow) {
    Glide.with(this.context)
        .load(resourceId)
        .apply(
            RequestOptions()
                .placeholder(placeholder)
                .centerCrop()
        )
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}