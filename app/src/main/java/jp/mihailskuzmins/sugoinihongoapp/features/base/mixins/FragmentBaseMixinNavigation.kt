package jp.mihailskuzmins.sugoinihongoapp.features.base.mixins

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigate
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigateUp
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.ViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsNavDirections
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsNavigation
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsNavigationBack
import jp.mihailskuzmins.sugoinihongoapp.resources.NavMode

fun <TViewModel> FragmentBase<TViewModel>.subscribeToNavigation(navMode: NavMode, popUpTo: Int? = null): Disposable
		where TViewModel : ViewModelBase,
		      TViewModel : SupportsNavigation {

	return viewModel.navActionIdObservable
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe { navigate(it, popUpTo, navMode) }
}

fun <TViewModel> FragmentBase<TViewModel>.subscribeToNavDirections(navMode: NavMode, popUpTo: Int? = null): Disposable
		where TViewModel : ViewModelBase,
		      TViewModel : SupportsNavDirections {

	return viewModel.navDirectionsObservable
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe { navigate(it, popUpTo, navMode) }
}

fun <TViewModel> FragmentBase<TViewModel>.subscribeToNavigationBack(navMode: NavMode): Disposable
		where TViewModel : ViewModelBase,
		      TViewModel : SupportsNavigationBack {

	return viewModel.navBackObservable
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe { navigateUp(navMode) }
}