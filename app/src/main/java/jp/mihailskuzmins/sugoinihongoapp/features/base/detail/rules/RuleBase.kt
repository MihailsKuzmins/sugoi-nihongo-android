package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules

import android.content.Context
import androidx.annotation.StringRes

abstract class RuleBase<T>(
	@StringRes protected val errorMessage: Int,
	val isVisible: Boolean) {

	abstract fun validate(value: T): Boolean

	open fun getErrorMessage(context: Context): String =
		context.getString(errorMessage)
}