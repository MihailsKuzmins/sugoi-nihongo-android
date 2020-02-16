package jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops

import jp.mihailskuzmins.sugoinihongoapp.helpers.BaseObservable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BindableProp<T: BaseObservable, R>(
	private val mFieldId: Int,
	defaultValue: R,
	private val mOnValueChanged: ((R) -> Unit)? = null) :
	ReadWriteProperty<T, R> {

	private var mField: R = defaultValue

	override fun getValue(thisRef: T, property: KProperty<*>): R =
		mField

	override fun setValue(thisRef: T, property: KProperty<*>, value: R) {
		if (mField == value)
			return

		mField = value
		mOnValueChanged?.invoke(value)
		thisRef.notifyPropertyChanged(mFieldId)
	}
}