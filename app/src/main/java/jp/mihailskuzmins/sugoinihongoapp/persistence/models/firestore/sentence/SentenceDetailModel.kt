package jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.sentence

import jp.mihailskuzmins.sugoinihongoapp.persistence.models.HasId

class SentenceDetailModel(override val id: String) : HasId {

	var original: String = ""
	var translation: String = ""
	var transcription: String = ""
}