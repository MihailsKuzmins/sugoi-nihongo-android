package jp.mihailskuzmins.sugoinihongoapp.features.word.study

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ItemsAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.helpers.MainThreadLooper
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.BindableProp
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.room.WordQuizQuestionResultEntity
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.room.WordQuizRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordStudyResultFindRecogniseListViewModel(
		app: Application,
		private val mWordQuizId: Long) :
	ListViewModelBase<WordQuizQuestionResultEntity>(app) {

	@Inject
	lateinit var wordQuizRepo: WordQuizRepo

	var score by BindableProp(BR.score, 0)
		@Bindable get
		private set

	var mistakeCount by BindableProp(BR.mistakeCount, 0)
		@Bindable get
		private set

	override fun initItems(itemsAction: ItemsAction<WordQuizQuestionResultEntity>) {
		GlobalScope.launch(Dispatchers.Main) {
			val quiz = wordQuizRepo.selectQuiz(mWordQuizId)

			wordQuizRepo.getResults(mWordQuizId)
				.run(itemsAction)

			MainThreadLooper.runInMainThread {
				score = quiz.score
				mistakeCount = quiz.mistakeCount
			}
		}
	}
}