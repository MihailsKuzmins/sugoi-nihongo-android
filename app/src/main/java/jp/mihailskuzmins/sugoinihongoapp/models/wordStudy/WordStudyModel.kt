package jp.mihailskuzmins.sugoinihongoapp.models.wordStudy

import jp.mihailskuzmins.sugoinihongoapp.persistence.models.HasId
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.Word

class WordStudyModel(
	override val id: String,
	override val original: String,
	override val translation: String,
	override var transcription: String) : HasId, Word