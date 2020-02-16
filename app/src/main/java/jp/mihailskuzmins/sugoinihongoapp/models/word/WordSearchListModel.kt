package jp.mihailskuzmins.sugoinihongoapp.models.word

class WordSearchListModel(
	model: WordListModel,
	val romaji: String) : WordList {

	override val id = model.id
	override val original = model.original
	override val translation = model.translation
	override val transcription = model.transcription
	override val mark = model.mark
}