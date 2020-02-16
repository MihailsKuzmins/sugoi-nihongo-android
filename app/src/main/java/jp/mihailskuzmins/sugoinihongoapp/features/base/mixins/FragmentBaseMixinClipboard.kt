package jp.mihailskuzmins.sugoinihongoapp.features.base.mixins

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.toast
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.ViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasClipboardAction

fun <TViewModel> FragmentBase<TViewModel>.subscribeToClipboard(): Disposable
		where TViewModel : ViewModelBase,
		      TViewModel : HasClipboardAction {

	return viewModel.clipboardValue
		.filter(String::isNotBlank)
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe {
			context!!
				.getString(R.string.general_copied_to_clipboard, it)
				.invoke { x -> context!!.toast(x) }
		}
}