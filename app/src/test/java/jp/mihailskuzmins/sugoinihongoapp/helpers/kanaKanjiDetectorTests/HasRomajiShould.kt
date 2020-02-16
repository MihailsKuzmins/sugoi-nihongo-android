package jp.mihailskuzmins.sugoinihongoapp.helpers.kanaKanjiDetectorTests

import jp.mihailskuzmins.sugoinihongoapp.helpers.japanese.KanaKanjiDetector
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test

class HasRomajiShould {

	@Test
	fun `return true for romaji word`() {
		KanaKanjiDetector
			.hasRomaji("nihongo")
			.shouldBeTrue()
	}

	@Test
	fun `return false for kana words`() {
		listOf("コレクション", "きゅうりょう")
			.forEach {
				KanaKanjiDetector
					.hasRomaji(it)
					.shouldBeFalse()
			}
	}

	@Test
	fun `return false for kanji word`() {
		KanaKanjiDetector
			.hasRomaji("日本語")
			.shouldBeFalse()
	}

	@Test
	fun `return false for blank strings`() {
		listOf("", " ")
			.forEach {
				KanaKanjiDetector
					.hasRomaji(it)
					.shouldBeFalse()
			}
	}
}