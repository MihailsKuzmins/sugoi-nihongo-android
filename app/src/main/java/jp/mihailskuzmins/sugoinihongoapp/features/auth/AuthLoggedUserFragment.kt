package jp.mihailskuzmins.sugoinihongoapp.features.auth


import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentAuthLoggedUserBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.application
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.finishActivity
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig

class AuthLoggedUserFragment : FragmentBase<AuthLoggedUserViewModel>(R.layout.fragment_auth_logged_user) {

	override fun getNavigationId() =
		R.id.authLoggedUserFragment

	override fun createViewModel() =
		provideViewModel {
			val errorMessageCode = AuthLoggedUserFragmentArgs
				.fromBundle(arguments!!)
				.errorMessageCode

			AuthLoggedUserViewModel(application, errorMessageCode)
		}

	override fun bindView() {
		getBinding<FragmentAuthLoggedUserBinding>().viewModel = viewModel
	}

	override fun beforeNavigatingBack() =
		!viewModel.isAuth

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		viewModel.signInSuccessObservable
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { finishActivity() }
			.addTo(d)

		viewModel.messageAlertDialog
			.subscribe(this)
			.addTo(d)
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
		}
}
