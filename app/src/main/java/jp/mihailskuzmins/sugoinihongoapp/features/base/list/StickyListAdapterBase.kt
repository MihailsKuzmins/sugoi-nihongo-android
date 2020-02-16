package jp.mihailskuzmins.sugoinihongoapp.features.base.list

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.ViewDataBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.AppListAdapter
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter

abstract class StickyListAdapterBase<TModel, TBinding: ViewDataBinding, THeaderBinding: ViewDataBinding> :
	BaseAdapter(), AppListAdapter<TModel>, StickyListHeadersAdapter {

	private val mItems: MutableList<TModel> = mutableListOf()

	final override fun updateItems(items: List<TModel>) {
		mItems.clear()
		mItems.addAll(items)
		notifyDataSetChanged()
	}

	final override fun getCount() =
		mItems.size

	final override fun getItem(position: Int) =
		mItems[position]

	final override fun getItemId(position: Int) =
		position.toLong()

	final override fun getHeaderId(position: Int) =
		getHeaderId(mItems[position])

	final override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
		getItemView<TBinding>(position, convertView, parent, false)

	final override fun getHeaderView(position: Int, convertView: View?, parent: ViewGroup) =
		getItemView<THeaderBinding>(position, convertView, parent, true)

	protected abstract fun onCreateViewHolder(parent: ViewGroup): ListAdapterViewHolderBase<TModel, TBinding>
	protected abstract fun onCreateHeaderViewHolder(parent: ViewGroup): ListAdapterViewHolderBase<TModel, THeaderBinding>
	protected abstract fun getHeaderId(model: TModel): Long

	@Suppress("UNCHECKED_CAST")
	private fun <T: ViewDataBinding> getItemView(position: Int, convertView: View?, parent: ViewGroup, isHeader: Boolean): View {
		convertView?.let {
			(convertView.tag as ListAdapterViewHolderBase<TModel, T>)
				.invoke { onBindViewHolder(it, position) }

			return@getItemView convertView
		}

		val viewHolder = if (isHeader) ::onCreateHeaderViewHolder
		else ::onCreateViewHolder

		return viewHolder(parent)
			.also { onBindViewHolder(it, position) }
			.run {
				this.itemView.also {
					it.tag = this@run
				}
			}
	}

	private fun <T: ViewDataBinding> onBindViewHolder(viewHolder: ListAdapterViewHolderBase<TModel, T>, position: Int) =
		viewHolder.bindView(mItems[position])
}