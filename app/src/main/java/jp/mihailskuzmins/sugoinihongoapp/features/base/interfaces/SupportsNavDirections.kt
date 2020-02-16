package jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces

import androidx.navigation.NavDirections
import io.reactivex.Observable

interface SupportsNavDirections {

	val navDirectionsObservable: Observable<NavDirections>
}