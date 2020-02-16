package jp.mihailskuzmins.sugoinihongoapp.features.grammarrule

import android.app.Application
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ItemsAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListSearchViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.grammarrule.GrammarRuleListModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.GrammarRuleRepo
import javax.inject.Inject

class GrammarRuleSearchListViewModel(app: Application) : ListSearchViewModelBase<GrammarRuleListModel>(app) {

	@Inject
	lateinit var grammarRuleRepo: GrammarRuleRepo

	override fun initSearchItems(itemsAction: ItemsAction<GrammarRuleListModel>) {
		grammarRuleRepo
			.subscribeToCollection(false, itemsAction)
			.addTo(cleanUp)
	}

	override fun filterItem(item: GrammarRuleListModel, searchText: String) =
		item.header.contains(searchText, true)
}