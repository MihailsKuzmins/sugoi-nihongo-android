package jp.mihailskuzmins.sugoinihongoapp.models.word

import jp.mihailskuzmins.sugoinihongoapp.persistence.models.HasId
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.Word

interface WordList : Word, HasId {

	val mark: Int
}