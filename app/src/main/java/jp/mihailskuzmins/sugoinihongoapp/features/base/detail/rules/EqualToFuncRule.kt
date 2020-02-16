package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules

import androidx.annotation.StringRes

class EqualToFuncRule<T>(
    private val mValueFunc: () -> T,
    @StringRes errorMessage: Int,
    isVisible: Boolean = true) : RuleBase<T>(errorMessage, isVisible)
 where T: Any, T: Comparable<T> {

    override fun validate(value: T) =
        mValueFunc()
            .run { this == value }
}