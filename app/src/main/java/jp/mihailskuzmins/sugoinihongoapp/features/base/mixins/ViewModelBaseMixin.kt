package jp.mihailskuzmins.sugoinihongoapp.features.base.mixins

import android.content.Intent
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.features.base.ViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItem

fun getDataForSave(detailItems: Array<DetailItem>): Map<String, Any?> =
	detailItems.asSequence()
		.filter(DetailItem::isValid) // For DetailItems which are saved onClose
		.filter { it.isValueChanged || !it.isEnabled }
		.associateBy({it.tag!!}, { if (it.isEnabled) it.valueAny else null })

fun ViewModelBase.startActivity(cls: Class<*>) =
	Intent(context, cls)
		.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		.invoke(context::startActivity)