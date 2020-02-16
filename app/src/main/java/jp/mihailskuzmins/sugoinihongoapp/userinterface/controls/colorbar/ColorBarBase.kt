package jp.mihailskuzmins.sugoinihongoapp.userinterface.controls.colorbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

abstract class ColorBarBase<T> : LinearLayout {

	private lateinit var mKeyIndexMap: Map<T, Int>
	private var mItemIndex = 0

	constructor(context: Context) : super(context)
	constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
	constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)

	init {
		orientation = HORIZONTAL
	}

	fun initColors(vararg pairs: Pair<T, Int>) {
		fun createView(colorRes: Int) =
			View(context).apply {
				setLayoutParams(0)
				setBackgroundResource(colorRes)
			}

		fun appendView(view: View) =
			view.run {
				this@ColorBarBase.addView(view)
				mItemIndex++
			}

		mKeyIndexMap = pairs
			.map {
				createView(it.second)
					.run { appendView(this) }
					.run { Pair(it.first, this) }
			}.toMap()
	}

	fun setItemWeights(map: Map<T, Int>) {
		map.forEach {
			mKeyIndexMap[it.key].also { x -> getChildAt(x!!).setLayoutParams(it.value) }
		}

		mKeyIndexMap
			.filter { it.key !in map.keys }
			.forEach { getChildAt(it.value).setLayoutParams(0) }
	}

	private fun View.setLayoutParams(weight: Int) {
		val floatWeight = weight.toFloat()

		if ((layoutParams as? LayoutParams)?.weight == floatWeight)
			return

		layoutParams = LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, floatWeight)
	}
}