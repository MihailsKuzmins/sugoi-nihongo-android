package jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.grammarrule

import jp.mihailskuzmins.sugoinihongoapp.persistence.models.HasId

class GrammarRuleDetailModel(override val id: String) : HasId {

	var header: String = ""
	var body: String = ""
}