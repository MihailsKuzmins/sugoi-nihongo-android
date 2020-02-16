package jp.mihailskuzmins.sugoinihongoapp.features.base.mixins

import androidx.annotation.StringRes
import io.reactivex.disposables.Disposable
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsDelete
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.configs.ConfirmConfig

fun <TViewModel> FragmentBase<TViewModel>.confirmDelete(@StringRes message: Int)
	where TViewModel : DetailViewModelBase,
	      TViewModel : SupportsDelete {

	ConfirmConfig().apply {
		title = context!!.getString(R.string.general_attention)
		this.message = context!!.getString(message)
	}.invoke { viewModel.deleteConfirmDialog.config = it }
}

fun <TViewModel> FragmentBase<TViewModel>.subscribeToDeleteDialog(): Disposable
		where TViewModel : DetailViewModelBase,
		      TViewModel : SupportsDelete {

	return viewModel.deleteConfirmDialog
		.subscribe(this)
}