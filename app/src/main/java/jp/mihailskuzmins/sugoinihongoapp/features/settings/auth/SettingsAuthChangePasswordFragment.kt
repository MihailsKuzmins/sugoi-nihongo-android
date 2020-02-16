package jp.mihailskuzmins.sugoinihongoapp.features.settings.auth


import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentSettingsAuthChangePasswordBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNavigationBack
import jp.mihailskuzmins.sugoinihongoapp.resources.NavMode

class SettingsAuthChangePasswordFragment :
	FragmentBase<SettingsAuthChangePasswordViewModel>(R.layout.fragment_settings_auth_change_password) {

	override fun getNavigationId() =
		R.id.settingsAuthChangePasswordFragment

	override fun createViewModel() =
		provideViewModel<SettingsAuthChangePasswordViewModel>()

	override fun bindView() {
		getBinding<FragmentSettingsAuthChangePasswordBinding>().viewModel = viewModel
	}

	override fun beforeNavigatingBack() =
		!viewModel.isAuth

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		viewModel.changePasswordAlertDialog
			.subscribe(this)
			.addTo(d)

		subscribeToNavigationBack(NavMode.Main)
			.addTo(d)
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
			isOpenSettingsAvailable = false
		}
}
