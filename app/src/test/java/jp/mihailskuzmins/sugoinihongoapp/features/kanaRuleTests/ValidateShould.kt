package jp.mihailskuzmins.sugoinihongoapp.features.kanaRuleTests

import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test

class ValidateShould : KanaRuleTestsBase() {

	@Test
	fun `return true if word has only hiragana`() {
		val result = createClass(false)
			.validate("ひらがな")

		result.shouldBeTrue()
	}

	@Test
	fun `return true if word has only katakana`() {
		val result = createClass(false)
			.validate("カタカナ")

		result.shouldBeTrue()
	}

	@Test
	fun `return false if word has only kanji`() {
		val result = createClass(false)
			.validate("漢字")

		result.shouldBeFalse()
	}

	@Test
	fun `return false if word has kana and kanji`() {
		val result = createClass(false)
			.validate("ひらがなカタカナ漢字")

		result.shouldBeFalse()
	}

	@Test
	fun `return false if word has spaces`() {
		val result = createClass(false)
			.validate("ひらがな 漢字")

		result.shouldBeFalse()
	}

	@Test
	fun `return false if word has romaji`() {
		val result = createClass(false)
			.validate("ひらがな漢字romaji")

		result.shouldBeFalse()
	}

	@Test
	fun `return true if word has punctuation and it's allowed`() {
		val result = createClass(true)
			.validate("ひらがな、カタカナ")

		result.shouldBeTrue()
	}

	@Test
	fun `return false if word has punctuation and it's not allowed`() {
		val result = createClass(false)
			.validate("ひらがな、カタカナ")

		result.shouldBeFalse()
	}
}