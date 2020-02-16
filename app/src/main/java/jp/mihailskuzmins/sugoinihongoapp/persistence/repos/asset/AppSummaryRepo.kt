package jp.mihailskuzmins.sugoinihongoapp.persistence.repos.asset

import android.content.Context
import jp.mihailskuzmins.sugoinihongoapp.extensions.combinePath
import jp.mihailskuzmins.sugoinihongoapp.extensions.runIf
import jp.mihailskuzmins.sugoinihongoapp.extensions.toLowerCaseEx
import java.io.InputStreamReader
import java.util.*

class AppSummaryRepo(context: Context) : AssetRepoBase(context) {

	private val mDefaultLocale = "en"
	private val mAvailableLocales = listOf(mDefaultLocale, "de", "es", "fr", "it", "nl", "pl", "pt", "ru")

	fun getAppSummary(): String {
		val locale = Locale.getDefault()
			.language
			.toLowerCaseEx()
			.runIf({ it !in mAvailableLocales }, { mDefaultLocale })

		return combinePath("appSummary", "app_summary.$locale.txt")
			.run { getAssetStreamReader(this, InputStreamReader::readText) }
	}
}