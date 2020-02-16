package jp.mihailskuzmins.sugoinihongoapp.features.auth

import android.app.Application
import android.view.inputmethod.EditorInfo
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.activities.MainActivity
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.extensions.toTextRes
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItem
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemEditTextPassword
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.initAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.NotBlankRule
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasAuthAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.createIsAuthDisposable
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.startActivity
import jp.mihailskuzmins.sugoinihongoapp.helpers.Command
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.BindableProp
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.AppAlertDialog
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.configs.AlertConfig
import jp.mihailskuzmins.sugoinihongoapp.helpers.rx.RxBus
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.AuthUtil
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SecurePreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants.RxBusContracts
import jp.mihailskuzmins.sugoinihongoapp.resources.AuthResult
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.SecureKeys
import jp.mihailskuzmins.sugoinihongoapp.userinterface.views.ButtonMessage
import javax.inject.Inject

class AuthLoggedUserViewModel(app: Application, private val mErrorMessageCode: Int) : DetailViewModelBase(app), HasAuthAction {

	@Inject lateinit var authUtil: AuthUtil
	@Inject lateinit var securePreferencesUtil: SecurePreferencesUtil

	private val mSignInSuccessSubject: Subject<Unit> = ReplaySubject.createWithSize(1)

	val passwordItem = DetailItemEditTextPassword(app, R.string.auth_password,
			command = Command(::signIn, EditorInfo.IME_ACTION_GO, ::isValid))
		.apply { addRule(NotBlankRule(false)) }

	override val authButton = ButtonMessage(app, R.string.auth_sign_in, ::signIn)
	val messageAlertDialog = AppAlertDialog()

	override var isAuth by BindableProp(BR.auth, false)
		@Bindable get
		private set

	var email by BindableProp(BR.email, "")
		@Bindable get
		private set

	init {
		email = securePreferencesUtil.getValue(SecureKeys.currentEmailKey)

		createIsAuthDisposable()
			.addTo(cleanUp)

		RxBus.listen(Boolean::class.java, RxBusContracts.isAuthInProgress)
			.subscribe { isAuth = it }
			.addTo(cleanUp)
	}

	val signInSuccessObservable: Observable<Unit>
		get() = mSignInSuccessSubject

	override fun initDetailItems(): Array<DetailItem> =
		arrayOf(passwordItem)

	override fun initDetailItemValues(onInit: initAction) {
		val defaultErrorCode = context.resources.getInteger(R.integer.default_error_code)

		if (defaultErrorCode != mErrorMessageCode) {
			authButton.message = context.getString(mErrorMessageCode)
			authButton.isMessageVisible = true
		}

		onInit()
	}

	fun sendPasswordReset() {
		if (email.isBlank())
			return

		RxBus.sentMessage(true, Boolean::class.java, RxBusContracts.isAuthInProgress)

		authUtil.sendPasswordReset(email) {
			val message = if (it == AuthResult.Success) context.getString(R.string.auth_password_reset_has_been_sent, email)
				else context.getString(it.toTextRes())

			publishAlertMessage(message)
			RxBus.sentMessage(false, Boolean::class.java, RxBusContracts.isAuthInProgress)
		}
	}

	fun resentEmailVerification() {
		if (email.isBlank())
			return

		RxBus.sentMessage(true, Boolean::class.java, RxBusContracts.isAuthInProgress)

		authUtil.sendEmailVerification {
			val message = if (it == AuthResult.Success) context.getString(R.string.auth_verification_has_been_sent, email)
				else context.getString(it.toTextRes())

			publishAlertMessage(message)
			RxBus.sentMessage(false, Boolean::class.java, RxBusContracts.isAuthInProgress)
		}
	}

	private fun signIn() {
		if (email.isEmpty())
			return

		authButton.messageWithVisibility = ""
		RxBus.sentMessage(true, Boolean::class.java, RxBusContracts.isAuthInProgress)

		authUtil.signIn(email, passwordItem.value) {
			if (it == AuthResult.Success) {
				startActivity(MainActivity::class.java)
				mSignInSuccessSubject.onNext(Unit)
			} else {
				authButton.message = it.toTextRes().run(context::getString)
				authButton.isMessageVisible = true
			}

			RxBus.sentMessage(false, Boolean::class.java, RxBusContracts.isAuthInProgress)
		}
	}

	private fun publishAlertMessage(message: String) =
		AlertConfig()
			.apply { this.message = message }
			.invoke { x-> messageAlertDialog.config = x }
}