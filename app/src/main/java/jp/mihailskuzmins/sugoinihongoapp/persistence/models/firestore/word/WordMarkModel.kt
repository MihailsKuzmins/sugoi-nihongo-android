package jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.word

import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants.Models
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.DefaultValues

class WordMarkModel {

	var mark = DefaultValues.int
	var isStudiable = DefaultValues.isStudiable

	enum class MarkState {

		Excellent,
		Good,
		Average,
		Bad,
		Excluded
	}
}

fun Int.getMarkState() =
	when (this) {
		in Models.wordMarkExcellent..Models.wordMarkMax -> WordMarkModel.MarkState.Excellent
		in Models.wordMarkGood until Models.wordMarkExcellent -> WordMarkModel.MarkState.Good
		in Models.wordMarkAverage until Models.wordMarkGood -> WordMarkModel.MarkState.Average
		in Models.wordMarkMin until Models.wordMarkAverage -> WordMarkModel.MarkState.Bad
		Models.nonStudiableMark -> WordMarkModel.MarkState.Excluded
		else -> throw IllegalArgumentException("Mark is out of range")
	}