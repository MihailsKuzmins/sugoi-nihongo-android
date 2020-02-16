package jp.mihailskuzmins.sugoinihongoapp.helpers.utils

import android.content.Context
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import jp.mihailskuzmins.sugoinihongoapp.BuildConfig
import jp.mihailskuzmins.sugoinihongoapp.extensions.invokeIf

class SharedPreferencesUtil(context: Context) {

	private val mRxPreferences: RxSharedPreferences = context
		.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
		.run(RxSharedPreferences::create)

	// Int
	fun getInt(key: String) =
		mRxPreferences
			.getInteger(key)
			.get()

	fun setInt(key: String, value: Int) =
		mRxPreferences
			.getInteger(key)
			.set(value)

	fun setIntIfNotSet(key: String, value: Int) =
		mRxPreferences
			.getInteger(key)
			.setIfNotSet(value)

	fun observeInt(key: String): Observable<Int> =
		mRxPreferences
			.getInteger(key)
			.asObservable()
			.distinctUntilChanged()

	// Boolean
	fun getBoolean(key: String) =
		mRxPreferences
			.getBoolean(key)
			.get()

	fun setBoolean(key: String, value: Boolean) =
		mRxPreferences
			.getBoolean(key)
			.set(value)

	fun setBooleanIfNotSet(key: String, value: Boolean) =
		mRxPreferences
			.getBoolean(key)
			.setIfNotSet(value)

	fun observeBool(key: String): Observable<Boolean> =
		mRxPreferences
			.getBoolean(key)
			.asObservable()
			.distinctUntilChanged()

	// Long
	fun getLong(key: String) =
		mRxPreferences
			.getLong(key)
			.get()

	fun setLong(key: String, value: Long) =
		mRxPreferences
			.getLong(key)
			.set(value)

	private fun <T> Preference<T>.setIfNotSet(value: T) =
		invokeIf({ !it.isSet }, { it.set(value) })
}