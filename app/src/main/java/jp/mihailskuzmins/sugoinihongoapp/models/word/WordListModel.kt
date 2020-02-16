package jp.mihailskuzmins.sugoinihongoapp.models.word

import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants.Models

class WordListModel(
	override val id: String,
	override val original: String,
	override val translation: String,
	override val transcription: String,
	mark: Int,
	isStudiable: Boolean) : WordList {

	override val mark = if (isStudiable) mark
		else Models.nonStudiableMark
}