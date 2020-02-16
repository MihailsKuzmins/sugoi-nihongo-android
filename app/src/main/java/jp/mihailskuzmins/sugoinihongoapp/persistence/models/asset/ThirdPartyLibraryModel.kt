package jp.mihailskuzmins.sugoinihongoapp.persistence.models.asset

import androidx.annotation.LayoutRes
import jp.mihailskuzmins.sugoinihongoapp.R

class ThirdPartyLibraryModel(
	val name: String,
	val copyright: String,
	val projectUrl: String,
	val attribution: Attribution) {

	enum class Attribution {
		Apache2
	}
}

@LayoutRes
fun ThirdPartyLibraryModel.Attribution.toLayoutResource() =
	when (this) {
		ThirdPartyLibraryModel.Attribution.Apache2 -> R.layout.attribution_apache2
	}