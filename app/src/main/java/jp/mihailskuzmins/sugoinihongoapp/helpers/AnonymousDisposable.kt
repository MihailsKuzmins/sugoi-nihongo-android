package jp.mihailskuzmins.sugoinihongoapp.helpers

import io.reactivex.disposables.Disposable
typealias DisposableAction = () -> Unit

class AnonymousDisposable(private var disposableAction: DisposableAction?): Disposable {

	override fun isDisposed() =
		disposableAction == null

	override fun dispose() {
		disposableAction?.invoke()
		disposableAction = null
	}
}