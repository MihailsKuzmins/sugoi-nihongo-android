package jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import jp.mihailskuzmins.sugoinihongoapp.extensions.applyIf
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.AppFragment
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.configs.AlertViewConfig

class AppAlertViewDialog<TModel, TBinding: ViewDataBinding> :
	AppDialog<AlertViewConfig<TModel, TBinding>>() {

	fun subscribe(fragment: AppFragment): Disposable =
		configObservable
			.map { createAlert(fragment.context!!, it) }
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { x -> x.show() }

	private fun createAlert(context: Context, config: AlertViewConfig<TModel, TBinding>): AlertDialog {
		@StringRes val okText = config.okText ?: android.R.string.ok

		val dataBinding = DataBindingUtil.inflate<TBinding>(
			LayoutInflater.from(context), config.dialogLayoutId, null, false)

		config.applyModelFunc(config.model, dataBinding)

		return AlertDialog.Builder(context)
			.setView(dataBinding.root)
			.applyIf(config.title.isNotBlank()) { setTitle(config.title) }
			.setPositiveButton(okText) { dialog, _ -> dialog.cancel() }
			.setOnCancelListener { cancelDialog() }
			.create()
	}
}