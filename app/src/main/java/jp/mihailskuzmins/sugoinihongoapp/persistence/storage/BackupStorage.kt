package jp.mihailskuzmins.sugoinihongoapp.persistence.storage

import android.content.Context
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.createAppInjector
import jp.mihailskuzmins.sugoinihongoapp.resources.Action
import jp.mihailskuzmins.sugoinihongoapp.resources.FirebaseStorageConstants
import java.io.File

class BackupStorage(context: Context) : FirebaseStorageBase() {

	private val mReference = FirebaseStorageConstants.backupReference

	init {
		context
			.createAppInjector()
			.inject(this)
	}

	fun upload(file: File, action: Action) =
		upload(mReference, file, action)
}