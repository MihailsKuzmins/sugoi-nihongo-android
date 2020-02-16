package jp.mihailskuzmins.sugoinihongoapp.models.wordStudy

import java.util.*

class WordQuizListModel(
	val wordQuizId: Long,
	val quizType: WordQuizType,
	val score: Int,
	val mistakeCount: Int,
	val date: Date
)