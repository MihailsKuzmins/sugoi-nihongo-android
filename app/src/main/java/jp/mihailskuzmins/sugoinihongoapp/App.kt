package jp.mihailskuzmins.sugoinihongoapp

import android.app.Application
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.createAppInjector
import jp.mihailskuzmins.sugoinihongoapp.helpers.app.*
import timber.log.Timber

@Suppress("unused")
class App : Application() {

	override fun onCreate() {
		super.onCreate()

		if (BuildConfig.DEBUG) {
			Timber.plant(Timber.DebugTree())
		}

		val injector = createAppInjector()

		listOf(
			DefaultPreferencesInitialiser(injector),
			ThemeModeInitialiser(injector),
			MarkDecrementHelper(this, injector),
			BackupHelper(this, injector),
			StudyHistoryCleanHelper(injector)
		).forEach(Runnable::run)
	}
}