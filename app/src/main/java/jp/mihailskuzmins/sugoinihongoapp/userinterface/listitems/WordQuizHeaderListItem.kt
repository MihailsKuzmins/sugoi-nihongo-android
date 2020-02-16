package jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems

import jp.mihailskuzmins.sugoinihongoapp.extensions.formatDate
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordQuizListModel

class WordQuizHeaderListItem(model: WordQuizListModel): ListItemBase() {

	init {
		text = model.date.formatDate()
	}
}