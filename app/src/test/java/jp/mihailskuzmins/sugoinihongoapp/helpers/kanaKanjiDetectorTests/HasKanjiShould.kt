package jp.mihailskuzmins.sugoinihongoapp.helpers.kanaKanjiDetectorTests

import jp.mihailskuzmins.sugoinihongoapp.helpers.japanese.KanaKanjiDetector
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test

class HasKanjiShould {

	@Test
	fun `return true for kanji word`() {
		KanaKanjiDetector
			.hasKanji("日本語")
			.shouldBeTrue()
	}

	@Test
	fun `return true for kanji word with kana`() {
		KanaKanjiDetector
			.hasKanji("案内する")
			.shouldBeTrue()
	}

	@Test
	fun `return false for kana words`() {
		listOf("スーパーマーケット", "きゅうりょう")
			.forEach {
				KanaKanjiDetector
					.hasKanji(it)
					.shouldBeFalse()
			}
	}

	@Test
	fun `return false for romaji word`() {
		KanaKanjiDetector
			.hasKanji("annaisuru")
			.shouldBeFalse()
	}

	@Test
	fun `return false for blank strings`() {
		listOf("", " ")
			.forEach {
				KanaKanjiDetector
					.hasKanji(it)
					.shouldBeFalse()
			}
	}
}