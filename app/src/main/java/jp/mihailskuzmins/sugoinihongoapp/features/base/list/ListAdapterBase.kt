package jp.mihailskuzmins.sugoinihongoapp.features.base.list

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.AppListAdapter

abstract class ListAdapterBase<TModel, TBinding: ViewDataBinding> :
	RecyclerView.Adapter<ListAdapterViewHolderBase<TModel, TBinding>>(), AppListAdapter<TModel> {

	private val mItems: MutableList<TModel> = mutableListOf()

	final override fun getItemCount() = mItems.size

	final override fun onBindViewHolder(holder: ListAdapterViewHolderBase<TModel, TBinding>, position: Int) =
		holder.bindView(mItems[position])

	final override fun updateItems(items: List<TModel>) {
		mItems.clear()
		mItems.addAll(items)
		notifyDataSetChanged()
	}
}