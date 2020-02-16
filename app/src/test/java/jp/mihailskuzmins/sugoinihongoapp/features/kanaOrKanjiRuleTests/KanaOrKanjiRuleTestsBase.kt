package jp.mihailskuzmins.sugoinihongoapp.features.kanaOrKanjiRuleTests

import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.KanaOrKanjiRule

abstract class KanaOrKanjiRuleTestsBase {

	protected fun createClass(withPunctuation: Boolean) =
		KanaOrKanjiRule(withPunctuation)
}