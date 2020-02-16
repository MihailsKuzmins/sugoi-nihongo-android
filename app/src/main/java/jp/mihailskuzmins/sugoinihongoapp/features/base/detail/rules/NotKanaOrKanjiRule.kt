package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules

import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.helpers.japanese.KanaKanjiDetector

class NotKanaOrKanjiRule(isVisible: Boolean = true) : RuleBase<String>(R.string.validation_kana_and_kanji_not_allowed, isVisible) {

	override fun validate(value: String) =
		!KanaKanjiDetector.hasKanaOrKanji(value)
}