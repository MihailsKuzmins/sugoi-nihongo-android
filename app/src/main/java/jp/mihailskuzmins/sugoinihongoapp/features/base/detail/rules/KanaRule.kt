package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules

import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.helpers.japanese.KanaKanjiDetector

class KanaRule(withPunctuation: Boolean = false, isVisible: Boolean = true) : RuleBase<String>(R.string.validation_kana, isVisible) {

	private val mValidateFunc: (Char) -> Boolean

	init {
		mValidateFunc = if (withPunctuation) { it: Char -> KanaKanjiDetector.isKana(it) || KanaKanjiDetector.isPunctuation(it) }
		else KanaKanjiDetector::isKana
	}

	override fun validate(value: String) =
		value.all(mValidateFunc::invoke)
}