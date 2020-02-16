package jp.mihailskuzmins.sugoinihongoapp.features.maxLengthRuleTests

import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test
import kotlin.test.assertFailsWith

class ValidateShould : MaxLengthRuleTestsBase() {

	@Test
	fun `return true if length is less than the max length`() {
		val maxLength = 4; val value = "123"

		val result = createClass(maxLength)
			.validate(value)

		result.shouldBeTrue()
	}

	@Test
	fun `return true if length is the same as the max length`() {
		val maxLength = 4; val value = "1234"

		val result = createClass(maxLength)
			.validate(value)

		result.shouldBeTrue()
	}

	@Test
	fun `return false if length is greater than the max length`() {
		val maxLength = 4; val value = "12345"

		val result = createClass(maxLength)
			.validate(value)

		result.shouldBeFalse()
	}

	@Test
	fun `throw exception if max length is zero`() {
		assertFailsWith<IllegalStateException> {
			createClass(0).validate("any")
		}
	}

	@Test
	fun `throw exception if max length is negative`() {
		assertFailsWith<IllegalStateException> {
			createClass(-1).validate("any")
		}
	}
}