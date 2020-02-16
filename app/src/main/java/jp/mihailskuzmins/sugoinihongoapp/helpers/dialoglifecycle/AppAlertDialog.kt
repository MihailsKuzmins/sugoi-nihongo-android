package jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.AppFragment
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.configs.AlertConfig

class AppAlertDialog : AppDialog<AlertConfig>() {

	fun subscribe(fragment: AppFragment): Disposable =
		configObservable
			.map { createAlert(fragment.context!!, it) }
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { x -> x.show() }

	private fun createAlert(context: Context, config: AlertConfig): AlertDialog {
		@StringRes val okText = config.okText ?: android.R.string.ok

		return AlertDialog.Builder(context)
			.setCancelable(config.isCancellable)
			.setTitle(config.title)
			.setMessage(config.message)
			.setPositiveButton(okText) { dialog, _ ->
				dialog.cancel()
				config.okAction?.invoke()
			}.setOnCancelListener { cancelDialog() }
			.create()
	}
}