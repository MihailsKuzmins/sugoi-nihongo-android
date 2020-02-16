package jp.mihailskuzmins.sugoinihongoapp.helpers.japanese

object KanaKanjiDetector {

	fun hasKanaOrKanji(string: String) = string.any(::isKanaOrKanji)

	fun hasKana(string: String) = string.any(::isKana)

	fun hasKanji(string: String) = string.any(::isKanji)

	fun hasRomaji(string: String) = string.any(::isRomaji)

	fun isKanaOrKanji(char: Char) =
		isKana(char) || isKanji(char)

	fun isKana(char: Char): Boolean {
		val unicodeBlock = Character.UnicodeBlock.of(char)

		return unicodeBlock === Character.UnicodeBlock.HIRAGANA ||
				unicodeBlock === Character.UnicodeBlock.KATAKANA ||
				unicodeBlock === Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS
	}

	fun isKanji(char: Char) =
		Character.UnicodeBlock.of(char) === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
				char == '々'

	fun isRomaji(char: Char) =
		char in 'A'..'Z' || char in 'a'..'z'

	fun isPunctuation(char: Char) =
		when (char) {
			'、' -> true
			else -> false
		}
}