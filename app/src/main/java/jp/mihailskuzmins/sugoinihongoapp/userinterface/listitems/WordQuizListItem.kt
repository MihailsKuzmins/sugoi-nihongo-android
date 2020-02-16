package jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems

import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.formatDateTime
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordQuizListModel
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordQuizType

class WordQuizListItem(model: WordQuizListModel) {

	val id: Long = model.wordQuizId
	val score = model.score
	val mistakeCount = model.mistakeCount
	val time = model.date.formatDateTime("HH:mm:ss")

	val quizType = when (model.quizType) {
		WordQuizType.RecogniseWord -> R.string.word_quiz_recognise_word
		WordQuizType.FindWord -> R.string.word_quiz_find_word
	}
}