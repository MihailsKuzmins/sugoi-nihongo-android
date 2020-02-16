package jp.mihailskuzmins.sugoinihongoapp.features.settings.about


import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentSettingsAppSummaryBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig

class SettingsAppSummaryFragment : FragmentBase<SettingsAppSummaryViewModel>(R.layout.fragment_settings_app_summary) {

	override fun getNavigationId() =
		R.id.settingsAppSummaryFragment

	override fun createViewModel() =
		provideViewModel<SettingsAppSummaryViewModel>()

	override fun bindView() {
		getBinding<FragmentSettingsAppSummaryBinding>().viewModel = viewModel
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
			isOpenSettingsAvailable = false
		}
}
