package jp.mihailskuzmins.sugoinihongoapp.userinterface.bindingadapters

import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.data.PieData
import jp.mihailskuzmins.sugoinihongoapp.userinterface.controls.CustomPieChart

@BindingAdapter("pieChartData")
fun setPieChartData(pieChart: CustomPieChart, pieData: PieData?) {
	pieData?.let {
		pieChart.data = it
		pieChart.invalidate()
	}
}

@BindingAdapter("usePercentValues")
fun setUsePercentData(pieChart: CustomPieChart, usePercentValues: Boolean) {
	pieChart.setUsePercentValues(usePercentValues)
}

@BindingAdapter("centerText")
fun setCenterText(pieChart: CustomPieChart, centerText: String?) {
	centerText?.let { pieChart.centerText = it }
}