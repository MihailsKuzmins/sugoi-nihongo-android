package jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems

import android.content.Context
import androidx.annotation.StringRes
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import jp.mihailskuzmins.sugoinihongoapp.extensions.applyIf
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.BindableProp

class DetailItemPieChart(
		context: Context,
		@StringRes label: Int) :
	DetailItemBase<Unit>(context, label, Unit) {

	var pieData: PieData? by BindableProp(BR.pieData, null)
		@Bindable get
		private set

	var usePercentValues by BindableProp(BR.usePercentValues, false)
		@Bindable get

	var centerText by BindableProp(BR.centerText, "")
		@Bindable get

	fun initChartData(pieDataSet: PieDataSet) {
		isVisible = pieDataSet.entryCount > 0

		this.applyIf(isVisible) {
			pieData = PieData(pieDataSet)
		}
	}
}