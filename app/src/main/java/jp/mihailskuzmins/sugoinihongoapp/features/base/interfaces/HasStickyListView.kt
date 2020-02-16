package jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView
import se.emilsjolander.stickylistheaders.StickyListHeadersListView

interface IsSticky<TListView: StickyListHeadersListView> {

	val listView: TListView
}

interface HasStickyListView : IsSticky<StickyListHeadersListView>
interface HasStickyExpandableListView : IsSticky<ExpandableStickyListHeadersListView>

fun HasStickyListView.initListView() {
	initListView(listView)
}

fun HasStickyExpandableListView.initListView() {
	initListView(listView)
}


private fun <TListView: StickyListHeadersListView> initListView(listView: TListView) {
	listView.isDrawingListUnderStickyHeader = false
}