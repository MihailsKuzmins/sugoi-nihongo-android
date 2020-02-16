package jp.mihailskuzmins.sugoinihongoapp.userinterface.views

import android.content.Context
import androidx.annotation.StringRes
import androidx.databinding.Bindable
import androidx.databinding.PropertyChangeRegistry
import androidx.databinding.library.baseAdapters.BR
import jp.mihailskuzmins.sugoinihongoapp.helpers.BaseObservable
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.BindableProp
import jp.mihailskuzmins.sugoinihongoapp.resources.Action

class ButtonMessage(
		context: Context,
		@StringRes label: Int,
		val action: Action,
		@StringRes message: Int? = null) :
	BaseObservable {

	override var callbacks: PropertyChangeRegistry? = null
	val label: String = context.getString(label)

	var messageWithVisibility: String
		get() = message
		set(value) {
			message = value
			isMessageVisible = value.isNotBlank()
		}

	var message: String by BindableProp(BR.message, if (message != null) context.getString(message) else "")
		@Bindable get

	var isMessageVisible by BindableProp(BR.messageVisible, false)
		@Bindable get

	var isEnabled by BindableProp(BR.enabled, true)
		@Bindable get
}