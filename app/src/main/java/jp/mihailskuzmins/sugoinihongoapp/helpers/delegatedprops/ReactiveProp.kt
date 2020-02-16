package jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ReactiveProp<T, R>(
	defaultValue: R,
	private val mOnValueChanged: (R) -> Unit) :
	ReadWriteProperty<T, R> {

	private var mField = defaultValue

	override fun getValue(thisRef: T, property: KProperty<*>): R =
		mField

	override fun setValue(thisRef: T, property: KProperty<*>, value: R) {
		if (mField == value)
			return

		mField = value
		mOnValueChanged.invoke(value)
	}
}