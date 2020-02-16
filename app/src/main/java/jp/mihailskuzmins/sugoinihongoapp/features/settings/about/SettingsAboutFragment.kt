package jp.mihailskuzmins.sugoinihongoapp.features.settings.about


import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentSettingsAboutBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNavigation
import jp.mihailskuzmins.sugoinihongoapp.resources.NavMode

class SettingsAboutFragment : FragmentBase<SettingsAboutViewModel>(R.layout.fragment_settings_about) {

	override fun getNavigationId() =
		R.id.settingsAboutFragment

	override fun createViewModel() =
		provideViewModel<SettingsAboutViewModel>()

	override fun bindView() {
		getBinding<FragmentSettingsAboutBinding>().viewModel = viewModel
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
