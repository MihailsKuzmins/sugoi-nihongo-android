package jp.mihailskuzmins.sugoinihongoapp.features.grammarrule

import android.app.Application
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ItemsAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.grammarrule.GrammarRuleListModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.GrammarRuleRepo
import javax.inject.Inject

class GrammarRuleListViewModel(app: Application): ListViewModelBase<GrammarRuleListModel>(app) {

	@Inject lateinit var grammarRuleRepo: GrammarRuleRepo

	override fun initItems(itemsAction: ItemsAction<GrammarRuleListModel>) {
		grammarRuleRepo
			.subscribeToCollection(true, itemsAction)
			.addTo(cleanUp)
	}
}