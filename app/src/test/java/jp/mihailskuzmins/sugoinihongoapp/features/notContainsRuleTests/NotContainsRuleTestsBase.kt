package jp.mihailskuzmins.sugoinihongoapp.features.notContainsRuleTests

import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.NotContainsRule

abstract class NotContainsRuleTestsBase {

	protected fun createClass(char: Char) =
		NotContainsRule(char)
}