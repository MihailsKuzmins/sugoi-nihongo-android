package jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle

import android.app.AlertDialog
import android.content.Context
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.AppFragment
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.configs.ConfirmConfig

class AppConfirmDialog : AppDialog<ConfirmConfig>() {

	private val mResultSubject: Subject<Result> = ReplaySubject.createWithSize(1)

	val yesObservable: Observable<Unit>
		get() = mResultSubject
			.filter { it == Result.Yes }
			.map { Unit }

	fun subscribe(fragment: AppFragment): Disposable =
		configObservable
			.map { createConfirm(fragment.context!!, it) }
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { x -> x.show() }

	private fun createConfirm(context: Context, config: ConfirmConfig) =
		AlertDialog.Builder(context)
			.setCancelable(false)
			.setTitle(config.title)
			.setMessage(config.message)
			.setPositiveButton(android.R.string.yes) { dialog, _ ->
				mResultSubject.onNext(Result.Yes)
				dialog.cancel()
			}
			.setNegativeButton(android.R.string.no) { dialog, _ ->
				mResultSubject.onNext(Result.No)
				dialog.cancel()
			}.setOnCancelListener { cancelDialog() }
			.create()

	private enum class Result {
		Yes,
		No
	}
}