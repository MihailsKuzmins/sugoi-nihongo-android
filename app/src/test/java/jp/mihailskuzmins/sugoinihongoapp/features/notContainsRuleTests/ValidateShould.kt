package jp.mihailskuzmins.sugoinihongoapp.features.notContainsRuleTests

import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test

class ValidateShould : NotContainsRuleTestsBase() {

	@Test
	fun `return true if string doesn't contain the character`() {
		val char = 'c'; val value = "aaa"

		val result = createClass(char)
			.validate(value)

		result.shouldBeTrue()
	}

	@Test
	fun `return false if string contains the character`() {
		val char = 'c'; val value = "aa${char}a"

		val result = createClass(char)
			.validate(value)

		result.shouldBeFalse()
	}

	@Test
	fun `return true if string is empty`() {
		val char = 'c'; val value = ""

		val result = createClass(char)
			.validate(value)

		result.shouldBeTrue()
	}
}