package jp.mihailskuzmins.sugoinihongoapp.userinterface.bindingadapters

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.getResource
import jp.mihailskuzmins.sugoinihongoapp.resources.Action

@BindingAdapter("android:visibility")
fun setVisibility(view: View, isVisible: Boolean?) {
	view.isVisible = isVisible ?: true
}

@BindingAdapter("android:visibility")
fun setVisibility(view: View, text: String?) {
	view.isVisible = !text.isNullOrBlank()
}

@BindingAdapter("android:background")
fun setBackground(view: View, backgroundRes: Int?) {
	backgroundRes?.let {
		view.setBackgroundResource(it)
	}
}

@BindingAdapter("isClickable")
fun setIsClickable(view: View, isClickable: Boolean?) {
	(isClickable ?: false)
		.let {
			view.apply {
				val backgroundResource = if (it) view.context.getResource(android.R.attr.selectableItemBackground)
					else 0

				this.isClickable = it
				isFocusable = it
				setBackgroundResource(backgroundResource)
			}
		}
}

@BindingAdapter("onLongClick")
fun setOnLongClickListener(view: View, onLongClickListener: Action?) {
	view.isLongClickable = onLongClickListener != null

	onLongClickListener?.let {
		view.setOnLongClickListener { _ -> it().run { true } }
	}
}