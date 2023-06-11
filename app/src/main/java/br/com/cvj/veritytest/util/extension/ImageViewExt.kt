package br.com.cvj.veritytest.util.extension


import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import br.com.cvj.veritytest.R
import com.bumptech.glide.Glide

fun ImageView.setImageUrl(
    url: String,
    @DrawableRes placeholderLoading: Int = R.drawable.ic_loading,
    @DrawableRes error: Int = R.drawable.img_avatar_default,
) {

    Glide
        .with(this)
        .load(url)
        .placeholder(placeholderLoading)
        .into(this)
        .onLoadFailed(ContextCompat.getDrawable(this.context, error))
}

fun AppCompatImageView.setImageUrl(
    url: String, @DrawableRes placeholderLoading: Int = R.drawable.ic_loading,
    @DrawableRes error: Int = R.drawable.img_avatar_default,
) {

    Glide
        .with(this)
        .load(url)
        .placeholder(placeholderLoading)
        .into(this)
        .onLoadFailed(ContextCompat.getDrawable(this.context, error))
}

fun ImageView.setGif(@DrawableRes gifId: Int) {
    Glide
        .with(this)
        .asGif()
        .load(gifId)
        .into(this)
}

fun AppCompatImageView.setGif(@DrawableRes gifId: Int) {
    Glide
        .with(this)
        .asGif()
        .load(gifId)
        .into(this)
}

fun ImageView.unsetGif() {
    Glide
        .with(this)
        .clear(this)
}

fun AppCompatImageView.unsetGif() {
    Glide
        .with(this)
        .clear(this)
}