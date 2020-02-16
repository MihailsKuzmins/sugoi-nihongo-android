package jp.mihailskuzmins.sugoinihongoapp.persistence.repos.asset

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jp.mihailskuzmins.sugoinihongoapp.extensions.toLowerCaseEx
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.asset.ThirdPartyLibraryModel


class ThirdPartyLibraryRepo(context: Context) : AssetRepoBase(context) {

	private val mTypeToken by lazy {
		object : TypeToken<List<ThirdPartyLibraryModel>>() {}
	}

	fun getThirdPartyLibraries(): List<ThirdPartyLibraryModel> =
		getAssetStreamReader("third_party_libraries.json") {
			return@getAssetStreamReader Gson()
				.fromJson<List<ThirdPartyLibraryModel>>(it, mTypeToken.type)
				.sortedBy { x -> x.name.toLowerCaseEx() }
		}
}