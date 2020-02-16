package jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.word

import jp.mihailskuzmins.sugoinihongoapp.persistence.models.HasId
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.DefaultValues
import java.util.*

class WordDetailModel(override val id: String) : HasId {

	var original: String = ""
	var translation: String = ""
	var transcription: String = ""
	var notes: String = ""

	var dateLastAccessed: Date = DefaultValues.date
	var isFavourite: Boolean? = null
	var isStudiable: Boolean = false
	var mark: Int = DefaultValues.int
	var timesAccessed: Int? = null
	var timesCorrect: Int? = null
}