package jp.mihailskuzmins.sugoinihongoapp.extensions.android

import android.app.Activity
import android.content.Intent
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke

fun Activity.hideKeyboard() =
	window.decorView.rootView
		.hideKeyboard()

fun Activity.startActivityAndFinish(clazz: Class<*>) {
	Intent(this, clazz)
		.invoke(::startActivity)

	finish()
}