package jp.mihailskuzmins.sugoinihongoapp.persistence.firestoreRepoBaseTests

import com.google.firebase.firestore.FieldValue
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.OnSetAction
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.DefaultValues
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldContainSame
import org.junit.Test
import java.util.*

class CreateSaveDataShould : FirestoreRepoBaseTestsBase() {

	private val mFirebaseDeleteValue: FieldValue by lazy { FieldValue.delete() }

	@Test
	fun `trim string value if in mandatory props`() {
		val prop = "prop1"; val value = "string val"
		val mandatoryProps = setOf(prop)
		val data = mapOf(prop to "  $value  ")

		val expectedResult = mapOf(prop to value)

		val result = createClass()
			.createSaveData(data, false, mandatoryProps, null)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `return null if in mandatory props`() {
		val prop1 = "prop1"; val prop2 = "prop2"; val prop3 = "prop3"
		val value1 = null; val value2 = ""; val value3 = " "
		val mandatoryProps = setOf(prop1, prop2, prop3)
		val data = mapOf(prop1 to value1, prop2 to value2, prop3 to value3)

		val expectedResult = mapOf(
			prop1 to value1,
			prop2 to value2,
			prop3 to value2) // blank is trimmed

		val result = createClass()
			.createSaveData(data, false, mandatoryProps, null)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `trim string value if not in mandatory props`() {
		val prop = "prop1"; val value = "string val"
		val data = mapOf(prop to "  $value  ")

		val expectedResult = mapOf(prop to value)

		val result = createClass()
			.createSaveData(data, false, null, null)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `not return blank string is new and not mandatory`() {
		val prop1 = "prop1"; val prop2 = "prop2"
		val value1 = ""; val value2 = " "
		val data = mapOf(prop1 to value1, prop2 to value2)

		val result = createClass()
			.createSaveData(data, true, null, null)

		result.shouldBeEmpty()
	}

	@Test
	fun `delete null and blank if not null and is not mandatory`() {
		val prop1 = "prop1"; val prop2 = "prop2"; val prop3 = "prop3"
		val value1 = ""; val value2 = " "; val value3 = null
		val data = mapOf(prop1 to value1, prop2 to value2, prop3 to value3)

		val expectedResult = mapOf(
			prop1 to mFirebaseDeleteValue,
			prop2 to mFirebaseDeleteValue,
			prop3 to mFirebaseDeleteValue
		)

		val result = createClass()
			.createSaveData(data, false, null, null)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `return default boolean if mandatory`() {
		val prop = "prop"; val value = DefaultValues.boolean
		val mandatoryProps = setOf(prop)
		val data = mapOf(prop to value)

		val expectedResult = mapOf(prop to value)

		val result = createClass()
			.createSaveData(data, false, mandatoryProps, null)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `return boolean if not default and not mandatory`() {
		val prop = "prop"; val value = !DefaultValues.boolean
		val data = mapOf(prop to value)

		val expectedResult = mapOf(prop to value)

		val result = createClass()
			.createSaveData(data, false, null, null)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `not return boolean if default, new and not mandatory`() {
		val prop = "prop"; val value = DefaultValues.boolean
		val data = mapOf(prop to value)

		val result = createClass()
			.createSaveData(data, true, null, null)

		result.shouldBeEmpty()
	}

	@Test
	fun `delete boolean if default, not new and not mandatory`() {
		val prop = "prop"
		val data = mapOf(prop to DefaultValues.boolean)

		val expectedResult = mapOf(prop to mFirebaseDeleteValue)

		val result = createClass()
			.createSaveData(data, false, null, null)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `return default int if mandatory`() {
		val prop = "prop"; val value = DefaultValues.int
		val mandatoryProps = setOf(prop)
		val data = mapOf(prop to value)

		val expectedResult = mapOf(prop to value)

		val result = createClass()
			.createSaveData(data, false, mandatoryProps, null)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `return int if not default and not mandatory`() {
		val prop = "prop"; val value = DefaultValues.int + 1
		val data = mapOf(prop to value)

		val expectedResult = mapOf(prop to value)

		val result = createClass()
			.createSaveData(data, false, null, null)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `not return int if default, new and not mandatory`() {
		val prop = "prop"; val value = DefaultValues.int
		val data = mapOf(prop to value)

		val result = createClass()
			.createSaveData(data, true, null, null)

		result.shouldBeEmpty()
	}

	@Test
	fun `delete int if default, not new and not mandatory`() {
		val prop = "prop"
		val data = mapOf(prop to DefaultValues.int)

		val expectedResult = mapOf(prop to mFirebaseDeleteValue)

		val result = createClass()
			.createSaveData(data, false, null, null)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `return default double if mandatory`() {
		val prop = "prop"; val value = DefaultValues.double
		val mandatoryProps = setOf(prop)
		val data = mapOf(prop to value)

		val expectedResult = mapOf(prop to value)

		val result = createClass()
			.createSaveData(data, false, mandatoryProps, null)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `return double if not default and not mandatory`() {
		val prop = "prop"; val value = DefaultValues.double + 0.1
		val data = mapOf(prop to value)

		val expectedResult = mapOf(prop to value)

		val result = createClass()
			.createSaveData(data, false, null, null)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `not return double if default, new and not mandatory`() {
		val prop = "prop"; val value = DefaultValues.double
		val data = mapOf(prop to value)

		val result = createClass()
			.createSaveData(data, true, null, null)

		result.shouldBeEmpty()
	}

	@Test
	fun `delete double if default, not new and not mandatory`() {
		val prop = "prop"
		val data = mapOf(prop to DefaultValues.double)

		val expectedResult = mapOf(prop to mFirebaseDeleteValue)

		val result = createClass()
			.createSaveData(data, false, null, null)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `return default date if mandatory`() {
		val prop = "prop"; val value = DefaultValues.date
		val mandatoryProps = setOf(prop)
		val data = mapOf(prop to value)

		val expectedResult = mapOf(prop to value)

		val result = createClass()
			.createSaveData(data, false, mandatoryProps, null)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `return date if not default and not mandatory`() {
		val prop = "prop"; val value = GregorianCalendar(1930, 0, 2).time
		val data = mapOf(prop to value)

		val expectedResult = mapOf(prop to value)

		val result = createClass()
			.createSaveData(data, false, null, null)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `not return date if default, new and not mandatory`() {
		val prop = "prop"; val value = DefaultValues.date
		val data = mapOf(prop to value)

		val result = createClass()
			.createSaveData(data, true, null, null)

		result.shouldBeEmpty()
	}

	@Test
	fun `delete date if default, not new and not mandatory`() {
		val prop = "prop"
		val data = mapOf(prop to DefaultValues.date)

		val expectedResult = mapOf(prop to mFirebaseDeleteValue)

		val result = createClass()
			.createSaveData(data, false, null, null)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `invoke onSet of method is not null and is new`() {
		val prop = "prop"; val addProp = "addProp"
		val value = 123; val addValue = 321
		val data =  mapOf(prop to value)
		val onSetAction: OnSetAction = { x: MutableMap<String, Any?> -> x[addProp] = addValue }

		val expectedResult = mapOf(
			prop to value,
			addProp to addValue)

		val result = createClass()
			.createSaveData(data, true, null, onSetAction)

		result.shouldContainSame(expectedResult)
	}

	@Test
	fun `not invoke onSet of method is not null and is not new`() {
		val prop = "prop"; val addProp = "addProp"
		val value = 123; val addValue = 321
		val data =  mapOf(prop to value)
		val onSetAction: OnSetAction = { x: MutableMap<String, Any?> -> x[addProp] = addValue }

		val expectedResult = mapOf(prop to value)

		val result = createClass()
			.createSaveData(data, false, null, onSetAction)

		result.shouldContainSame(expectedResult)
	}
}