package jp.mihailskuzmins.sugoinihongoapp.features.base.mixins

import io.reactivex.disposables.Disposable
import jp.mihailskuzmins.sugoinihongoapp.features.base.ViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsDelete

fun <TViewModel> TViewModel.subscribeToDeleteConfirm(): Disposable
	where TViewModel : ViewModelBase,
	      TViewModel : SupportsDelete {

	return deleteConfirmDialog.yesObservable
		.subscribe{
			delete()
			publishDelete()
		}
}