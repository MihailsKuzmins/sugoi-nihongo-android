package jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.grammarrule

import jp.mihailskuzmins.sugoinihongoapp.persistence.models.HasId

class GrammarRuleListModel(override val id: String) : HasId {

	var header: String = ""
	var body: String = ""
}