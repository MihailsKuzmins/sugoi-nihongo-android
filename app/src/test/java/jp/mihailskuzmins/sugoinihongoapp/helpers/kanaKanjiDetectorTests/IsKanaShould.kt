package jp.mihailskuzmins.sugoinihongoapp.helpers.kanaKanjiDetectorTests

import jp.mihailskuzmins.sugoinihongoapp.helpers.japanese.KanaKanjiDetector
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test

class IsKanaShould : KanaKanjiDetectorTestsBase() {

	@Test
	fun `return true for katakana and hiragana`() {
		hiragana
			.union(katakana)
			.forEach {
				KanaKanjiDetector
					.isKana(it)
					.shouldBeTrue()
			}
	}

	@Test
	fun `return false for kanji`() {
		KanaKanjiDetector
			.isKana('日')
			.shouldBeFalse()
	}

	@Test
	fun `return false for romaji`() {
		KanaKanjiDetector
			.isKana('a')
			.shouldBeFalse()
	}
}