package jp.mihailskuzmins.sugoinihongoapp.features.word.study

import android.app.Application
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ItemsAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordQuizListModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.room.WordQuizRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordStudyHistoryFindRecogniseListViewModel(app: Application) : ListViewModelBase<WordQuizListModel>(app) {

	@Inject
	lateinit var wordQuizRepo: WordQuizRepo

	override fun initItems(itemsAction: ItemsAction<WordQuizListModel>) {
		GlobalScope.launch {
			wordQuizRepo.selectLatestQuizzes()
				.map {
					WordQuizListModel(
						it.wordQuizId!!,
						it.wordQuizType,
						it.score,
						it.mistakeCount,
						it.date
					)
				}
				.sortedByDescending(WordQuizListModel::date)
				.invoke(itemsAction)
		}
	}
}