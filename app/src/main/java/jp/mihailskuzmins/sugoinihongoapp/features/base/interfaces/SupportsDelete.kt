package jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces

import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.AppConfirmDialog

interface SupportsDelete : SupportsNavigationBack {

	val deleteConfirmDialog: AppConfirmDialog

	fun delete()
	fun publishDelete()
}