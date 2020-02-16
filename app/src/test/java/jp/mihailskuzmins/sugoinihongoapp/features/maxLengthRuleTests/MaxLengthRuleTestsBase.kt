package jp.mihailskuzmins.sugoinihongoapp.features.maxLengthRuleTests

import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.MaxLengthRule

abstract class MaxLengthRuleTestsBase {

	protected fun createClass(maxLength: Int) =
		MaxLengthRule(maxLength)
}