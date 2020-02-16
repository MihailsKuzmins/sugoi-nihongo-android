package jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems

import jp.mihailskuzmins.sugoinihongoapp.persistence.models.HasId
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.sentence.SentenceListModel

class SentenceListItem(model: SentenceListModel) : HasId {

	override val id: String = model.id
	val original: String = model.original
	val translation: String = model.translation
}