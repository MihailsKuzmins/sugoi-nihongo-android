package jp.mihailskuzmins.sugoinihongoapp.features.settings

import android.app.Application
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.setThemeMode
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItem
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemSwitch
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.initAction
import jp.mihailskuzmins.sugoinihongoapp.helpers.MainThreadLooper
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.Keys
import javax.inject.Inject

class SettingsUserInterfaceViewModel(app: Application) : DetailViewModelBase(app) {

	@Inject
	lateinit var sharedPreferencesUtil: SharedPreferencesUtil

	val isEntryIdShownItem = DetailItemSwitch(app, R.string.settings_ui_show_entry_id, R.string.settings_ui_entry_id_is_shown)
		.apply {
			addValueChangedAction { sharedPreferencesUtil.setBoolean(Keys.uiShowEntryId, it) }
		}

	val isWordMarkTableShownItem = DetailItemSwitch(app, R.string.settings_ui_show_word_mark_table, R.string.settings_ui_word_mark_table_is_shown)
		.apply {
			addValueChangedAction { sharedPreferencesUtil.setBoolean(Keys.uiShowWordMarkTable, it) }
		}

	val isDarkModeItem = DetailItemSwitch(app, R.string.settings_dark_mode, R.string.settings_set_dark_mode_for_application)
		.apply {
			addValueChangedAction {
				sharedPreferencesUtil.setBoolean(Keys.uiIsDarkMode, it)
				MainThreadLooper.runInMainThread(sharedPreferencesUtil::setThemeMode)
			}
		}

	override fun initDetailItems(): Array<DetailItem> =
		arrayOf(isEntryIdShownItem, isWordMarkTableShownItem, isDarkModeItem)

	override fun initDetailItemValues(onInit: initAction) {
		isEntryIdShownItem.initialValue = sharedPreferencesUtil.getBoolean(Keys.uiShowEntryId)
		isWordMarkTableShownItem.initialValue = sharedPreferencesUtil.getBoolean(Keys.uiShowWordMarkTable)
		isDarkModeItem.initialValue = sharedPreferencesUtil.getBoolean(Keys.uiIsDarkMode)

		onInit()
	}
}