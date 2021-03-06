package jp.mihailskuzmins.sugoinihongoapp.features.word.favourites

import android.app.Application
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ItemsAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.models.word.WordListModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.WordRepo
import javax.inject.Inject

class WordFavouritesListViewModel(app: Application) : ListViewModelBase<WordListModel>(app) {

	@Inject
	lateinit var wordRepo: WordRepo

	override fun initItems(itemsAction: ItemsAction<WordListModel>) {
		wordRepo
			.subscribeToFavourites(itemsAction)
			.addTo(cleanUp)
	}
}