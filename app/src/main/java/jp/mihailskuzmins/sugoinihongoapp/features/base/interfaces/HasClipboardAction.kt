package jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces

import io.reactivex.Observable
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.ClipboardUtil

interface HasClipboardAction {

	val clipboardUtil: ClipboardUtil
	val clipboardValue: Observable<String>

	fun setClipboard(value: String) =
		clipboardUtil.setClipboard(value)
}