package jp.mihailskuzmins.sugoinihongoapp.features.sentence

import android.app.Application
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ItemsAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.sentence.SentenceListModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.SentenceRepo
import javax.inject.Inject

class SentenceListViewModel(app: Application) : ListViewModelBase<SentenceListModel>(app) {

	@Inject
	lateinit var sentenceRepo: SentenceRepo

	override fun initItems(itemsAction: ItemsAction<SentenceListModel>) {
		sentenceRepo
			.subscribeToCollection(true, itemsAction)
			.addTo(cleanUp)
	}
}