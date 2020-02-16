package jp.mihailskuzmins.sugoinihongoapp.models.wordStudy

class WordQuizQuestionModel(word: WordStudyModel, val mark: Int, options: List<WordStudyModel>) {

	val wordId = word.id
	val word = word.toOptionModel(true)

	val options: List<WordQuizQuestionOptionModel> = options
		.map { it.toOptionModel(false) }
		.toMutableList()
		.apply { this@apply.add(this@WordQuizQuestionModel.word) }
		.shuffled()
}

private fun WordStudyModel.toOptionModel(isCorrect: Boolean) =
	WordQuizQuestionOptionModel(
		original,
		translation,
		transcription,
		isCorrect
	)