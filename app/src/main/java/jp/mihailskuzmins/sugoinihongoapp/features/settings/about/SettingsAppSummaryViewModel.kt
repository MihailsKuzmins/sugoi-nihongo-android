package jp.mihailskuzmins.sugoinihongoapp.features.settings.about

import android.app.Application
import jp.mihailskuzmins.sugoinihongoapp.features.base.ViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.asset.AppSummaryRepo
import javax.inject.Inject

class SettingsAppSummaryViewModel(app: Application) : ViewModelBase(app) {

	@Inject
	lateinit var appSummaryRepo: AppSummaryRepo

	val appSummary: String

	init {
		appSummary = appSummaryRepo.getAppSummary()
		isInitialisedSubject.onNext(true)
	}
}