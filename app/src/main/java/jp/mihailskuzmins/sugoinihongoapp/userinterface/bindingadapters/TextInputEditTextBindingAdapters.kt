package jp.mihailskuzmins.sugoinihongoapp.userinterface.bindingadapters

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.showKeyboard
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemWithFocus
import jp.mihailskuzmins.sugoinihongoapp.helpers.Command
import jp.mihailskuzmins.sugoinihongoapp.userinterface.controls.CustomTextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@BindingAdapter("selectionAndFocusHandler")
fun setSelectionAndFocusHandler(editText: CustomTextInputEditText, detailItem: DetailItemWithFocus?) {
	if (detailItem == null)
		return

	editText.setOnSaveInstanceStateListener { hasFocus ->
		detailItem.hasFocus = hasFocus
	}

	editText.setOnAttachedToWindowListener {
		if (!detailItem.hasFocus)
			return@setOnAttachedToWindowListener

		// with the GlobalScope keyboard appears
		GlobalScope.launch(Dispatchers.Main) {
			editText.requestFocus()
			editText.setSelection(editText.length())
			editText.showKeyboard()
		}
	}
}

@BindingAdapter("android:imeOptions")
fun setImeOptions(editText: TextInputEditText, imeOptions: Int?) {
	imeOptions?.let {
		editText.imeOptions = it
	}
}

@BindingAdapter("imeCommand")
fun setImeCommand(editText: TextInputEditText, command: Command?) {
	command?.let {
		editText.setOnEditorActionListener { _, imeOptions, _ ->
			if (imeOptions != it.imeOptions)
				false
			else {
				val canExecute = it.canExecute()
				if (canExecute)
					it.execute()

				// True - don't hide the keyboard; False - hide the keyboard
				!canExecute
			}
		}
	}
}