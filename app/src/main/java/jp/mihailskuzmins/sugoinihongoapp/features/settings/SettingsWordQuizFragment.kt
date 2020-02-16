package jp.mihailskuzmins.sugoinihongoapp.features.settings

import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentSettingsWordQuizBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig

class SettingsWordQuizFragment : FragmentBase<SettingsWordQuizViewModel>(R.layout.fragment_settings_word_quiz) {

	override fun getNavigationId() =
		R.id.settingsWordQuizFragment

	override fun createViewModel() =
		provideViewModel<SettingsWordQuizViewModel>()

	override fun bindView() {
		getBinding<FragmentSettingsWordQuizBinding>().viewModel = viewModel
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
			isOpenSettingsAvailable = false
		}
}
