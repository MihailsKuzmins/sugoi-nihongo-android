package jp.mihailskuzmins.sugoinihongoapp.extensions

import io.reactivex.disposables.CompositeDisposable
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsActivation

typealias ActivateAction = (cleanUp: CompositeDisposable) -> Unit

fun SupportsActivation.whenActivated(activationAction: ActivateAction) =
	this.activator.addActivationBlock {
		val d = CompositeDisposable()
		activationAction(d)
		return@addActivationBlock d
	}