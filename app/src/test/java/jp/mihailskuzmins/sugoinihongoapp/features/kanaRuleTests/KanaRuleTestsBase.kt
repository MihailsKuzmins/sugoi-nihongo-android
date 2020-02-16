package jp.mihailskuzmins.sugoinihongoapp.features.kanaRuleTests

import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.KanaRule

abstract class KanaRuleTestsBase {

	protected fun createClass(withPunctuation: Boolean) =
		KanaRule(withPunctuation)
}