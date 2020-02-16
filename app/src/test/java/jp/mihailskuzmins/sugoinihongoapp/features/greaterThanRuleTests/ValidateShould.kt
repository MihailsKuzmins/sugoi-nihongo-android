package jp.mihailskuzmins.sugoinihongoapp.features.greaterThanRuleTests

import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test

class ValidateShould : GreaterThanRuleTestsBase() {

	@Test
	fun `return true if value is greater than the min value`() {
		val value = 123; val minValue = value - 1

		val result = createClass(minValue)
			.validate(value)

		result.shouldBeTrue()
	}

	@Test
	fun `return false if value is the same as the min value`() {
		val value = 123

		val result = createClass(value)
			.validate(value)

		result.shouldBeFalse()
	}

	@Test
	fun `return false if value is less than the min value`() {
		val value = 123; val minValue = value + 1

		val result = createClass(minValue)
			.validate(value)

		result.shouldBeFalse()
	}
}