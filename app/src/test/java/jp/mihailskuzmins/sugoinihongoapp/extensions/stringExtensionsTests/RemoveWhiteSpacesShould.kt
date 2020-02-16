package jp.mihailskuzmins.sugoinihongoapp.extensions.stringExtensionsTests

import jp.mihailskuzmins.sugoinihongoapp.extensions.removeWhiteSpaces
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class RemoveWhiteSpacesShould {

	@Test
	fun `not change string if it does not have white spaces`() {
		val value = "value value"

		value
			.removeWhiteSpaces()
			.shouldBeEqualTo(value)
	}

	@Test
	fun `remove spaces if greater than single`() {
		val part1 = "aaa"
		val part2 = "bbb"
		val part3 = "ccc"

		val expectedResult = "$part1 $part2 $part3"

		"$part1  $part2   $part3"
			.removeWhiteSpaces()
			.shouldBeEqualTo(expectedResult)
	}

	@Test
	fun `remove new lines`() {
		val part1 = "aaa"
		val part2 = "bbb"

		val expectedResult = "$part1 $part2"

		StringBuilder()
			.appendln(part1)
			.appendln()
			.append(part2)
			.toString()
			.removeWhiteSpaces()
			.shouldBeEqualTo(expectedResult)
	}

	@Test
	fun `trim string`() {
		val part1 = "aaa"
		val part2 = "bbb"

		val expectedResult = "$part1 $part2"

		" $part1  $part2 "
			.removeWhiteSpaces()
			.shouldBeEqualTo(expectedResult)
	}
}