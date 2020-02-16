package jp.mihailskuzmins.sugoinihongoapp.helpers.rx

import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.extensions.letIfNull
import java.lang.reflect.Type

private typealias RxBusElement = Pair<Type, String>
private typealias RxBusAction = (map: Map<RxBusElement, Subject<Any>>, element: RxBusElement) -> Unit

object RxBus {

	private val mMessageBus = mutableMapOf<RxBusElement, Subject<Any>>()

	fun <T: Any> sentMessage(message: T, clazz: Class<T>, contract: String) {
		initSubjectIfNecessary(clazz, contract)
			.onNext(message)
	}

	@Suppress("UNCHECKED_CAST")
	fun <T: Any> listen(clazz: Class<T>, contract: String): Observable<T> =
		initSubjectIfNecessary(clazz, contract)
			// for Boolean there was a weird exception: java.lang.ClassCastException: Cannot cast java.lang.Boolean to boolean
			.map { it as T }
			.distinctUntilChanged()

	private fun <T> initSubjectIfNecessary(clazz: Class<T>, contract: String): Subject<Any> {
		val pair = Pair(clazz, contract)

		return mMessageBus[pair].letIfNull {
			val subject = ReplaySubject.createWithSize<Any>(1)
			mMessageBus[pair] = subject

			return subject
		}
	}
}