package jp.mihailskuzmins.sugoinihongoapp.helpers.app

import android.content.Context
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.components.AppInjector
import jp.mihailskuzmins.sugoinihongoapp.extensions.invokeIfFalse
import jp.mihailskuzmins.sugoinihongoapp.extensions.toDate
import jp.mihailskuzmins.sugoinihongoapp.helpers.services.MarkDecrementService
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.Keys
import java.util.*
import javax.inject.Inject

class MarkDecrementHelper(context: Context, injector: AppInjector) : AuthHelperBase(context) {

	@Inject
	lateinit var sharedPreferencesUtil: SharedPreferencesUtil

	init {
		injector.inject(this)
	}

	override fun runInternal() {
		sharedPreferencesUtil.getBoolean(Keys.wordMarkIsDecrementEnabled)
			.invokeIfFalse { return@runInternal }

		val lastRunEpoch = sharedPreferencesUtil.getLong(Keys.latestMarkDecrementEpoch)
		val todayEpoch = Date().toDate().time

		if (lastRunEpoch < todayEpoch) {
			MarkDecrementService(context)
				.runService()
		}
	}
}