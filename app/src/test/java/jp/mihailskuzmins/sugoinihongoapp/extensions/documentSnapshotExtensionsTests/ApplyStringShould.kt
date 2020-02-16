package jp.mihailskuzmins.sugoinihongoapp.extensions.documentSnapshotExtensionsTests

import com.google.firebase.firestore.DocumentSnapshot
import io.mockk.every
import io.mockk.mockk
import jp.mihailskuzmins.sugoinihongoapp.extensions.applyString
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test

class ApplyStringShould {

	@Test
	fun `not invoke function if key does not exist`() {
		var check = false

		val mockDocumentSnapshot = mockk<DocumentSnapshot>()
		every { mockDocumentSnapshot.getString(any()) } returns null

		mockDocumentSnapshot
			.applyString("key") {
				check = true
			}

		check
			.shouldBeFalse()
	}

	@Test
	fun `invoke function if key exists`() {
		var check = false

		val mockDocumentSnapshot = mockk<DocumentSnapshot>()
		every { mockDocumentSnapshot.getString(any()) } returns "value"

		mockDocumentSnapshot
			.applyString("key") {
				check = true
			}

		check
			.shouldBeTrue()
	}
}