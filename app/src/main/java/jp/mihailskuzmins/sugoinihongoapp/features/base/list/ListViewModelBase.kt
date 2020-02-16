package jp.mihailskuzmins.sugoinihongoapp.features.base.list

import android.app.Application
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.extensions.whenActivated
import jp.mihailskuzmins.sugoinihongoapp.features.base.ViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.FilterableListViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasItemsViewModel
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.ReactiveProp

typealias ItemsAction<T> = (items: List<T>) -> Unit

abstract class ListViewModelBase<TModel>(app: Application): ViewModelBase(app), HasItemsViewModel {

	private val mItemsSubject: Subject<List<TModel>> = ReplaySubject.createWithSize(1)
	private val mHasItemsSubject: Subject<Boolean> = ReplaySubject.createWithSize(1)

	var hasItems by ReactiveProp(false, mHasItemsSubject::onNext)
		private set

	init {
		mItemsSubject
			.map { it.isNotEmpty() }
			.distinctUntilChanged()
			.subscribe { hasItems = it }
			.addTo(cleanUp)

		whenActivated {
			initItems { x ->
				mItemsSubject.onNext(x)
				isInitialisedSubject.onNext(true)
			}
		}
	}

	val itemsObservable: Observable<List<TModel>>
		get() = mItemsSubject

	final override val hasItemsObservable: Observable<Boolean>
		get() = mHasItemsSubject.startWith(hasItems).distinctUntilChanged()

	protected abstract fun initItems(itemsAction: ItemsAction<TModel>)

	@Suppress("unused") // Needed in order to prevent other viewModels from using this method
	protected fun FilterableListViewModel.publishItems(items: List<TModel>) =
		mItemsSubject.onNext(items)

}