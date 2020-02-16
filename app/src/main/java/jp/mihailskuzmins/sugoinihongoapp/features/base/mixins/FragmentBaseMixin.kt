package jp.mihailskuzmins.sugoinihongoapp.features.base.mixins

import androidx.core.view.isVisible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.ViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasItemsViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasNoResultsTextView

fun <TFragment, TViewModel> TFragment.subscribeToNoResultsTextView(): Disposable
	where TFragment : FragmentBase<TViewModel>,
	      TFragment: HasNoResultsTextView,
	      TViewModel : ViewModelBase,
	      TViewModel : HasItemsViewModel {

	return Observables
		.combineLatest(
			viewModel.isInitialised,
			viewModel.hasItemsObservable)
		{ x, y -> Pair(x, y) }
		.filter { x -> x.first }
		.map { x -> !x.second }
		.distinctUntilChanged()
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe { noResultsTextView.isVisible = it }
}