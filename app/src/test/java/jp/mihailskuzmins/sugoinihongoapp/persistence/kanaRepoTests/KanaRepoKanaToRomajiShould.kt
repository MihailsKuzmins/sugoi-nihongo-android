package jp.mihailskuzmins.sugoinihongoapp.persistence.kanaRepoTests

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class KanaRepoKanaToRomajiShould : KanaRepoTestsBase() {

	@Test
	fun `correctly convert from katakana to romaji`() {
		val repo = createClass()

		katakanaMap.forEach {
			repo
				.kanaToRomaji(it.value)
				.shouldBeEqualTo(it.key)
		}
	}

	@Test
	fun `correctly convert from hiragana to romaji`() {
		val repo = createClass()

		hiraganaMap.forEach {
			repo
				.kanaToRomaji(it.value)
				.shouldBeEqualTo(it.key)
		}
	}

	@Test
	fun `correctly convert combined kana to romaji`() {
		val repo = createClass()

		combinedMap.forEach {
			repo
				.kanaToRomaji(it.value)
				.shouldBeEqualTo(it.key)
		}
	}

	@Test
	fun `return empty string for empty string`() {
		createClass()
			.kanaToRomaji("")
			.shouldBeEmpty()
	}
}