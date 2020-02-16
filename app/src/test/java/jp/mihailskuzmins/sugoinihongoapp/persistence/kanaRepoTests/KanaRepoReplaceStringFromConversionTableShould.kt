package jp.mihailskuzmins.sugoinihongoapp.persistence.kanaRepoTests

import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.asset.ConversionType
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.asset.Kana
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class KanaRepoReplaceStringFromConversionTableShould : KanaRepoTestsBase() {

	@Test
	fun `replace characters from conversion table for hiragana`() {
		val input = StringBuilder("hiragana")

		createClass()
			.replaceStringFromConversionTable(input, ConversionType.RomajiToHiragana)
			.toString()
			.shouldBeEqualTo("ひらがな")
	}

	@Test
	fun `replace characters from the conversion table for katakana`() {
		val input = StringBuilder("katakana")

		createClass()
			.replaceStringFromConversionTable(input, ConversionType.RomajiToKatakana)
			.toString()
			.shouldBeEqualTo("カタカナ")
	}

	@Test
	fun `replace characters from conversion table for romaji from hiragana`() {
		val input = StringBuilder("ひらがな")

		createClass()
			.replaceStringFromConversionTable(input, ConversionType.KanaToRomaji)
			.toString()
			.shouldBeEqualTo("hiragana")
	}

	@Test
	fun `replace characters from conversion table for romaji from katakana`() {
		val input = StringBuilder("カタカナ")

		createClass()
			.replaceStringFromConversionTable(input, ConversionType.KanaToRomaji)
			.toString()
			.shouldBeEqualTo("katakana")
	}

	@Test
	fun `not replace sokuon character for hiragana`() {
		val input = StringBuilder("ki${Kana.Hiragana.sokuon}saten")

		createClass()
			.replaceStringFromConversionTable(input, ConversionType.RomajiToHiragana)
			.toString()
			.shouldBeEqualTo("きっさてん")
	}

	@Test
	fun `not replace sokuon character for katakana`() {
		val input = StringBuilder("che${Kana.Katakana.sokuon}ku")

		createClass()
			.replaceStringFromConversionTable(input, ConversionType.RomajiToKatakana)
			.toString()
			.shouldBeEqualTo("チェック")
	}

	@Test
	fun `not replace chouonpu for katakana`() {
		val input = StringBuilder("koーhiー")

		createClass()
			.replaceStringFromConversionTable(input, ConversionType.RomajiToKatakana)
			.toString()
			.shouldBeEqualTo("コーヒー")
	}
}