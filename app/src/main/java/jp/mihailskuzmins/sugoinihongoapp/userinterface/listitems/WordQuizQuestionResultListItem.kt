package jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems

import android.content.Context
import androidx.annotation.DrawableRes
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.applyIf
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.room.WordQuizQuestionResultEntity

class WordQuizQuestionResultListItem(
	context: Context,
	model: WordQuizQuestionResultEntity
) : ListItemBase() {

	@DrawableRes
	val image: Int = if (model.isCorrect) R.drawable.ic_done_green_24dp
		else R.drawable.ic_clear_red_24dp

	init {
		text = createText(context, model)
	}

	private fun createText(context: Context, model: WordQuizQuestionResultEntity): String =
		StringBuilder(model.questionText)
			.applyIf(!model.isCorrect) { appendln()
				.append(context.getString(R.string.general_submitted, model.submittedText))
			}.toString()
}