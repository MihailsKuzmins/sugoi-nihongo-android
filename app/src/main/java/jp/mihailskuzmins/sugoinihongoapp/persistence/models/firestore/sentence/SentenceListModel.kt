package jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.sentence

import jp.mihailskuzmins.sugoinihongoapp.persistence.models.HasId

open class SentenceListModel(override val id: String) : HasId {

	var original = ""
	var translation = ""
}