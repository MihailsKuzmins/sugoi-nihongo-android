package jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.configs

import androidx.annotation.StringRes
import jp.mihailskuzmins.sugoinihongoapp.resources.Action

class AlertConfig {

	@StringRes
	var okText: Int? = null
	var title: String = ""
	var message: String = ""
	var okAction: Action? = null
	var isCancellable: Boolean = true
}
