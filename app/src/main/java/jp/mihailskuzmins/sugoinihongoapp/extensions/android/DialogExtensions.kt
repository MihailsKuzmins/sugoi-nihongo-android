package jp.mihailskuzmins.sugoinihongoapp.extensions.android

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.makeToast(@StringRes textRes: Int, length: Int = Toast.LENGTH_SHORT): Toast =
	Toast.makeText(this, textRes, length)

fun Context.toast(text: String, length: Int = Toast.LENGTH_SHORT) =
	Toast.makeText(this, text, length)
		.show()