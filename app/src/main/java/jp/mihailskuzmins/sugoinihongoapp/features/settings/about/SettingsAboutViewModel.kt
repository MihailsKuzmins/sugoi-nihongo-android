package jp.mihailskuzmins.sugoinihongoapp.features.settings.about

import android.app.Application
import android.content.Context
import androidx.annotation.IdRes
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.BuildConfig
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.browse
import jp.mihailskuzmins.sugoinihongoapp.extensions.dateFrom
import jp.mihailskuzmins.sugoinihongoapp.extensions.formatDate
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItem
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemTextView
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemTextViewImage
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.initAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsNavigation
import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants
import jp.mihailskuzmins.sugoinihongoapp.userinterface.views.SectionHeader

class SettingsAboutViewModel(app: Application) : DetailViewModelBase(app), SupportsNavigation {

	private val mNavActionIdSubject: Subject<Int> = PublishSubject.create()

	val appNameItem = DetailItemTextView(app, R.string.general_application)
	val appVersionItem = DetailItemTextView(app, R.string.general_version)
	val appWebsiteItem = DetailItemTextViewImage(app, R.string.general_website, R.drawable.ic_language_black_24dp)
		.apply {
			onClickAction = { app.browse(AppConstants.Urls.appWebsiteUrl) }
		}

	val devCreditsItem = DetailItemTextViewImage(app, R.string.settings_dev_credits, R.drawable.ic_android_black_24dp)
		.apply {
			onClickAction = { publishNavigate(R.id.settingsAbout_to_settingsThirdPartyLibs) }
		}

	val appSummaryItem = DetailItemTextViewImage(app, R.string.settings_app_summary, R.drawable.ic_info_outline_black_24dp)
		.apply {
			onClickAction = { publishNavigate(R.id.settingsAbout_to_settingsAppSummary) }
		}

	val resourcesSection = SectionHeader(app, R.string.general_resources)

	override val navActionIdObservable: Observable<Int>
		get() = mNavActionIdSubject.distinctUntilChanged()

	override fun initDetailItems(): Array<DetailItem> =
		arrayOf(appNameItem, appVersionItem, appWebsiteItem, devCreditsItem, appSummaryItem)

	override fun initDetailItemValues(onInit: initAction) {
		val context: Context = getApplication()

		val date = dateFrom(2020, 1, 20)
			.formatDate()

		appNameItem.value = context.getString(R.string.app_name)
		appVersionItem.value = context.getString(R.string.settings_version_as_of, BuildConfig.VERSION_NAME, date)
		appWebsiteItem.value = context.getString(R.string.settings_visit_app_website)
		devCreditsItem.value = context.getString(R.string.settings_third_party_dev)
		appSummaryItem.value = context.getString(R.string.settings_app_intention_description)

		onInit()
	}

	private fun publishNavigate(@IdRes navActionId: Int) {
		mNavActionIdSubject.onNext(navActionId)
	}
}