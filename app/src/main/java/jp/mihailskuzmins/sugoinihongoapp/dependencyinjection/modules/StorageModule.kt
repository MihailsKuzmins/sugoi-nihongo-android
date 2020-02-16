package jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import jp.mihailskuzmins.sugoinihongoapp.helpers.SingletonHolder
import jp.mihailskuzmins.sugoinihongoapp.persistence.storage.BackupStorage
import javax.inject.Singleton

@Module
class StorageModule private constructor (private val mContext: Context) {

	companion object : SingletonHolder<StorageModule, Context>(::StorageModule)

	@Provides
	@Singleton
	fun provideBackupStorage() =
		BackupStorage(mContext)
}