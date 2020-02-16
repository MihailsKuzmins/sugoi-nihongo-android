package jp.mihailskuzmins.sugoinihongoapp.resources

object RoomConstants {

	const val wordQuizTable = "wordQuizTable"
	const val wordQuizQuestionResultTable = "wordQuizQuestionResult"

	const val wordQuizHistoryMaxCount = 30

	object WordQuiz {

		const val wordQuizId = "wordQuizId"
		const val score = "score"
		const val mistakeCount = "mistakeCount"
		const val date = "date"
		const val wordQuizType = "wordQuizType"
	}

	object WordQuizQuestionResult {

		const val wordId = "wordId"
		const val wordQuizId = "wordQuizId"
		const val questionText = "questionText"
		const val submittedText = "submittedText"
		const val isCorrect = "isCorrect"
	}
}