package jp.mihailskuzmins.sugoinihongoapp.helpers.kanaKanjiDetectorTests

import jp.mihailskuzmins.sugoinihongoapp.helpers.japanese.KanaKanjiDetector
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test

class HasKanaOrKanjiShould {

	@Test
	fun `return true for words with hiragana and katakana`() {
		listOf("よっきゅう", "スーパーマーケット")
			.forEach {
				KanaKanjiDetector
					.hasKanaOrKanji(it)
					.shouldBeTrue()
			}
	}

	@Test
	fun `return true for kanji`() {
		KanaKanjiDetector
			.hasKanaOrKanji("格闘")
			.shouldBeTrue()
	}

	@Test
	fun `return false for romaji`() {
		KanaKanjiDetector
			.hasKanaOrKanji("modoru")
			.shouldBeFalse()
	}

	@Test
	fun `return false for blank strings`() {
		listOf("", " ")
			.forEach {
				KanaKanjiDetector
					.hasKanaOrKanji(it)
					.shouldBeFalse()
			}
	}
}