package jp.mihailskuzmins.sugoinihongoapp.userinterface.controls.tableview

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

abstract class TableViewAdapterBase<TModel, TBinding: ViewDataBinding>(private val mItems: List<TModel>) {

	fun getItemsCount() = mItems.size

	fun onBindViewHolder(holder: TableViewHolderBase<TModel, out TBinding>, position: Int) =
		holder.bindView(mItems[position])

	open fun getItemType(position: Int) = 0

	abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolderBase<TModel, out TBinding>


	abstract class TableViewHolderBase<TModel, TBinding: ViewDataBinding>(private val mBinding: TBinding) {

		val view = mBinding.root

		fun bindView(model: TModel) =
			bindView(model, mBinding)

		protected abstract fun bindView(model: TModel, binding: TBinding)
	}
}