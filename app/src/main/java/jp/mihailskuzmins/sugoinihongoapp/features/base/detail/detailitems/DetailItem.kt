package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface DetailItem: Disposable {

	val isValidObservable: Observable<Boolean>
	val isValid: Boolean
	val isValueChanged: Boolean

	val label: String
	val tag: String?

	val valueAny: Any

	var isEnabled: Boolean
	var isVisible: Boolean
}