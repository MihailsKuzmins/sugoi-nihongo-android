package jp.mihailskuzmins.sugoinihongoapp.features.settings.about

import android.app.Application
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ItemsAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.asset.ThirdPartyLibraryModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.asset.ThirdPartyLibraryRepo
import javax.inject.Inject

class SettingsThirdPartyLibraryListViewModel(app: Application) : ListViewModelBase<ThirdPartyLibraryModel>(app) {

	@Inject
	lateinit var thirdPartyLibraryRepo: ThirdPartyLibraryRepo

	override fun initItems(itemsAction: ItemsAction<ThirdPartyLibraryModel>) {
		val items = thirdPartyLibraryRepo.getThirdPartyLibraries()
		itemsAction(items)
	}
}