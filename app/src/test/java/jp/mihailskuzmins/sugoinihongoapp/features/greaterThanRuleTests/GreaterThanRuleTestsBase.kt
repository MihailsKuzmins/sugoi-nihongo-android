package jp.mihailskuzmins.sugoinihongoapp.features.greaterThanRuleTests

import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.GreaterThanRule

abstract class GreaterThanRuleTestsBase {

	protected fun <T: Comparable<T>> createClass(minValue: T) =
		GreaterThanRule(minValue)
}