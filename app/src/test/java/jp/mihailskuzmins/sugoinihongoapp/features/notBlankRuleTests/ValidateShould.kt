package jp.mihailskuzmins.sugoinihongoapp.features.notBlankRuleTests

import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test

class ValidateShould : NotBlankRuleTestsBase() {

	@Test
	fun `return true if string has non-white-space characters`() {
		val result = createClass()
			.validate("  some value  ")

		result.shouldBeTrue()
	}

	@Test
	fun `return false if string is empty`() {
		val result = createClass()
			.validate("")

		result.shouldBeFalse()
	}

	@Test
	fun `return false if string is blank`() {
		val result = createClass()
			.validate(" ")

		result.shouldBeFalse()
	}
}