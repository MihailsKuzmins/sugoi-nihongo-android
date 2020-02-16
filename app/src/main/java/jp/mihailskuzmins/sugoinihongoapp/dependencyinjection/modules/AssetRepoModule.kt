package jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import jp.mihailskuzmins.sugoinihongoapp.helpers.SingletonHolder
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.asset.AppSummaryRepo
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.asset.KanaRepo
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.asset.ThirdPartyLibraryRepo
import javax.inject.Singleton

@Module
class AssetRepoModule private constructor(private val mContext: Context) {

	companion object : SingletonHolder<AssetRepoModule, Context>(::AssetRepoModule)

	@Provides
	@Singleton
	internal fun provideAppSummaryRepo() =
		AppSummaryRepo(mContext)

	@Provides
	@Singleton
	internal fun provideThirdPartyLibraryRepo() =
		ThirdPartyLibraryRepo(mContext)

	@Provides
	@Singleton
	internal fun provideKanaRepo() =
		KanaRepo(mContext)
}