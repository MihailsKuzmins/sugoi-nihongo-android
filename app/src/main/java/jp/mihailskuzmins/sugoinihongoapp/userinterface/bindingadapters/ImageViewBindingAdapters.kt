package jp.mihailskuzmins.sugoinihongoapp.userinterface.bindingadapters

import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("imageResource")
fun setImageResource(imageView: ImageView, imageRes: Int?) {
	imageView.isVisible = imageRes != null
	imageRes?.let { imageView.setImageResource(it) }
}
