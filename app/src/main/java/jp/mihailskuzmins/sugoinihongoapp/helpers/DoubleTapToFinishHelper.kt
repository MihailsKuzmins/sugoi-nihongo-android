package jp.mihailskuzmins.sugoinihongoapp.helpers

import android.content.Context
import android.widget.Toast
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.alsoIfFalse
import jp.mihailskuzmins.sugoinihongoapp.extensions.alsoIfTrue
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.makeToast
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants
import java.util.*
import kotlin.math.absoluteValue

class DoubleTapToFinishHelper(context: Context) {

	private var mLatestBackButtonPressedDate = FirestoreConstants.DefaultValues.date

	private val toast: Toast by lazy {
		R.string.general_tap_twice_to_exit
			.run { context.makeToast(this) }
	}

	fun canNavigateBack() = (Date().time - mLatestBackButtonPressedDate.time)
		.run { absoluteValue <= 1000L }
		.also { mLatestBackButtonPressedDate = Date() }
		.alsoIfTrue(toast::cancel)
		.alsoIfFalse(toast::show)
}