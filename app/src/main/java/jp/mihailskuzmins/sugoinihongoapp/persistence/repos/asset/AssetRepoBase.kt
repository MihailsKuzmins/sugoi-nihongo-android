package jp.mihailskuzmins.sugoinihongoapp.persistence.repos.asset

import android.content.Context
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

abstract class AssetRepoBase(private val mContext: Context) {

	protected fun <T> getAssetStreamReader(path: String, readerAction: (InputStreamReader) -> T): T =
		mContext.assets.open(path)
			.use {
				InputStreamReader(it, StandardCharsets.UTF_8)
					.use(readerAction)
			}

	protected fun <T> getResourceStreamReader(path: String, readerAction: (InputStreamReader) -> T): T =
		AssetRepoBase::class.java.getResource(path)!!
			.openStream()
			.use {
				InputStreamReader(it, StandardCharsets.UTF_8)
					.use(readerAction)
			}
}