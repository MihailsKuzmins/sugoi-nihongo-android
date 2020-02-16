package jp.mihailskuzmins.sugoinihongoapp.userinterface.grammarRuleListItemTests

import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.grammarrule.GrammarRuleListModel
import jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems.GrammarRuleListItem

abstract class GrammarRuleListItemTestsBase {

	protected fun createClass() =
		GrammarRuleListItem(GrammarRuleListModel(""))
}