package jp.mihailskuzmins.sugoinihongoapp.features.settings.backup


import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentSettingsBackupBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNavigation
import jp.mihailskuzmins.sugoinihongoapp.resources.NavMode

class SettingsBackupFragment : FragmentBase<SettingsBackupViewModel>(R.layout.fragment_settings_backup) {

	override fun getNavigationId() =
		R.id.settingsBackupFragment

	override fun createViewModel() =
		provideViewModel<SettingsBackupViewModel>()

	override fun bindView() {
		getBinding<FragmentSettingsBackupBinding>().viewModel = viewModel
	}

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		subscribeToNavigation(NavMode.Main)
			.addTo(d)
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
			isOpenSettingsAvailable = false
		}
}
