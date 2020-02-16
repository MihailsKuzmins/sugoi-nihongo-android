package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules

import android.content.Context
import jp.mihailskuzmins.sugoinihongoapp.R

class GreaterThanRule<T: Comparable<T>>(
	private val mMinValue: T,
	isVisible: Boolean = true) : RuleBase<T>(R.string.validation_greater_than, isVisible) {

	override fun validate(value: T)
			= value > mMinValue

	override fun getErrorMessage(context: Context): String =
		context.getString(errorMessage, mMinValue)
}