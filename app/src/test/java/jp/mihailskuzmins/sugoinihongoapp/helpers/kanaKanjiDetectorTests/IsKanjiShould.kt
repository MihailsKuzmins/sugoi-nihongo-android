package jp.mihailskuzmins.sugoinihongoapp.helpers.kanaKanjiDetectorTests

import jp.mihailskuzmins.sugoinihongoapp.helpers.japanese.KanaKanjiDetector
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test

class IsKanjiShould {

	@Test
	fun `return true for kanji`() {
		KanaKanjiDetector
			.isKanji('日')
			.shouldBeTrue()
	}

	@Test
	fun `return true for iteration mark`() {
		KanaKanjiDetector
			.isKanji('々')
			.shouldBeTrue()
	}

	@Test
	fun `return false for kana`() {
		listOf('か', 'カ')
			.forEach {
				KanaKanjiDetector
					.isKanji(it)
					.shouldBeFalse()
			}
	}

	@Test
	fun `return false for romaji`() {
		KanaKanjiDetector
			.isKanji('a')
			.shouldBeFalse()
	}
}