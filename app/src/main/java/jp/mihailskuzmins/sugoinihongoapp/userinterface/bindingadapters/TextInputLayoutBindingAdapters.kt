package jp.mihailskuzmins.sugoinihongoapp.userinterface.bindingadapters

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import jp.mihailskuzmins.sugoinihongoapp.extensions.applyIf
import jp.mihailskuzmins.sugoinihongoapp.extensions.hasValue

@BindingAdapter("errorText")
fun setErrorText(inputLayout: TextInputLayout, validationMessage: String?) {
	val isErrorEnabled = !validationMessage.isNullOrBlank()
	inputLayout
		.apply { this.isErrorEnabled = isErrorEnabled }
		.applyIf(isErrorEnabled) { error = validationMessage }
}

@BindingAdapter("counterMaxLength")
fun setCounterMaxLength(inputLayout: TextInputLayout, maxLength: Int?) {
	inputLayout.isCounterEnabled = maxLength.hasValue
	maxLength?.let { inputLayout.counterMaxLength = it }

	// Prevent default red highlight (does not disappear when disabled)
	// It's already ensured by Rules
	inputLayout.setCounterOverflowTextAppearance(-1)
}

@BindingAdapter("endIconModePasswordToggle")
fun setEndIconModePasswordToggle(inputLayout: TextInputLayout, isEnabled: Boolean?) {
    inputLayout.endIconMode = if (isEnabled == true) TextInputLayout.END_ICON_PASSWORD_TOGGLE
		else TextInputLayout.END_ICON_NONE
}