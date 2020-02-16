package jp.mihailskuzmins.sugoinihongoapp.extensions.anyExtensionsTests

import jp.mihailskuzmins.sugoinihongoapp.extensions.letIfNull
import org.amshove.kluent.shouldBe
import org.junit.Test

class LefIfNullShould {

	@Test
	fun `return the same object if not null`() {
		val source = 1
		val check = source + 1

		source
			.letIfNull { check }
			.shouldBe(source)
	}

	@Test
	fun `return new object if null`() {
		val source: Boolean? = null
		val check = 1

		source
			.letIfNull { check }
			.shouldBe(check)
	}
}