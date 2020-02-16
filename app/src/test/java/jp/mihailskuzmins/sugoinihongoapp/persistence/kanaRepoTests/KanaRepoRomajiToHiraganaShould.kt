package jp.mihailskuzmins.sugoinihongoapp.persistence.kanaRepoTests

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class KanaRepoRomajiToHiraganaShould : KanaRepoTestsBase() {

	@Test
	fun `correctly convert from romaji to hiragana`() {
		val repo = createClass()

		hiraganaMap.forEach {
			repo
				.romajiToHiragana(it.key)
				.shouldBeEqualTo(it.value)
		}
	}

	@Test
	fun `return empty string for empty string`() {
		createClass()
			.kanaToRomaji("")
			.shouldBeEmpty()
	}
}