package jp.mihailskuzmins.sugoinihongoapp.persistence.kanaRepoTests

import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.asset.Kana
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class KanaRepoReplaceSokuonWithDoubleConsonants : KanaRepoTestsBase() {

	@Test
	fun `replace sokuon character with the next one for hiragana`() {
		val input = StringBuilder("ki${Kana.Hiragana.sokuon}saten")

		createClass()
			.replaceSokuonWithDoubleConsonants(input)
			.toString()
			.shouldBeEqualTo("kissaten")
	}

	@Test
	fun `replace sokuon character with the next one for katakana`() {
		val input = StringBuilder("po${Kana.Katakana.sokuon}kii")

		createClass()
			.replaceSokuonWithDoubleConsonants(input)
			.toString()
			.shouldBeEqualTo("pokkii")
	}

	@Test
	fun `remove sokuon if it is in the first position for hiragana`() {
		val sokuon = Kana.Hiragana.sokuon
		val input = StringBuilder("${sokuon}ki${sokuon}saten")

		createClass()
			.replaceSokuonWithDoubleConsonants(input)
			.toString()
			.shouldBeEqualTo("kissaten")
	}

	@Test
	fun `remove sokuon if it is in the first position for katakana`() {
		val sokuon = Kana.Katakana.sokuon
		val input = StringBuilder("${sokuon}po${sokuon}kii")

		createClass()
			.replaceSokuonWithDoubleConsonants(input)
			.toString()
			.shouldBeEqualTo("pokkii")
	}

	@Test
	fun `remove sokuon if it is in the last position for hiragana`() {
		val sokuon = Kana.Hiragana.sokuon
		val input = StringBuilder("ki${sokuon}saten${sokuon}")

		createClass()
			.replaceSokuonWithDoubleConsonants(input)
			.toString()
			.shouldBeEqualTo("kissaten")
	}

	@Test
	fun `remove sokuon if it is in the last position for katakana`() {
		val sokuon = Kana.Katakana.sokuon
		val input = StringBuilder("po${sokuon}kii${sokuon}")

		createClass()
			.replaceSokuonWithDoubleConsonants(input)
			.toString()
			.shouldBeEqualTo("pokkii")
	}

	@Test
	fun `remove sokuon if it is not before consonants for hiragana`() {
		val sokuon = Kana.Hiragana.sokuon
		val input = StringBuilder("kis${sokuon}aten")

		createClass()
			.replaceSokuonWithDoubleConsonants(input)
			.toString()
			.shouldBeEqualTo("kisaten")
	}

	@Test
	fun `remove sokuon if it is not before consonants for katakana`() {
		val sokuon = Kana.Katakana.sokuon
		val input = StringBuilder("p${sokuon}okii")

		createClass()
			.replaceSokuonWithDoubleConsonants(input)
			.toString()
			.shouldBeEqualTo("pokii")
	}

	@Test
	fun `handle multiple errors`() {
		val sokuon = Kana.Hiragana.sokuon
		val input = StringBuilder("${sokuon}kis${sokuon}aten${sokuon}")

		createClass()
			.replaceSokuonWithDoubleConsonants(input)
			.toString()
			.shouldBeEqualTo("kisaten")
	}
}