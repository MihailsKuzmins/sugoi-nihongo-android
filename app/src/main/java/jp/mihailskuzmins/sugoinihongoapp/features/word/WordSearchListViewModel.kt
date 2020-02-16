package jp.mihailskuzmins.sugoinihongoapp.features.word

import android.app.Application
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ItemsAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListSearchViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.models.word.WordSearchListModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.asset.KanaRepo
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.WordRepo
import javax.inject.Inject

class WordSearchListViewModel(app: Application) : ListSearchViewModelBase<WordSearchListModel>(app) {

	@Inject lateinit var wordRepo: WordRepo
	@Inject lateinit var kanaRepo: KanaRepo

	override fun initSearchItems(itemsAction: ItemsAction<WordSearchListModel>) {
		wordRepo.subscribeToCollection(false) {
			it.map { x ->
				val kanaString = if (x.transcription.isBlank()) x.translation
					else x.transcription

				val romaji = kanaRepo.kanaToRomaji(kanaString)

				WordSearchListModel(x, romaji)
			}.run(itemsAction)
		}.addTo(cleanUp)
	}

	override fun filterItem(item: WordSearchListModel, searchText: String) =
		item.original.contains(searchText, true) ||
				item.translation.contains(searchText, true) ||
				item.transcription.contains(searchText, true) ||
				item.romaji.contains(searchText, true)
}