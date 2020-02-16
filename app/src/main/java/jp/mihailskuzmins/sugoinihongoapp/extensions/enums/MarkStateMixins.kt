package jp.mihailskuzmins.sugoinihongoapp.extensions.enums

import androidx.annotation.ColorRes
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.word.WordMarkModel.MarkState

@ColorRes
fun MarkState.toColor() =
	when (this) {
		MarkState.Excellent -> R.color.colorMarkExcellent
		MarkState.Good -> R.color.colorMarkGood
		MarkState.Average -> R.color.colorMarkAverage
		MarkState.Bad -> R.color.colorMarkBad
		MarkState.Excluded -> R.color.colorMarkExcluded
	}