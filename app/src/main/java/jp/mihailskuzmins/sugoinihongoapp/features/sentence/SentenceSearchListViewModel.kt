package jp.mihailskuzmins.sugoinihongoapp.features.sentence

import android.app.Application
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ItemsAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListSearchViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.sentence.SentenceListModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.SentenceRepo
import javax.inject.Inject

class SentenceSearchListViewModel(app: Application) : ListSearchViewModelBase<SentenceListModel>(app) {

	@Inject
	lateinit var sentenceRepo: SentenceRepo

	override fun initSearchItems(itemsAction: ItemsAction<SentenceListModel>) {
		sentenceRepo
			.subscribeToCollection(false, itemsAction)
			.addTo(cleanUp)
	}

	override fun filterItem(item: SentenceListModel, searchText: String) =
		item.original.contains(searchText, true)
}