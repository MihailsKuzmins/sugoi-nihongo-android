package jp.mihailskuzmins.sugoinihongoapp.helpers.kanaKanjiDetectorTests

import jp.mihailskuzmins.sugoinihongoapp.helpers.japanese.KanaKanjiDetector
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test

class IsRomajiShould {

	@Test
	fun `return true for english lowercase letters`() {
		('a'..'z')
			.forEach {
				KanaKanjiDetector
					.isRomaji(it)
					.shouldBeTrue()
			}
	}

	@Test
	fun `return true for english uppercase letters`() {
		('A'..'Z')
			.forEach {
				KanaKanjiDetector
					.isRomaji(it)
					.shouldBeTrue()
			}
	}

	@Test
	fun `return false for kana, kanji or symbols`() {
		listOf('@', 'か', '日')
			.forEach {
				KanaKanjiDetector
					.isRomaji(it)
					.shouldBeFalse()
			}
	}
}