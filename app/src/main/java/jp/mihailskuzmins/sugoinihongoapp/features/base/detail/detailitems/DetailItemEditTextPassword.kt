package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems

import android.content.Context
import androidx.annotation.StringRes
import jp.mihailskuzmins.sugoinihongoapp.helpers.Command

class DetailItemEditTextPassword(
        context: Context,
        @StringRes label: Int,
        val command: Command? = null) :
    DetailItemBase<String>(context, label, ""), DetailItemWithFocus {

    val imeOptions: Int? = command?.imeOptions
    override var hasFocus: Boolean = false
}