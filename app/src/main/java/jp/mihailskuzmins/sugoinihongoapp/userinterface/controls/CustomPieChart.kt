package jp.mihailskuzmins.sugoinihongoapp.userinterface.controls

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.PieChart
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke

class CustomPieChart : PieChart {

	constructor(context: Context) : super(context)
	constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
	constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)

	init {
		description.isEnabled = false
		legend.isEnabled = false
		setHoleColor(android.R.color.transparent)
		setNoDataText("")

		ResourcesCompat
			.getColor(context.resources, R.color.colorToolbarItem, context.theme)
			.invoke {
				setEntryLabelColor(it)
				setCenterTextColor(it)
			}
	}
}