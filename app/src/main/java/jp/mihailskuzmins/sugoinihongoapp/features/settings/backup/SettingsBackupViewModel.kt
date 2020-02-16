package jp.mihailskuzmins.sugoinihongoapp.features.settings.backup

import android.app.Application
import android.text.format.Formatter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.backupDirectory
import jp.mihailskuzmins.sugoinihongoapp.extensions.formatDateTime
import jp.mihailskuzmins.sugoinihongoapp.extensions.getTimeCreated
import jp.mihailskuzmins.sugoinihongoapp.extensions.isBackupFile
import jp.mihailskuzmins.sugoinihongoapp.extensions.sumByLong
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.*
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.initAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsNavigation
import jp.mihailskuzmins.sugoinihongoapp.helpers.rx.RxFileScanner
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.Int
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.Keys
import jp.mihailskuzmins.sugoinihongoapp.userinterface.views.SectionHeader
import java.io.File
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

class SettingsBackupViewModel(app: Application) : DetailViewModelBase(app), SupportsNavigation {

	private val mNavActionIdSubject: Subject<kotlin.Int> = PublishSubject.create()

	@Inject
	lateinit var sharedPreferencesUtil: SharedPreferencesUtil

	var isBackupEnabledItem = DetailItemSwitch(app, R.string.settings_backup_create_backups, R.string.settings_backup_backup_data)
		.apply {
			addValueChangedAction { sharedPreferencesUtil.setBoolean(Keys.isBackupEnabled, it) }
		}

	val backupFileCountItem = DetailItemSeekBar(app, R.string.settings_backup_number_of_files, Int.backupFileMaxCount)
		.apply {
			addValueChangedAction { sharedPreferencesUtil.setInt(Keys.backupFileCount, it) }
		}

	val backupCreationFrequencyDaysItem = DetailItemSeekBar(app, R.string.settings_backup_creation_frequency, Int.backupCreationFrequencyMaxDays)
		.apply {
			addValueChangedAction { sharedPreferencesUtil.setInt(Keys.backupCreationFrequencyDays, it) }
		}

	val localBackupCountItem = DetailItemTextViewAction(app, R.string.settings_local_backups)
		.apply {
			onClickAction = { mNavActionIdSubject.onNext(R.id.settingsBackup_to_settingsBackupList) }
		}

	val lastBackupDateItem = DetailItemTextView(app, R.string.settings_last_backup_date)
	val infoSectionHeader = SectionHeader(app, R.string.general_information)

	init {
		isBackupEnabledItem.valueChanged
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe {
				backupFileCountItem.isEnabled = it
				backupCreationFrequencyDaysItem.isEnabled = it
			}.addTo(cleanUp)

		val backupFilesObservable = RxFileScanner(context.backupDirectory, ::isBackupFile)
			.filesObservable
			.publish()

		backupFilesObservable
			.map { it.isNotEmpty() }
			.distinctUntilChanged()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { localBackupCountItem.isEnabled = it }
			.addTo(cleanUp)

		backupFilesObservable
			.map {
				if (it.isEmpty())
					return@map context.getString(R.string.general_none)

				val totalSize = it
					.sumByLong(File::length)
					.run { Formatter.formatShortFileSize(context, this) }

				return@map "${it.size} ($totalSize)"
			}.distinctUntilChanged()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { localBackupCountItem.value = it }
			.addTo(cleanUp)

		backupFilesObservable
			.map {
				if (it.isEmpty())
					context.getString(R.string.general_none)
				else
					it.maxBy(File::getTimeCreated)!!
						.getTimeCreated()
						.run(::Date)
						.formatDateTime(timeFormat = DateFormat.SHORT)
			}.distinctUntilChanged()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { lastBackupDateItem.value = it }
			.addTo(cleanUp)

		backupFilesObservable
			.connect()
			.addTo(cleanUp)
	}

	override val navActionIdObservable: Observable<kotlin.Int>
		get() = mNavActionIdSubject.distinctUntilChanged()

	override fun initDetailItems(): Array<DetailItem> =
		arrayOf(isBackupEnabledItem, backupFileCountItem, backupCreationFrequencyDaysItem)

	override fun initDetailItemValues(onInit: initAction) {
		isBackupEnabledItem.initialValue = sharedPreferencesUtil.getBoolean(Keys.isBackupEnabled)
		backupFileCountItem.initialValue = sharedPreferencesUtil.getInt(Keys.backupFileCount)
		backupCreationFrequencyDaysItem.initialValue = sharedPreferencesUtil.getInt(Keys.backupCreationFrequencyDays)

		onInit()
	}
}