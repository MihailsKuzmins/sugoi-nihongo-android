package jp.mihailskuzmins.sugoinihongoapp.persistence.kanaRepoTests

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotContain
import org.junit.Test

class KanaRepoReplaceLongVowelsWithChoonpuShould : KanaRepoTestsBase() {

	@Test
	fun `replace long vowels with choonpu character`() {
		val input = StringBuilder("meeru")

		createClass()
			.replaceLongVowelsWithChoonpu(input)
			.toString()
			.shouldBeEqualTo("meーru")
	}

	@Test
	fun `replace multiple long vowels with choonpu`() {
		val input = StringBuilder("baashoomee")

		createClass()
			.replaceLongVowelsWithChoonpu(input)
			.toString()
			.shouldBeEqualTo("baーshoーmeー")
	}

	@Test
	fun `not replace consonants with choonpu`() {
		val input = "kissaten"

		createClass()
			.replaceLongVowelsWithChoonpu(StringBuilder(input))
			.toString()
			.shouldBeEqualTo(input)
			.shouldNotContain('ー')
	}
}