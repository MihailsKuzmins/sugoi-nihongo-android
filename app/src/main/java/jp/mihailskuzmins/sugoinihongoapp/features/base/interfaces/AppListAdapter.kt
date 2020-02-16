package jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces

interface AppListAdapter<TModel> {

	fun updateItems(items: List<TModel>)
}