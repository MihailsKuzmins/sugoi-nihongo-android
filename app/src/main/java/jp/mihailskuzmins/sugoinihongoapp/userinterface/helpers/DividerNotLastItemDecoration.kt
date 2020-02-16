package jp.mihailskuzmins.sugoinihongoapp.userinterface.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class DividerNotLastItemDecoration(context: Context, orientation: Int) : RecyclerView.ItemDecoration() {

	private val mHorizontal = LinearLayout.HORIZONTAL
	private val mVertical = LinearLayout.VERTICAL
	private val mBounds = Rect()

	private val mAttrs = intArrayOf(android.R.attr.listDivider)

	private var mDivider: Drawable? = null
	private var mOrientation: Int = 0

	init {
		val attrs = context.obtainStyledAttributes(mAttrs)
		mDivider = attrs.getDrawable(0)

		attrs.recycle()
		setOrientation(orientation)
	}

	private fun setOrientation(orientation: Int) {
		require(!(orientation != mHorizontal && orientation != mVertical)) { "Invalid orientation. It should be either mHorizontal or mVertical" }
		mOrientation = orientation
	}

	override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
		if (parent.layoutManager == null || mDivider == null)
			return

		if (mOrientation == mVertical)
			drawVertical(c, parent)
		else
			drawHorizontal(c, parent)
	}

	private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
		canvas.save()
		val left: Int
		val right: Int

		if (parent.clipToPadding) {
			left = parent.paddingLeft
			right = parent.width - parent.paddingRight
			canvas.clipRect(
				left, parent.paddingTop, right,
				parent.height - parent.paddingBottom
			)
		} else {
			left = 0
			right = parent.width
		}

		val childCount = parent.childCount
		for (i in 0 until childCount - 1) {
			val child = parent.getChildAt(i)
			parent.getDecoratedBoundsWithMargins(child, mBounds)
			val bottom = mBounds.bottom + child.translationY.roundToInt()
			val top = bottom - mDivider!!.intrinsicHeight
			mDivider!!.setBounds(left, top, right, bottom)
			mDivider!!.draw(canvas)
		}
		canvas.restore()
	}

	private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
		canvas.save()
		val top: Int
		val bottom: Int

		if (parent.clipToPadding) {
			top = parent.paddingTop
			bottom = parent.height - parent.paddingBottom
			canvas.clipRect(
				parent.paddingLeft, top,
				parent.width - parent.paddingRight, bottom
			)
		} else {
			top = 0
			bottom = parent.height
		}

		val childCount = parent.childCount
		for (i in 0 until childCount - 1) {
			val child = parent.getChildAt(i)
			parent.layoutManager!!.getDecoratedBoundsWithMargins(child, mBounds)
			val right = mBounds.right + child.translationX.roundToInt()
			val left = right - mDivider!!.intrinsicWidth
			mDivider!!.setBounds(left, top, right, bottom)
			mDivider!!.draw(canvas)
		}
		canvas.restore()
	}

	override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
		if (mDivider == null) {
			outRect.set(0, 0, 0, 0)
			return
		}

		if (mOrientation == mVertical)
			outRect.set(0, 0, 0, mDivider!!.intrinsicHeight)
		else
			outRect.set(0, 0, mDivider!!.intrinsicWidth, 0)
	}
}
