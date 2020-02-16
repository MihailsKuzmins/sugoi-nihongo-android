package jp.mihailskuzmins.sugoinihongoapp.features.settings.about


import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListFragmentBase
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.asset.ThirdPartyLibraryModel

class SettingsThirdPartyLibraryListFragment :
	ListFragmentBase<ThirdPartyLibraryModel, SettingsThirdPartyLibraryListViewModel>(R.layout.fragment_settings_third_party_libraries_list) {

	@BindView(R.id.fragment_settings_third_party_library_list_recycler_view)
	override lateinit var recyclerView: RecyclerView

	override fun getNavigationId() =
		R.id.settingsThirdPartyLibraryListFragment

	override fun createViewModel() =
		provideViewModel<SettingsThirdPartyLibraryListViewModel>()

	override fun createRecyclerViewAdapter() =
		SettingsThirdPartyLibraryListAdapter()

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
			isOpenSettingsAvailable = false
		}
}
