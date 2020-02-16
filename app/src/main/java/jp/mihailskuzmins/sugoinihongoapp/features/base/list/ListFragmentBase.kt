package jp.mihailskuzmins.sugoinihongoapp.features.base.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.AppListAdapter
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasRecyclerView
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.initRecyclerView
import jp.mihailskuzmins.sugoinihongoapp.userinterface.helpers.AppLinearLayoutManager

abstract class ListFragmentBase<TModel, TViewModel: ListViewModelBase<TModel>>(@LayoutRes layoutId: Int) :
	FragmentBase<TViewModel>(layoutId), HasRecyclerView {

	private val mLayoutManager: AppLinearLayoutManager
		get() = recyclerView.layoutManager as AppLinearLayoutManager

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
		super.onCreateView(inflater, container, savedInstanceState)
			.also { recyclerView.adapter = createRecyclerViewAdapter() }
			.also { initRecyclerView() }

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		viewModel.itemsObservable
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(::updateItems)
			.addTo(d)
	}

	protected abstract fun createRecyclerViewAdapter(): RecyclerView.Adapter<*>

	override fun bindView() {}

	protected open fun initRecyclerView() =
		initRecyclerView(context!!)

	@Suppress("UNCHECKED_CAST")
	private fun updateItems(items: List<TModel>) {
		(recyclerView.adapter as AppListAdapter<TModel>).updateItems(items)
		mLayoutManager.scrollToLastKnowPosition()
	}
}