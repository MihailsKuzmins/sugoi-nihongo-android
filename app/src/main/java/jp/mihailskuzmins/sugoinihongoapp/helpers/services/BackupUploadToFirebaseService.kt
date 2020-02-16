package jp.mihailskuzmins.sugoinihongoapp.helpers.services

import android.content.Context
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.backupSyncDirectory
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.createAppInjector
import jp.mihailskuzmins.sugoinihongoapp.extensions.invokeIfFalse
import jp.mihailskuzmins.sugoinihongoapp.extensions.isBackupFile
import jp.mihailskuzmins.sugoinihongoapp.extensions.isInternetAvailable
import jp.mihailskuzmins.sugoinihongoapp.persistence.storage.BackupStorage
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class BackupUploadToFirebaseService(context: Context) : AppService(context, Dispatchers.IO) {

	@Inject
	lateinit var backupStorage: BackupStorage

	init {
		context
			.createAppInjector()
			.inject(this)
	}

	override suspend fun run() {
		isInternetAvailable()
			.invokeIfFalse { return@run }

		context.backupSyncDirectory
			.listFiles(::isBackupFile)
			.forEach {
				backupStorage.upload(it) {
					it.delete()
				}
			}
	}
}
