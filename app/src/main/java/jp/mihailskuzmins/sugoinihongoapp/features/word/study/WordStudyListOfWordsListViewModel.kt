package jp.mihailskuzmins.sugoinihongoapp.features.word.study

import android.app.Application
import jp.mihailskuzmins.sugoinihongoapp.databinding.DialogWordStudyWordBinding
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ItemsAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.AppAlertViewDialog
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordStudyModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.study.WordStudyRepo
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.Keys
import javax.inject.Inject

class WordStudyListOfWordsListViewModel(app: Application) : ListViewModelBase<WordStudyModel>(app) {

	val wordDialog = AppAlertViewDialog<WordStudyModel, DialogWordStudyWordBinding>()

	@Inject lateinit var wordStudyRepo: WordStudyRepo
	@Inject lateinit var sharedPreferencesUtil: SharedPreferencesUtil

	override fun initItems(itemsAction: ItemsAction<WordStudyModel>) {
		val questionCount = sharedPreferencesUtil
			.getInt(Keys.wordQuizQuestionCount)
			.toLong()

		wordStudyRepo.getQuizListOfWords(questionCount) { itemsAction(it.shuffled()) }
	}
}