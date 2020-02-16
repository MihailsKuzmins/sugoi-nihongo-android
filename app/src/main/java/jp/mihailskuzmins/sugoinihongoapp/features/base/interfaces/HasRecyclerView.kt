package jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces

import android.content.Context
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.userinterface.helpers.AppLinearLayoutManager
import jp.mihailskuzmins.sugoinihongoapp.userinterface.helpers.DividerNotLastItemDecoration

interface HasRecyclerView {

	val recyclerView: RecyclerView

	fun createItemDecoration(context: Context, orientation: Int): RecyclerView.ItemDecoration {
		return DividerNotLastItemDecoration(context, orientation)
	}
}

fun HasRecyclerView.initRecyclerView(context: Context) {
	val layoutManager = AppLinearLayoutManager(context)
	layoutManager.orientation = RecyclerView.VERTICAL
	recyclerView.layoutManager = layoutManager
	recyclerView.itemAnimator = DefaultItemAnimator()

	createItemDecoration(context, layoutManager.orientation)
		.invoke(recyclerView::addItemDecoration)
}