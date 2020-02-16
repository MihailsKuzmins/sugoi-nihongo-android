package jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces

import io.reactivex.Observable

interface SupportsNavigation {

	val navActionIdObservable: Observable<Int>
}