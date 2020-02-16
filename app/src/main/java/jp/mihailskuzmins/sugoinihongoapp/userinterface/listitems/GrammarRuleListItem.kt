package jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems

import jp.mihailskuzmins.sugoinihongoapp.extensions.removeWhiteSpaces
import jp.mihailskuzmins.sugoinihongoapp.extensions.trimWithEllipsis
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.HasId
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.grammarrule.GrammarRuleListModel
import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants.Models

class GrammarRuleListItem(model: GrammarRuleListModel) : HasId {

	override val id: String = model.id
	val header = model.header
	val body = createBodyText(model.body)

	internal fun createBodyText(body: String) =
		body
			.trimWithEllipsis(Models.grammarRuleListItemBodyMaxLength)
			.removeWhiteSpaces()
}