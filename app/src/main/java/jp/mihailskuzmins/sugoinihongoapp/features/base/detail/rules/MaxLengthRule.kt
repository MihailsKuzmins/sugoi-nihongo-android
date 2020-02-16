package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules

import jp.mihailskuzmins.sugoinihongoapp.R

class MaxLengthRule(
	private val mMaxLength: Int,
	isVisible: Boolean = true) : RuleBase<String>(R.string.validation_max_length, isVisible) {

	init {
		check(mMaxLength > 0) { "Maximum length must be greater than zero!" }
	}

	override fun validate(value: String) =
		value.length <= mMaxLength
}