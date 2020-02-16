package jp.mihailskuzmins.sugoinihongoapp.features.auth

import android.app.Application
import android.text.InputType
import android.view.inputmethod.EditorInfo
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.extensions.toMessage
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItem
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemEditText
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemEditTextPassword
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.initAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.EqualToFuncRule
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.MinLengthRule
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.NotBlankRule
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasAuthAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsNavigation
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.createIsAuthDisposable
import jp.mihailskuzmins.sugoinihongoapp.helpers.Command
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.AppAlertDialog
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.configs.AlertConfig
import jp.mihailskuzmins.sugoinihongoapp.helpers.rx.RxBus
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.AuthUtil
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SecurePreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants
import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants.RxBusContracts
import jp.mihailskuzmins.sugoinihongoapp.resources.AuthResult
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.SecureKeys
import jp.mihailskuzmins.sugoinihongoapp.userinterface.views.ButtonMessage
import javax.inject.Inject

class AuthSignUpViewModel(app: Application) :
    DetailViewModelBase(app), HasAuthAction, SupportsNavigation {

    @Inject lateinit var authUtil: AuthUtil
    @Inject lateinit var securePreferencesUtil: SecurePreferencesUtil

    private val mNavActionIdSubject: Subject<Int> = PublishSubject.create()

    val emailItem = DetailItemEditText(app, R.string.auth_email, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        .apply { addRule(NotBlankRule(false)) }

    val passwordItem = DetailItemEditTextPassword(app, R.string.auth_password)
        .apply { addRules(NotBlankRule(false), MinLengthRule(AppConstants.Auth.minPasswordLength)) }

    val confirmPasswordItem = DetailItemEditTextPassword(app, R.string.auth_confirm_password,
            command = Command(::signUp, EditorInfo.IME_ACTION_GO, ::isValid))
        .apply { addRule(NotBlankRule(false)) }

    override val authButton = ButtonMessage(app, R.string.auth_sign_up, ::signUp)
    val signUpResultAlertDialog = AppAlertDialog()

    override var isAuth: Boolean = false
        private set

    init {
        passwordItem.addRule(EqualToFuncRule(confirmPasswordItem::value, R.string.auth_passwords_must_be_equal))

        // Skip initial value
        passwordItem.valueChanged
            .skip(1)
            .invoke {
                confirmPasswordItem.triggerValidation(it)
                    .addTo(cleanUp)
            }

        confirmPasswordItem.valueChanged
            .skip(1)
            .invoke {
                passwordItem.triggerValidation(it)
                    .addTo(cleanUp)
            }

        createIsAuthDisposable()
            .addTo(cleanUp)

        RxBus.listen(Boolean::class.java, RxBusContracts.isAuthInProgress)
            .subscribe { isAuth = it }
            .addTo(cleanUp)
    }

    override val navActionIdObservable: Observable<Int>
        get() = mNavActionIdSubject.distinctUntilChanged()

    override fun initDetailItems(): Array<DetailItem> =
        arrayOf(emailItem, passwordItem, confirmPasswordItem)

    override fun initDetailItemValues(onInit: initAction) {
        onInit()
    }

    private fun signUp() {
        RxBus.sentMessage(true, Boolean::class.java, RxBusContracts.isAuthInProgress)
        val email = emailItem.value

        authUtil.signUp(email, passwordItem.value) {
            if (it == AuthResult.Success) {
                securePreferencesUtil.setValue(SecureKeys.currentEmailKey, email)
                publishSignUpResult(email)
            } else {
                authButton.messageWithVisibility = it.toMessage(context)
            }

            RxBus.sentMessage(false, Boolean::class.java, RxBusContracts.isAuthInProgress)
        }
    }

    private fun publishSignUpResult(email: String) {
        AlertConfig().apply {
            title = context.getString(R.string.auth_sign_up_successful)
            message = context.getString(R.string.auth_verification_has_been_sent, email)
            okAction = { mNavActionIdSubject.onNext(R.id.authSignUp_to_authLoggedUser) }
            isCancellable = false
        }.invoke { signUpResultAlertDialog.config = it }
    }
}