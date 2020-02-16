package jp.mihailskuzmins.sugoinihongoapp.persistence.models.miscellaneous

interface WordMarkTableModel {

	val markCount: Int
}

class WordMarkTableRowModel(val mark: Int, override val markCount: Int) : WordMarkTableModel

class WordMarkTableTotalModel(override val markCount: Int) : WordMarkTableModel