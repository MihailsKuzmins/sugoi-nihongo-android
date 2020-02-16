package jp.mihailskuzmins.sugoinihongoapp.userinterface.bindingadapters

import android.graphics.drawable.Drawable
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import jp.mihailskuzmins.sugoinihongoapp.R

@BindingAdapter("srcVector")
fun setSrcVector(fab: FloatingActionButton, drawable: Drawable?) {
	fab.setImageDrawable(drawable ?: fab.context.getDrawable(R.drawable.ic_add_white_24dp))
}

@BindingAdapter("isEnabled")
fun setIsEnabled(fab: FloatingActionButton, isEnabled: Boolean?) {
	val isFabEnabled = isEnabled ?: true

	with(fab) {
		this.isEnabled = isFabEnabled
		this.alpha = if (isFabEnabled) 1F else 0.5F
	}
}