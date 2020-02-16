package jp.mihailskuzmins.sugoinihongoapp.userinterface.bindingadapters

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.browse
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke

@BindingAdapter("underlinedText")
fun setUnderlinedText(textView: TextView, text: String?) {
	text?.let {
		textView.text = SpannableString(it.trim()).apply {
			setSpan(UnderlineSpan(), 0, it.length, 0)
		}
	}
}

@BindingAdapter("browseUrl")
fun setBrowseUrlOnClickListener(textView: TextView, url: String?) {
	url?.let {
		textView.setOnClickListener { x -> x.context.browse(it)}
	}
}

@BindingAdapter("android:textSize")
fun setTextSize(textView: TextView, textSize: Float?) {
	val size = textSize ?: TextView(textView.context).textSize

	textSize?.let {
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
	}
}

@BindingAdapter("android:textColor")
fun setTextColor(textView: TextView, @ColorRes colorRes: Int?) {
	ContextCompat
		.getColor(textView.context, colorRes ?: R.color.colorDetailText)
		.invoke(textView::setTextColor)
}