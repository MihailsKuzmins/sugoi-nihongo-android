package jp.mihailskuzmins.sugoinihongoapp.helpers.kanaKanjiDetectorTests

import jp.mihailskuzmins.sugoinihongoapp.helpers.japanese.KanaKanjiDetector
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test

class IsKanaOrKanjiShould : KanaKanjiDetectorTestsBase() {

	@Test
	fun `return true for hiragana and katakana`() {
		hiragana
			.union(katakana)
			.forEach {
				KanaKanjiDetector
					.isKanaOrKanji(it)
					.shouldBeTrue()
			}
	}

	@Test
	fun `return true for kanji`() {
		KanaKanjiDetector
			.isKanaOrKanji('日')
			.shouldBeTrue()
	}

	@Test
	fun `return false for romaji`() {
		KanaKanjiDetector
			.isKanaOrKanji('a')
			.shouldBeFalse()
	}
}