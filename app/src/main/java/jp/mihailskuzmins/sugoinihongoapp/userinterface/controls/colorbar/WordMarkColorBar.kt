package jp.mihailskuzmins.sugoinihongoapp.userinterface.controls.colorbar

import android.content.Context
import android.util.AttributeSet
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.word.WordMarkModel

class WordMarkColorBar : ColorBarBase<WordMarkModel.MarkState> {

	constructor(context: Context) : super(context)
	constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
	constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)
}