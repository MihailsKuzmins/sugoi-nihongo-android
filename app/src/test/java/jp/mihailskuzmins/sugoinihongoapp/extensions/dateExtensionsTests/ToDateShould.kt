package jp.mihailskuzmins.sugoinihongoapp.extensions.dateExtensionsTests

import jp.mihailskuzmins.sugoinihongoapp.extensions.toDate
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import java.util.*

class ToDateShould {

	@Test
	fun `return the same date without time`() {
		val calendar = Calendar.getInstance(Locale.getDefault())
		calendar.set(2019, 7, 31, 22, 55, 12)

		val result = calendar.time.toDate()
			.toString()

		calendar.add(Calendar.HOUR, -22)
		calendar.add(Calendar.MINUTE, -55)
		calendar.add(Calendar.SECOND, -12)

		calendar.time.toString()
			.shouldBeEqualTo(result)
	}
}