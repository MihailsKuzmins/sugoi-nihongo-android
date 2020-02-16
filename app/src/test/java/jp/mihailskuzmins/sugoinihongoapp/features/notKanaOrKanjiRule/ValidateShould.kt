package jp.mihailskuzmins.sugoinihongoapp.features.notKanaOrKanjiRule

import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test

class ValidateShould : NotKanaOrKanjiRuleTestsBase() {

	@Test
	fun `return true if string has no kana nor kanji`() {
		val result = createClass()
			.validate("no kana nor kanji")

		result.shouldBeTrue()
	}

	@Test
	fun `return false if string has hiragana`() {
		val result = createClass()
			.validate("no kanji, but ひらがな")

		result.shouldBeFalse()
	}

	@Test
	fun `return false if string has katakana`() {
		val result = createClass()
			.validate("no kanji, but カタカナ")

		result.shouldBeFalse()
	}

	@Test
	fun `return false if string has kanji`() {
		val result = createClass()
			.validate("no kana, but 漢字")

		result.shouldBeFalse()
	}
}