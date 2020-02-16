package jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.configs

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding

class AlertViewConfig<TModel, TBinding: ViewDataBinding>(
	@LayoutRes val dialogLayoutId: Int,
	val model: TModel,
	val applyModelFunc: (TModel, TBinding) -> Unit) {

	@StringRes
	var okText: Int? = null
	var title: String = ""
}