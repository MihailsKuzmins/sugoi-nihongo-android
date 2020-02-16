package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules

import jp.mihailskuzmins.sugoinihongoapp.R

class NotBlankRule(isVisible: Boolean = true) : RuleBase<String>(R.string.validation_not_blank, isVisible) {

	override fun validate(value: String) =
		value.isNotBlank()
}