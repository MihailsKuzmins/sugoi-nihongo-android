package jp.mihailskuzmins.sugoinihongoapp.helpers.rx

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
typealias ListAction<E> = (list: MutableList<E>) -> Unit

class SourceList<E: Any> {

	private val mList: MutableList<E> = mutableListOf()
	private val mChangedSubject: Subject<Unit> = PublishSubject.create()

	val collectionChanged: Observable<List<E>>
		get() =  mChangedSubject.map { mList }

	fun add(element: E) {
		mList.add(element)
		invokeNext()
	}

	fun remove(clazz: Class<*>) {
		val elements = mList
			.filter { it::class.java == clazz }

		if (elements.isNotEmpty()) {
			mList.removeAll(elements)
			invokeNext()
		}
	}

	private fun invokeNext() =
		mChangedSubject.onNext(Unit)
}