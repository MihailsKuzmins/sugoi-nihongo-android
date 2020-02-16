package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems

import android.content.Context
import android.text.InputType
import androidx.annotation.StringRes
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.MaxLengthRule
import jp.mihailskuzmins.sugoinihongoapp.helpers.Box
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.BindableProp

class DetailItemEditText(
		context: Context,
		label: String,
		val inputType: Int = InputType.TYPE_CLASS_TEXT,
		maxLength: Int? = null) :
	DetailItemBase<String>(context, label, ""), DetailItemWithFocus {

	constructor(context: Context, @StringRes label: Int, inputType: Int = InputType.TYPE_CLASS_TEXT, maxLength: Int? = null) :
			this(context, context.getString(label), inputType, maxLength)

	var maxLength: Int? by BindableProp(BR.maxLength, null) { it: Int? ->
		removeRule(MaxLengthRule::class.java)
		it?.let { x -> this.addRule(MaxLengthRule(x)) }
	}
		@Bindable get
		private set

	init {
		isEnabledObservable
			.map {
				val length = if (it) maxLength else null
				Box(length)
			}
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { this.maxLength = it.value }
			.addTo(cleanUp)
	}

	override var hasFocus: Boolean = false
}