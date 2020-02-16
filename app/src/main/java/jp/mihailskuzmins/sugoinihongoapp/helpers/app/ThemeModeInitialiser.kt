package jp.mihailskuzmins.sugoinihongoapp.helpers.app

import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.components.AppInjector
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.setThemeMode
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import javax.inject.Inject

class ThemeModeInitialiser(injector: AppInjector): Runnable {

	@Inject
	lateinit var sharedPreferencesUtil: SharedPreferencesUtil

	init {
		injector.inject(this)
	}

	override fun run() = sharedPreferencesUtil.setThemeMode()
}