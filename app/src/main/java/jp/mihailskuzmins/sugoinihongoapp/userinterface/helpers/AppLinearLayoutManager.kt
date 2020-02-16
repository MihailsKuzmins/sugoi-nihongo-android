package jp.mihailskuzmins.sugoinihongoapp.userinterface.helpers

import android.content.Context
import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import jp.mihailskuzmins.sugoinihongoapp.extensions.getPrivateField

class AppLinearLayoutManager(context: Context) : LinearLayoutManager(context) {

	private var mPosition: Int? = null
	private var mOffset: Int? = null

	override fun onScrollStateChanged(state: Int) {
		super.onScrollStateChanged(state)

		mPosition = findFirstVisibleItemPosition()
		mOffset = mPosition?.let { findViewByPosition(it)?.top ?: 0 }
	}

	override fun onRestoreInstanceState(state: Parcelable?) {
		super.onRestoreInstanceState(state)

		if (state == null)
			return

		mPosition = state.getPrivateField("mAnchorPosition")
		mOffset = state.getPrivateField("mAnchorOffset")
	}

	fun scrollToLastKnowPosition() {
		var position = mPosition; var offset = mOffset
		if (position == null || offset == null)
			return

		// Scroll to the last item in case of the overflow
		if (position >= itemCount) {
			position = itemCount - 1
			offset = 0
		}

		scrollToPositionWithOffset(position, offset)
	}
}