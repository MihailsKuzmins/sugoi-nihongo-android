package jp.mihailskuzmins.sugoinihongoapp.persistence.kanaRepoTests

import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.asset.Kana
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotContain
import org.junit.Test

class KanaRepoReplaceDoubleConsonantsWithSokuonShould : KanaRepoTestsBase() {

	@Test
	fun `replace consonants with respective hiragana sokuon`() {
		val input = StringBuilder("kissaten")
		val kana = Kana.Hiragana

		createClass()
			.replaceDoubleConsonantsWithSokuon(input, kana)
			.toString()
			.shouldBeEqualTo("ki${kana.sokuon}saten")
	}

	@Test
	fun `replace consonants with respective katakana sokuon`() {
		val input = StringBuilder("pokkii")
		val kana = Kana.Katakana

		createClass()
			.replaceDoubleConsonantsWithSokuon(input, kana)
			.toString()
			.shouldBeEqualTo("po${kana.sokuon}kii")
	}

	@Test
	fun `replace multiple consonants with sokuon`() {
		val input = StringBuilder("pakkottessan")
		val kana = Kana.Katakana
		val sokuon = kana.sokuon

		createClass()
			.replaceDoubleConsonantsWithSokuon(input, kana)
			.toString()
			.shouldBeEqualTo("pa${sokuon}ko${sokuon}te${sokuon}san")
	}

	@Test
	fun `not replace vowels with sokuon`() {
		val input = "boopaakuujeenii"
		val kana = Kana.Katakana

		createClass()
			.replaceDoubleConsonantsWithSokuon(StringBuilder(input), kana)
			.toString()
			.shouldBeEqualTo(input)
			.shouldNotContain(kana.sokuon)
	}

	@Test
	fun `not replace n with sokuon for hiragana`() {
		val input = "annaisuru"
		val kana = Kana.Hiragana

		createClass()
			.replaceDoubleConsonantsWithSokuon(StringBuilder(input), kana)
			.toString()
			.shouldBeEqualTo(input)
			.shouldNotContain(kana.sokuon)
	}

	@Test
	fun `not replace n with sokuon for katakana`() {
		val input = "aanuri"
		val kana = Kana.Katakana

		createClass()
			.replaceDoubleConsonantsWithSokuon(StringBuilder(input), kana)
			.toString()
			.shouldBeEqualTo(input)
			.shouldNotContain(kana.sokuon)
	}

	@Test
	fun `replace with sokuon ch characters for hiragana`() {
		val input = StringBuilder("kamitchi")
		val kana = Kana.Hiragana

		createClass()
			.replaceDoubleConsonantsWithSokuon(input, kana)
			.toString()
			.shouldBeEqualTo("kami${kana.sokuon}chi")
	}
}