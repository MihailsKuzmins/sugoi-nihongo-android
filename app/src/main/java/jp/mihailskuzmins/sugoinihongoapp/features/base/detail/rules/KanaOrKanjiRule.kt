package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules

import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.helpers.japanese.KanaKanjiDetector

class KanaOrKanjiRule(withPunctuation: Boolean = false, isVisible: Boolean = true) : RuleBase<String>(R.string.validation_kana_or_kanji, isVisible) {

	private val mValidateFunc: (Char) -> Boolean

	init {
		mValidateFunc = if (withPunctuation) { it: Char -> KanaKanjiDetector.isKanaOrKanji(it) || KanaKanjiDetector.isPunctuation(it) }
		else KanaKanjiDetector::isKanaOrKanji
	}

	override fun validate(value: String) =
		value.all(mValidateFunc::invoke)
}