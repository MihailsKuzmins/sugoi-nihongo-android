package jp.mihailskuzmins.sugoinihongoapp.features.auth


import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentAuthSignUpBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNavigation
import jp.mihailskuzmins.sugoinihongoapp.resources.NavMode

class AuthSignUpFragment : FragmentBase<AuthSignUpViewModel>(R.layout.fragment_auth_sign_up) {

    override fun getNavigationId() =
        R.id.authSignUpFragment

    override fun createViewModel() =
        provideViewModel<AuthSignUpViewModel>()

    override fun bindView() {
        getBinding<FragmentAuthSignUpBinding>().viewModel = viewModel
    }

    override fun beforeNavigatingBack() =
        !viewModel.isAuth

    override fun initSubscriptions(d: CompositeDisposable) {
        super.initSubscriptions(d)

        viewModel.signUpResultAlertDialog
            .subscribe(this)
            .addTo(d)

        subscribeToNavigation(NavMode.SignInSignUp, R.id.authSignInFragment)
            .addTo(d)
    }
}
