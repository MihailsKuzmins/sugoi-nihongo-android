package jp.mihailskuzmins.sugoinihongoapp.helpers

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

typealias CleanupAction = () -> CompositeDisposable

class ViewModelActivator: Disposable {

	private val mBlocks: MutableList<CleanupAction> = mutableListOf()
	private val mCleanUp = CompositeDisposable()
	private var isActivated = false

	fun activate() {
		if (isActivated)
			return

		mBlocks
			.map { it() }
			.forEach { it.addTo(mCleanUp) }

		isActivated = true
	}

	fun addActivationBlock(activateAction: CleanupAction) =
		mBlocks.add(activateAction)

	override fun isDisposed() =
		mCleanUp.isDisposed

	override fun dispose() =
		mCleanUp.dispose()
}