package jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems

import androidx.annotation.ColorRes
import jp.mihailskuzmins.sugoinihongoapp.extensions.enums.toColor
import jp.mihailskuzmins.sugoinihongoapp.models.word.WordList
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.HasId
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.createJapaneseTextWithBrackets
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.word.getMarkState

class WordListItem(model: WordList) : HasId {

	override val id = model.id
	val original = model.original
	val translation = model.createJapaneseTextWithBrackets()
	@ColorRes val markStateColor = model.mark.getMarkState().toColor()
}