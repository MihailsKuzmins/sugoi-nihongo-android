package jp.mihailskuzmins.sugoinihongoapp.features.settings.auth

import android.app.Application
import android.text.InputType
import android.view.inputmethod.EditorInfo
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.activities.SignInSignUpActivity
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.extensions.toMessage
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItem
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemEditText
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemEditTextPassword
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.initAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.NotBlankRule
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasAuthAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.createIsAuthDisposable
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.startActivity
import jp.mihailskuzmins.sugoinihongoapp.helpers.Command
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.AppAlertDialog
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.AppConfirmDialog
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.configs.AlertConfig
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.configs.ConfirmConfig
import jp.mihailskuzmins.sugoinihongoapp.helpers.rx.RxBus
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.AuthUtil
import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants.RxBusContracts
import jp.mihailskuzmins.sugoinihongoapp.resources.AuthResult
import jp.mihailskuzmins.sugoinihongoapp.userinterface.views.ButtonMessage
import javax.inject.Inject

class SettingsAuthDeleteAccountViewModel(app: Application) : DetailViewModelBase(app), HasAuthAction {

	@Inject lateinit var authUtil: AuthUtil

	private val mIsAuthInProgressConst = RxBusContracts.isAuthInProgress
	private val mDeleteSuccessSubject: Subject<Unit> = ReplaySubject.createWithSize(1)

	val emailItem = DetailItemEditText(app, R.string.auth_email, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
		.apply { addRule(NotBlankRule(false)) }

	val passwordItem = DetailItemEditTextPassword(app, R.string.auth_password,
			command = Command(::publishDeleteConfirm, EditorInfo.IME_ACTION_GO, ::isValid))
		.apply { addRule(NotBlankRule(false)) }

	override val authButton = ButtonMessage(app, R.string.settings_auth_delete_account, ::publishDeleteConfirm)
	val deleteConfirmDialog = AppConfirmDialog()
	val deleteErrorAlertDialog = AppAlertDialog()

	override var isAuth: Boolean = false
		private set

	init {
		createIsAuthDisposable()
			.addTo(cleanUp)

		deleteConfirmDialog.yesObservable
			.subscribe { deleteAccount() }
			.addTo(cleanUp)

		RxBus.listen(Boolean::class.java, mIsAuthInProgressConst)
			.subscribe { isAuth = it }
			.addTo(cleanUp)
	}

	val deleteSuccessObservable: Observable<Unit>
		get() = mDeleteSuccessSubject

	override fun initDetailItems(): Array<DetailItem> =
		arrayOf(emailItem, passwordItem)

	override fun initDetailItemValues(onInit: initAction) {
		onInit()
	}

	private fun deleteAccount() {
		RxBus.sentMessage(true, Boolean::class.java, mIsAuthInProgressConst)

		authUtil.deleteAccount(emailItem.value, passwordItem.value) {
			RxBus.sentMessage(false, Boolean::class.java, mIsAuthInProgressConst)

			if (it == AuthResult.Success) {
				startActivity(SignInSignUpActivity::class.java)
				mDeleteSuccessSubject.onNext(Unit)
			} else {
				publishDeleteError(it)
			}
		}
	}

	private fun publishDeleteConfirm() {
		val message = StringBuilder()
			.appendln(context.getString(R.string.settings_auth_delete_account_confirmation))
			.appendln()
			.append(context.getString(R.string.settings_auth_delete_account_warning))
			.toString()

		ConfirmConfig().apply {
			title = context.getString(R.string.general_attention)
			this.message = message
		}.invoke { deleteConfirmDialog.config = it }
	}

	private fun publishDeleteError(authResult: AuthResult) =
		AlertConfig().apply {
			title = context.getString(R.string.general_error)
			message = authResult.toMessage(context)
		}.invoke { deleteErrorAlertDialog.config = it }
}