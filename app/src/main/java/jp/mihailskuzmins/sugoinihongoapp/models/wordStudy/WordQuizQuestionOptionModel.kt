package jp.mihailskuzmins.sugoinihongoapp.models.wordStudy

import jp.mihailskuzmins.sugoinihongoapp.persistence.models.Word

class WordQuizQuestionOptionModel(
	override val original: String,
	override val translation: String,
	override val transcription: String,
	val isCorrect: Boolean
) : Word