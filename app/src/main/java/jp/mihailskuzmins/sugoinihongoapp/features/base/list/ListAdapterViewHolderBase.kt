package jp.mihailskuzmins.sugoinihongoapp.features.base.list

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class ListAdapterViewHolderBase<TModel, TBinding: ViewDataBinding>(
	private val binding: TBinding
): RecyclerView.ViewHolder(binding.root) {

	fun bindView(model: TModel) =
		bindView(model, binding)

	protected abstract fun bindView(model: TModel, binding: TBinding)
}