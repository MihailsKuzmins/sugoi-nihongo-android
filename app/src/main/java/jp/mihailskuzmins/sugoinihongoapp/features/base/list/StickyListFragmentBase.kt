package jp.mihailskuzmins.sugoinihongoapp.features.base.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.extensions.getPrivateField
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.AppListAdapter
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasStickyExpandableListView
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasStickyListView
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.initListView
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter

abstract class StickyListFragmentBase<TModel, TViewModel: ListViewModelBase<TModel>>(@LayoutRes layoutId: Int) :
	FragmentBase<TViewModel>(layoutId) {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
		super.onCreateView(inflater, container, savedInstanceState)
			.also {
				when (this) {
					is HasStickyListView -> { listView.adapter = createAdapter(); initListView() }
					is HasStickyExpandableListView -> { listView.setAdapter(createAdapter()); initListView() }
					else -> throw Error("StickyListView interfaces are not implemented")
				}
			}

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		viewModel.itemsObservable
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(::updateItems)
			.addTo(d)
	}

	protected abstract fun createAdapter(): StickyListHeadersAdapter

	final override fun bindView() {}


	private fun updateItems(items: List<TModel>) {
		val adapter = when(this) {
			is HasStickyListView  -> listView.adapter
			is HasStickyExpandableListView -> {
				@Suppress("INACCESSIBLE_TYPE")
				listView.adapter.getPrivateField("mInnerAdapter")
			}
			else -> throw Error("StickyListView interfaces are not implemented")
		}

		@Suppress("UNCHECKED_CAST")
		(adapter as AppListAdapter<TModel>).updateItems(items)
	}
}