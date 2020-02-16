package jp.mihailskuzmins.sugoinihongoapp.extensions.anyExtensionsTests

import jp.mihailskuzmins.sugoinihongoapp.extensions.applyIf
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test

class ApplyIfShould {

	@Test
	fun `invoke function if predicate is true`() {
		var check = false

		Any()
			.applyIf(true) {
				check = true
				return
			}

		check
			.shouldBeTrue()
	}

	@Test
	fun `not invoke function if predicate is false`() {
		var check = false

		Any()
			.applyIf(false) {
				check = true
				return
			}

		check
			.shouldBeFalse()
	}
}