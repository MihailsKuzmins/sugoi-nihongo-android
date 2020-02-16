package jp.mihailskuzmins.sugoinihongoapp.extensions.anyExtensionsTests

import jp.mihailskuzmins.sugoinihongoapp.extensions.isEqual
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test

class IsNotEqualShould {

	@Test
	fun `return false for different strings`() {
		"aaa"
			.isEqual("bbb")
			.shouldBeFalse()
	}

	@Test
	fun `return true for identical strings`() {
		"aaa"
			.isEqual("aaa")
			.shouldBeTrue()
	}

	@Test
	fun `trim strings and return true for identical strings`() {
		" aaa "
			.isEqual("aaa")
			.shouldBeTrue()
	}

	@Test
	fun `return true for identical booleans`() {
		true
			.isEqual(true)
			.shouldBeTrue()

		false
			.isEqual(false)
			.shouldBeTrue()
	}

	@Test
	fun `return false for not identical booleans`() {
		false
			.isEqual(true)
			.shouldBeFalse()

		true
			.isEqual(false)
			.shouldBeFalse()
	}

	@Test
	fun `return true for identical ints`() {
		123
			.isEqual(123)
			.shouldBeTrue()
	}

	@Test
	fun `return false for not identical ints`() {
		123
			.isEqual(321)
			.shouldBeFalse()
	}
}