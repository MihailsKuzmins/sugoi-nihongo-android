package jp.mihailskuzmins.sugoinihongoapp.helpers.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import jp.mihailskuzmins.sugoinihongoapp.BuildConfig
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke

class ClipboardUtil(context: Context) {

	private val mClipboardManager: ClipboardManager = context
		.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

	fun setClipboard(value: String) = ClipData
		.newPlainText(BuildConfig.APPLICATION_ID, value)
		.invoke { mClipboardManager.primaryClip = it }
}