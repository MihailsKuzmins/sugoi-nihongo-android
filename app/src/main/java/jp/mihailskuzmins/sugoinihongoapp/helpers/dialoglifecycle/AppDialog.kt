package jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle

import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.helpers.Box

abstract class AppDialog<TConfig> {

	private val mConfigBoxSubject: Subject<Box<TConfig?>> = ReplaySubject.createWithSize(1)

	var config: TConfig? = null
		set(value) {
			field = value
			mConfigBoxSubject.onNext(Box(value))
		}

	protected val configObservable: Observable<TConfig>
		get() = mConfigBoxSubject
			.filter { it.value != null }
			.map { it.value }

	protected fun cancelDialog() {
		config = null
	}
}