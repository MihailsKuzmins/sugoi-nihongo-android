package jp.mihailskuzmins.sugoinihongoapp.features.settings.backup


import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasNoResultsTextView
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListFragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNoResultsTextView
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.miscellaneous.SettingsLocalBackupListModel

class SettingsLocalBackupListFragment :
	ListFragmentBase<SettingsLocalBackupListModel, SettingsLocalBackupListViewModel>(R.layout.fragment_settings_local_backup_list),
	HasNoResultsTextView {

	@BindView(R.id.fragment_settings_local_backup_list_recycler_view)
	override lateinit var recyclerView: RecyclerView

	@BindView(R.id.fragment_settings_local_backup_list_no_results_text_view)
	override lateinit var noResultsTextView: TextView

	override fun getNavigationId() =
		R.id.settingsLocalBackupListFragment

	override fun createViewModel() =
		provideViewModel<SettingsLocalBackupListViewModel>()

	override fun createRecyclerViewAdapter() =
		SettingsLocalBackupListAdapter(context!!)

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		subscribeToNoResultsTextView()
			.addTo(d)
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
			isOpenSettingsAvailable = false
		}
}
