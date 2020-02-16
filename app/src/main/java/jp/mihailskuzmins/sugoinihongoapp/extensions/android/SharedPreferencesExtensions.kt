package jp.mihailskuzmins.sugoinihongoapp.extensions.android

import androidx.appcompat.app.AppCompatDelegate
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.Keys

fun SharedPreferencesUtil.setThemeMode() {
	val mode = if (getBoolean(Keys.uiIsDarkMode)) AppCompatDelegate.MODE_NIGHT_YES
	else AppCompatDelegate.MODE_NIGHT_NO

	AppCompatDelegate.setDefaultNightMode(mode)
}