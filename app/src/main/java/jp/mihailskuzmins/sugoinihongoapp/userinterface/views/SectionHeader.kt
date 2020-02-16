package jp.mihailskuzmins.sugoinihongoapp.userinterface.views

import android.content.Context
import androidx.annotation.StringRes

class SectionHeader(val label: String) {
	constructor(context: Context, @StringRes label: Int) : this(context.getString(label))
}