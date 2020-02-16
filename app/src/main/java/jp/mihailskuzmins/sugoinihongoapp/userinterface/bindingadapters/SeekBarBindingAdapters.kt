package jp.mihailskuzmins.sugoinihongoapp.userinterface.bindingadapters

import android.widget.SeekBar
import androidx.databinding.BindingAdapter
import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants

@BindingAdapter("isSeekBarEnabled")
fun setSeekBarEnabled(seekBar: SeekBar, isSeekBarEnabled: Boolean?) {
	val isEnabled = isSeekBarEnabled ?: true
	val alpha = if (isEnabled) 1F
		else AppConstants.disabledAlpha

	seekBar.apply {
		this.isEnabled = isEnabled
		this.alpha = alpha
	}
}