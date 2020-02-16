package jp.mihailskuzmins.sugoinihongoapp.features.settings.auth

import android.app.Application
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItem
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemTextView
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.initAction
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.AuthUtil
import javax.inject.Inject

class SettingsAuthUserViewModel(app: Application) : DetailViewModelBase(app) {

	@Inject lateinit var authUtil: AuthUtil

	val emailItem = DetailItemTextView(app, R.string.auth_email)

	override fun initDetailItems(): Array<DetailItem> =
		arrayOf(emailItem)

	override fun initDetailItemValues(onInit: initAction) {
		emailItem.value = authUtil.userEmail

		onInit()
	}

	fun signOut() =
		authUtil.signOut()
}