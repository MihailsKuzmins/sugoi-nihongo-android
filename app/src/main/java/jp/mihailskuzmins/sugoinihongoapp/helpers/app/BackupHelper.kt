package jp.mihailskuzmins.sugoinihongoapp.helpers.app

import android.content.Context
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.components.AppInjector
import jp.mihailskuzmins.sugoinihongoapp.extensions.*
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.backupDirectory
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.backupSyncDirectory
import jp.mihailskuzmins.sugoinihongoapp.helpers.services.BackupCreateService
import jp.mihailskuzmins.sugoinihongoapp.helpers.services.BackupUploadToFirebaseService
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.Keys
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject

class BackupHelper(context: Context, injector: AppInjector) : AuthHelperBase(context) {

	@Inject
	lateinit var sharedPreferencesUtil: SharedPreferencesUtil

	init {
		injector.inject(this)
	}

	override fun runInternal() {
		sharedPreferencesUtil.getBoolean(Keys.isBackupEnabled)
			.invokeIfFalse { return@runInternal }

		val lastRunEpoch = sharedPreferencesUtil.getLong(Keys.latestBackupCreationEpoch)
		val backupFrequency = sharedPreferencesUtil.getInt(Keys.backupCreationFrequencyDays)
		val creationEpoch = Date().toDate().add(Calendar.DAY_OF_YEAR, -backupFrequency).time
		val isNewBackupNeeded = lastRunEpoch <= creationEpoch

		initDirectories()
		cleanBackupDirectories(isNewBackupNeeded)
		uploadToFirebase()

		if (isNewBackupNeeded) {
			BackupCreateService(context)
				.runService()
		}
	}

	private fun initDirectories() {

		fun  initDirectory(directory: File) =
			if (directory.mkdir()) Timber.i("Created directory: %s", directory.absolutePath)
				else Timber.d("Directory exists: %s", directory.absolutePath)

		context
			.backupSyncDirectory
			.run(::initDirectory)

		context
			.backupDirectory
			.run(::initDirectory)
	}

	private fun cleanBackupDirectories(isNewBackupNeeded: Boolean) {
		context.backupDirectory
			.listFiles()
			.asSequence()
			.filter(File::isDirectory)
			.forEach { it.deleteRecursively() }

		// The new backup is created after the directory is cleaned, therefore, "-1" for the pending backup
		val backupFileCount = sharedPreferencesUtil
			.getInt(Keys.backupFileCount)
			.runIf(isNewBackupNeeded) { this - 1 }

		context.backupDirectory
			.listFiles(::isBackupFile)
			.asSequence()
			.sortedByDescending(File::getTimeCreated)
			.drop(backupFileCount)
			.forEach { it.delete() }
	}

	private fun uploadToFirebase() {
		context.backupSyncDirectory
			.listFiles(::isBackupFile)
			.any()
			.invokeIfTrue {
				BackupUploadToFirebaseService(context)
					.runService()
			}
	}
}