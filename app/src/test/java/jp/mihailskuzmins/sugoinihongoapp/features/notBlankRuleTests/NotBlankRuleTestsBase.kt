package jp.mihailskuzmins.sugoinihongoapp.features.notBlankRuleTests

import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.NotBlankRule

abstract class NotBlankRuleTestsBase {

	protected fun createClass() =
		NotBlankRule()
}