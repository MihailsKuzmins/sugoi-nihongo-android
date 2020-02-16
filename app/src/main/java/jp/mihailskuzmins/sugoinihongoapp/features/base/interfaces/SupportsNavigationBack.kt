package jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces

import io.reactivex.Observable

interface SupportsNavigationBack {

	val navBackObservable: Observable<Unit>
}