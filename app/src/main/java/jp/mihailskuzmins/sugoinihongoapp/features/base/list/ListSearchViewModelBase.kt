package jp.mihailskuzmins.sugoinihongoapp.features.base.list

import android.app.Application
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.FilterableListViewModel
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.ReactiveProp
import java.util.concurrent.TimeUnit

abstract class ListSearchViewModelBase<TModel>(app: Application) :
	ListViewModelBase<TModel>(app), FilterableListViewModel {

	private val mSearchTextSubject: Subject<String> = ReplaySubject.createWithSize(1)
	private var mItems = emptyList<TModel>()

	final override var searchText by ReactiveProp("", mSearchTextSubject::onNext)

	init {
		mSearchTextSubject
			.debounce(150, TimeUnit.MILLISECONDS)
			.distinctUntilChanged()
			.map { searchText ->
				mItems.filter { x -> filterBySearchText(x, searchText) }
			}.observeOn(AndroidSchedulers.mainThread())
			.subscribe { publishItems(it) }
			.addTo(cleanUp)
	}

	final override fun initItems(itemsAction: ItemsAction<TModel>) {
		initSearchItems {
			mItems = it

			it.filter { x -> filterBySearchText(x, searchText) }
				.invoke(itemsAction)
		}
	}

	protected abstract fun initSearchItems(itemsAction: ItemsAction<TModel>)

	protected abstract fun filterItem(item: TModel, searchText: String): Boolean

	private fun filterBySearchText(item: TModel, searchText: String) =
		searchText.isBlank() ||  filterItem(item, searchText.trim())
}