package jp.mihailskuzmins.sugoinihongoapp.helpers.piechart

import android.content.Context
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class PieDataSetBuilder(context: Context, items: Array<PieEntry>, colors: Array<Int>) {

	private val mDataSet: PieDataSet

	init {
		val notEmptyItems = items
			.mapIndexed { i, it -> Pair(it, colors[i]) }
			.filter { it.first.value > 0f }

		val includedItems = notEmptyItems
			.map { it.first }

		val includedColors = notEmptyItems
			.map { it.second }
			.toIntArray()

		mDataSet = PieDataSet(includedItems, null)
		mDataSet.setColors(includedColors, context)
	}

	fun setValueFormatter(valueFormatter: ValueFormatter): PieDataSetBuilder {
		mDataSet.valueFormatter = valueFormatter
		return this
	}

	fun setValuePosition(valuePosition: PieDataSet.ValuePosition): PieDataSetBuilder {
		mDataSet.xValuePosition = valuePosition
		mDataSet.yValuePosition = valuePosition

		return this
	}

	fun build() = mDataSet
}