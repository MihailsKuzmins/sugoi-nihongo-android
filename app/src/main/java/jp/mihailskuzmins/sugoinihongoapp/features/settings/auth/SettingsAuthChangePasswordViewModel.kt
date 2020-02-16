package jp.mihailskuzmins.sugoinihongoapp.features.settings.auth

import android.app.Application
import android.view.inputmethod.EditorInfo
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.extensions.toMessage
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItem
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemEditTextPassword
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.initAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.EqualToFuncRule
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.MinLengthRule
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.NotBlankRule
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasAuthAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsNavigationBack
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.createIsAuthDisposable
import jp.mihailskuzmins.sugoinihongoapp.helpers.Command
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.AppAlertDialog
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.configs.AlertConfig
import jp.mihailskuzmins.sugoinihongoapp.helpers.rx.RxBus
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.AuthUtil
import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants
import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants.RxBusContracts
import jp.mihailskuzmins.sugoinihongoapp.resources.AuthResult
import jp.mihailskuzmins.sugoinihongoapp.userinterface.views.ButtonMessage
import javax.inject.Inject

class SettingsAuthChangePasswordViewModel(app: Application) :
	DetailViewModelBase(app), HasAuthAction, SupportsNavigationBack {

	@Inject lateinit var authUtil: AuthUtil

	private val mIsAuthInProgressConst = RxBusContracts.isAuthInProgress
	private val mNavBackSubject: Subject<Unit> = ReplaySubject.createWithSize(1)

	val currentPasswordItem = DetailItemEditTextPassword(app, R.string.auth_current_password)
		.apply {
			addRule(NotBlankRule(false))
			hasFocus = true
		}

	val newPasswordItem = DetailItemEditTextPassword(app, R.string.auth_new_password)
		.apply { addRules(NotBlankRule(false), MinLengthRule(AppConstants.Auth.minPasswordLength)) }

	val confirmNewPasswordItem = DetailItemEditTextPassword(app, R.string.auth_confirm_new_password,
			command = Command(::changePassword, EditorInfo.IME_ACTION_GO, ::isValid))
		.apply { addRule(NotBlankRule(false)) }

	override val authButton = ButtonMessage(app, R.string.settings_auth_change_password, ::changePassword)
	val changePasswordAlertDialog = AppAlertDialog()

	override var isAuth: Boolean = false
		private set

	init {
		newPasswordItem.addRule(EqualToFuncRule(confirmNewPasswordItem::value, R.string.auth_passwords_must_be_equal))

		newPasswordItem.valueChanged
			.skip(1)
			.invoke {
				confirmNewPasswordItem.triggerValidation(it)
					.addTo(cleanUp)
			}

		confirmNewPasswordItem.valueChanged
			.skip(1)
			.invoke {
				newPasswordItem.triggerValidation(it)
					.addTo(cleanUp)
			}

		createIsAuthDisposable()
			.addTo(cleanUp)

		RxBus.listen(Boolean::class.java, mIsAuthInProgressConst)
			.subscribe { isAuth = it }
			.addTo(cleanUp)
	}

	override val navBackObservable: Observable<Unit>
		get() = mNavBackSubject

	override fun initDetailItems(): Array<DetailItem> =
		arrayOf(currentPasswordItem, newPasswordItem, confirmNewPasswordItem)

	override fun initDetailItemValues(onInit: initAction) {
		onInit()
	}

	private fun changePassword() {
		RxBus.sentMessage(true, Boolean::class.java, mIsAuthInProgressConst)

		authUtil.changePassword(currentPasswordItem.value, newPasswordItem.value) {
			RxBus.sentMessage(false, Boolean::class.java, mIsAuthInProgressConst)

			if (it == AuthResult.Success) {
				mNavBackSubject.onNext(Unit)
			} else {
				publishErrorAuthResult(it)
			}
		}
	}

	private fun publishErrorAuthResult(authResult: AuthResult) {
		AlertConfig().apply {
			title = context.getString(R.string.general_attention)
			message = authResult.toMessage(context)
		}.invoke { changePasswordAlertDialog.config = it }
	}
}