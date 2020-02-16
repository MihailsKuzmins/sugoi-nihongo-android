package jp.mihailskuzmins.sugoinihongoapp.features.auth

import android.app.Application
import android.text.InputType
import android.view.inputmethod.EditorInfo
import androidx.navigation.NavDirections
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.activities.MainActivity
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.extensions.toTextRes
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItem
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemEditText
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemEditTextPassword
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.initAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.NotBlankRule
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasAuthAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsNavDirections
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.createIsAuthDisposable
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.startActivity
import jp.mihailskuzmins.sugoinihongoapp.helpers.Command
import jp.mihailskuzmins.sugoinihongoapp.helpers.rx.RxBus
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.AuthUtil
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SecurePreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants.RxBusContracts
import jp.mihailskuzmins.sugoinihongoapp.resources.AuthResult
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.SecureKeys
import jp.mihailskuzmins.sugoinihongoapp.userinterface.views.ButtonMessage
import javax.inject.Inject

class AuthSignInViewModel(app: Application) : DetailViewModelBase(app), HasAuthAction, SupportsNavDirections {

    @Inject lateinit var authUtil: AuthUtil
    @Inject lateinit var securePreferencesUtil: SecurePreferencesUtil

    private val mSignInSuccessSubject: Subject<Unit> = ReplaySubject.createWithSize(1)
    private val mNavDirectionsSubject: Subject<NavDirections> = PublishSubject.create()

    val emailItem = DetailItemEditText(app, R.string.auth_email, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        .apply { addRule(NotBlankRule(false)) }

    val passwordItem = DetailItemEditTextPassword(app, R.string.auth_password,
            command = Command(::signIn, EditorInfo.IME_ACTION_GO, ::isValid))
        .apply { addRule(NotBlankRule(false)) }

    override val authButton = ButtonMessage(app, R.string.auth_sign_in, ::signIn)

    override var isAuth: Boolean = false
        private set

    init {
        createIsAuthDisposable()
            .addTo(cleanUp)

        RxBus.listen(Boolean::class.java, RxBusContracts.isAuthInProgress)
            .subscribe { isAuth = it }
            .addTo(cleanUp)
    }

    val signInSuccessObservable: Observable<Unit>
        get() = mSignInSuccessSubject

    override val navDirectionsObservable: Observable<NavDirections>
        get() = mNavDirectionsSubject.distinctUntilChanged()

    override fun initDetailItems(): Array<DetailItem> =
        arrayOf(emailItem, passwordItem)

    override fun initDetailItemValues(onInit: initAction) {
        emailItem.value = securePreferencesUtil.getValue(SecureKeys.currentEmailKey)
        onInit()
    }

    fun setLastKnownEmail() {
        if (emailItem.value.isBlank()) {
            emailItem.value = securePreferencesUtil.getValue(SecureKeys.currentEmailKey)
        }
    }

    private fun signIn() {
        RxBus.sentMessage(true, Boolean::class.java, RxBusContracts.isAuthInProgress)
        val email = emailItem.value

        authUtil.signIn(email, passwordItem.value) {
            RxBus.sentMessage(false, Boolean::class.java, RxBusContracts.isAuthInProgress)

            when (it) {
                AuthResult.Success -> {
                    securePreferencesUtil.setValue(SecureKeys.currentEmailKey, email)

                    startActivity(MainActivity::class.java)
                    mSignInSuccessSubject.onNext(Unit)
                }
                AuthResult.WrongPassword, AuthResult.EmailNotVerified -> {
                    securePreferencesUtil.setValue(SecureKeys.currentEmailKey, email)

                    getLoggedUserNavDirections(it)
                        .invoke(mNavDirectionsSubject::onNext)
                }
                else -> {
                    authButton.message = getErrorMessage(it)
                    authButton.isMessageVisible = true
                }
            }
        }
    }

    private fun getLoggedUserNavDirections(authResult: AuthResult) =
        authResult
            .toTextRes()
            .run { AuthSignInFragmentDirections.authSignInToAuthLoggedUser(this) }

    private fun getErrorMessage(authResult: AuthResult): String =
        authResult
            .toTextRes()
            .run(context::getString)
}