package jp.mihailskuzmins.sugoinihongoapp.extensions.android

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import jp.mihailskuzmins.sugoinihongoapp.extensions.cast

fun LinearLayout.removeViewsFrom(skip: Int) =
	removeViews(skip, this.childCount - skip)

fun View.showKeyboard() {
	try {
		context
			.getSystemService(Context.INPUT_METHOD_SERVICE)
			.cast<InputMethodManager>()
			.showSoftInput(this, InputMethodManager.SHOW_FORCED) // Keyboard is not shown with SHOW_IMPLICIT if it takes the full screen
	} catch (_: Error) { }
}

fun View.hideKeyboard() {
	try {
		val imm = context.
			getSystemService(Context.INPUT_METHOD_SERVICE)
			.cast<InputMethodManager>()

		if (!imm.isActive)
			return

		imm.hideSoftInputFromWindow(this.windowToken, 0)
	} catch (_: Error) { }
}