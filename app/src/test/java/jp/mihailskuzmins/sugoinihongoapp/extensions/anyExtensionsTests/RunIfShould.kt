package jp.mihailskuzmins.sugoinihongoapp.extensions.anyExtensionsTests

import jp.mihailskuzmins.sugoinihongoapp.extensions.runIf
import org.amshove.kluent.shouldBe
import org.junit.Test

class RunIfShould {

	@Test
	fun `return new object if predicate is true`() {
		val check = 1

		check
			.runIf(true) { check + 1 }
			.shouldBe(check + 1)
	}

	@Test
	fun `not return new object if predicate is false`() {
		val check = 1

		check
			.runIf(false) { check + 1 }
			.shouldBe(check)
	}

	@Test
	fun `return from fun true if predicate is true`() {
		val check = 1

		Any()
			.runIf(true, { check }, { check + 1 })
			.shouldBe(check)
	}

	@Test
	fun `return from fun false if predicate is false`() {
		val check = 1

		Any()
			.runIf(false, { check + 1 }, { check })
			.shouldBe(check)
	}
}