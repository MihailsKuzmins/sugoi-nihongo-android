package jp.mihailskuzmins.sugoinihongoapp.helpers.piechart

import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.round

class RelativeValueFormatter(private val totalCount: Int) : ValueFormatter() {

	override fun getFormattedValue(value: Float) =
		"${round(value).toInt()} / $totalCount"
}