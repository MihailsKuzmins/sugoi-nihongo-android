package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules

import android.content.Context
import jp.mihailskuzmins.sugoinihongoapp.R

class MinLengthRule(
    private val mMinLength: Int,
    isVisible: Boolean = true) : RuleBase<String>(R.string.validation_min_length, isVisible) {

    init {
        check(mMinLength > 0) { "Minimum length must be greater than zero" }
    }

    override fun validate(value: String) =
        value.length >= mMinLength

    override fun getErrorMessage(context: Context): String =
        context.getString(errorMessage, mMinLength)
}