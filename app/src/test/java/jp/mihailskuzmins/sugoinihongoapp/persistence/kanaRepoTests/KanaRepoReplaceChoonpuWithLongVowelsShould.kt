package jp.mihailskuzmins.sugoinihongoapp.persistence.kanaRepoTests

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class KanaRepoReplaceChoonpuWithLongVowelsShould : KanaRepoTestsBase() {

	@Test
	fun `replace choonpu character with previous`() {
		val input = StringBuilder("meーru")

		createClass()
			.replaceChoonpuWithLongVowels(input)
			.toString()
			.shouldBeEqualTo("meeru")
	}

	@Test
	fun `remove choonpu character if it is the first one`() {
		val input = StringBuilder("ーmeーru")

		createClass()
			.replaceChoonpuWithLongVowels(input)
			.toString()
			.shouldBeEqualTo("meeru")
	}

	@Test
	fun `remove choonpu character if it is not after roman vowels`() {
		val input = StringBuilder("mーeーru")

		createClass()
			.replaceChoonpuWithLongVowels(input)
			.toString()
			.shouldBeEqualTo("meeru")
	}

	@Test
	fun `handle multiple error`() {
		val input = StringBuilder("ーmーeーrーu")

		createClass()
			.replaceChoonpuWithLongVowels(input)
			.toString()
			.shouldBeEqualTo("meeru")
	}
}