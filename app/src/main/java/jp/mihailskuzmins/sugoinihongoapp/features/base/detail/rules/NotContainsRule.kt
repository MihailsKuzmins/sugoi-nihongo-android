package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules

import android.content.Context
import jp.mihailskuzmins.sugoinihongoapp.R

class NotContainsRule(
    private val mChar: Char,
    isVisible: Boolean = true) : RuleBase<String>(R.string.validation_invalid_character_encountered, isVisible) {

    override fun validate(value: String) =
        mChar !in value

    override fun getErrorMessage(context: Context): String =
        context.getString(errorMessage, mChar)
}