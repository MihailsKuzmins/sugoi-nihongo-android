package jp.mihailskuzmins.sugoinihongoapp.features.settings


import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentSettingsUserInterfaceBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig

class SettingsUserInterfaceFragment : FragmentBase<SettingsUserInterfaceViewModel>(R.layout.fragment_settings_user_interface) {

	override fun getNavigationId() =
		R.id.settingsUserInterfaceFragment

	override fun createViewModel() =
		provideViewModel<SettingsUserInterfaceViewModel>()

	override fun bindView() {
		getBinding<FragmentSettingsUserInterfaceBinding>().viewModel = viewModel
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
			isOpenSettingsAvailable = false
		}
}
