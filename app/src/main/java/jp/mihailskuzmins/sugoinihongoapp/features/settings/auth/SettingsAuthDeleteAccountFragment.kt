package jp.mihailskuzmins.sugoinihongoapp.features.settings.auth


import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentSettingsAuthDeleteAccountBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.finishActivity
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig

class SettingsAuthDeleteAccountFragment :
	FragmentBase<SettingsAuthDeleteAccountViewModel>(R.layout.fragment_settings_auth_delete_account) {

	override fun getNavigationId() =
		R.id.settingsAuthDeleteAccountFragment

	override fun createViewModel() =
		provideViewModel<SettingsAuthDeleteAccountViewModel>()

	override fun bindView() {
		getBinding<FragmentSettingsAuthDeleteAccountBinding>().viewModel = viewModel
	}

	override fun beforeNavigatingBack() =
		!viewModel.isAuth

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		viewModel.deleteSuccessObservable
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { finishActivity() }
			.addTo(d)

		viewModel.deleteConfirmDialog
			.subscribe(this)
			.addTo(d)

		viewModel.deleteErrorAlertDialog
			.subscribe(this)
			.addTo(d)
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
			isOpenSettingsAvailable = false
		}
}
