package jp.mihailskuzmins.sugoinihongoapp.features.settings


import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentSettingsMainBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNavigation
import jp.mihailskuzmins.sugoinihongoapp.resources.NavMode

class SettingsMainFragment : FragmentBase<SettingsMainViewModel>(R.layout.fragment_settings_main) {

	override fun getNavigationId() =
		R.id.settingsMainFragment

	override fun createViewModel() =
		provideViewModel<SettingsMainViewModel>()

	override fun bindView() {
		getBinding<FragmentSettingsMainBinding>().viewModel = viewModel
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
