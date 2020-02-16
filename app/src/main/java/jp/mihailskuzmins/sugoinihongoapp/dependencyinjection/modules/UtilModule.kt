package jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import jp.mihailskuzmins.sugoinihongoapp.helpers.SingletonHolder
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.AuthUtil
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.ClipboardUtil
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SecurePreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import javax.inject.Singleton

@Module
class UtilModule private constructor (private val mContext: Context) {

	companion object : SingletonHolder<UtilModule, Context>(::UtilModule)

	@Provides
	@Singleton
	internal fun provideSharedPreferencesUtil() =
		SharedPreferencesUtil(mContext)

	@Provides
	@Singleton
	internal fun provideClipboardUtil() =
		ClipboardUtil(mContext)

	@Provides
	@Singleton
	internal fun provideAuthUtil() =
		AuthUtil()

	@Provides
	@Singleton
	internal fun provideSecurePreferencesUtil() =
		SecurePreferencesUtil(mContext)
}