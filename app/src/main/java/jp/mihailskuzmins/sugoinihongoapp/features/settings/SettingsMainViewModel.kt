package jp.mihailskuzmins.sugoinihongoapp.features.settings

import android.app.Application
import androidx.annotation.IdRes
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.BuildConfig
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItem
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemTextViewImage
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.initAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsNavigation
import jp.mihailskuzmins.sugoinihongoapp.userinterface.views.SectionHeader

class SettingsMainViewModel(app: Application) : DetailViewModelBase(app), SupportsNavigation {

	private val mNavActionIdSubject: Subject<Int> = PublishSubject.create()
	override val navActionIdObservable: Observable<Int> = mNavActionIdSubject.distinctUntilChanged()

	val wordQuizItem = DetailItemTextViewImage(app, R.string.word_quiz, R.drawable.ic_bookmark_24dp)
		.apply {
			onClickAction = { publishNavigate(R.id.settingsMain_to_settingsWordQuiz) }
		}

	val uiItem = DetailItemTextViewImage(app, R.string.general_user_interface, R.drawable.ic_tablet_android_black_24dp)
		.apply {
			onClickAction = { publishNavigate(R.id.settingsMain_to_settingsUi) }
		}

	val backupItem = DetailItemTextViewImage(app, R.string.general_backup, R.drawable.ic_archive_black_24dp)
		.apply {
			onClickAction = { publishNavigate(R.id.settingsMain_to_settingsBackup) }
		}

	val accountItem = DetailItemTextViewImage(app, R.string.auth_account, R.drawable.ic_account_circle_black_24dp)
		.apply {
			onClickAction = { publishNavigate(R.id.settingsMain_to_settingsAuthUser) }
		}

	val aboutItem = DetailItemTextViewImage(app, R.string.general_about_app, R.drawable.ic_info_outline_black_24dp)
		.apply {
			onClickAction = { publishNavigate(R.id.settingsMain_to_settingsAbout) }
		}

	val studySectionHeader = SectionHeader(app, R.string.settings_study)
	val technicalSectionHeader = SectionHeader(app, R.string.settings_technical)
	val appSectionHeader = SectionHeader(app, R.string.general_application)

	override fun initDetailItems(): Array<DetailItem> =
		arrayOf(wordQuizItem, uiItem, backupItem, accountItem, aboutItem)

	override fun initDetailItemValues(onInit: initAction) {
		wordQuizItem.value = context.getString(R.string.word_settings_manage_quiz)
		uiItem.value = context.getString(R.string.settings_manage_ui_elements)
		backupItem.value = context.getString(R.string.settings_manage_backups)
		accountItem.value = context.getString(R.string.settings_personal_information)
		aboutItem.value = context.getString(R.string.settings_app_version, BuildConfig.VERSION_NAME)

		onInit()
	}

	private fun publishNavigate(@IdRes navActionId: Int) {
		mNavActionIdSubject.onNext(navActionId)
	}
}