package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems

import android.content.Context
import androidx.annotation.StringRes

class DetailItemSwitch(
		context: Context,
		@StringRes primaryLabel: Int,
		@StringRes secondaryLabel: Int) :
	DetailItemBase<Boolean>(context, primaryLabel, false) {

	val secondaryLabel: String = context.getString(secondaryLabel)

	fun toggle() {
		value = !value
	}
}