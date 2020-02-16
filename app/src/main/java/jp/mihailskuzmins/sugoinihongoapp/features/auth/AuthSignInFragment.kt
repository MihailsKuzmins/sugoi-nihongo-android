package jp.mihailskuzmins.sugoinihongoapp.features.auth


import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentAuthSignInBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.finishActivity
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNavDirections
import jp.mihailskuzmins.sugoinihongoapp.resources.NavMode

class AuthSignInFragment : FragmentBase<AuthSignInViewModel>(R.layout.fragment_auth_sign_in) {

    override fun onResume() {
        viewModel.setLastKnownEmail()
        super.onResume()
    }

    override fun getNavigationId() =
        R.id.authSignInFragment

    override fun createViewModel() =
        provideViewModel<AuthSignInViewModel>()

    override fun bindView() {
        getBinding<FragmentAuthSignInBinding>().viewModel = viewModel
    }

    override fun beforeNavigatingBack() =
        !viewModel.isAuth

    override fun initSubscriptions(d: CompositeDisposable) {
        super.initSubscriptions(d)

        viewModel.signInSuccessObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { finishActivity() }
            .addTo(d)

        subscribeToNavDirections(NavMode.SignInSignUp)
            .addTo(d)
    }
}
