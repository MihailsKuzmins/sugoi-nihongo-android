package jp.mihailskuzmins.sugoinihongoapp.userinterface.controls

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import jp.mihailskuzmins.sugoinihongoapp.helpers.MainThreadLooper
import jp.mihailskuzmins.sugoinihongoapp.resources.OnAttachedToWindowListener
import jp.mihailskuzmins.sugoinihongoapp.resources.OnSaveInstanceStateListener

class CustomTextInputEditText : TextInputEditText {

	private var mHasFocus: Boolean = false
	private var mOnSaveInstanceStateListener: OnSaveInstanceStateListener? = null
	private var mOnAttachedToWindowListener: OnAttachedToWindowListener? = null

	constructor(context: Context) : super(context)
	constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
	constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)

	init {
		super.setOnFocusChangeListener { _, hasFocus ->
			mHasFocus = hasFocus
		}
	}

	override fun setOnFocusChangeListener(l: OnFocusChangeListener?) =
		throw NotImplementedError()

	fun setOnSaveInstanceStateListener(l: OnSaveInstanceStateListener) {
		mOnSaveInstanceStateListener = l
	}

	fun setOnAttachedToWindowListener(l: OnAttachedToWindowListener) {
		if (isAttachedToWindow)
			l.invoke()

		mOnAttachedToWindowListener = l
	}

	override fun onAttachedToWindow() {
		super.onAttachedToWindow()

		mOnAttachedToWindowListener?.let {
			MainThreadLooper.runInMainThread(it::invoke)
		}
	}

	override fun onSaveInstanceState(): Parcelable? {
		// Invoke here, because when the view is removed, onFocusChanges is still fired, which will overwrite the value if assigned directly
		mOnSaveInstanceStateListener?.invoke(mHasFocus)
		mOnSaveInstanceStateListener = null
		mOnAttachedToWindowListener = null

		return super.onSaveInstanceState()
	}
}
