package jp.mihailskuzmins.sugoinihongoapp.helpers.services

import android.content.Context
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.createAppInjector
import jp.mihailskuzmins.sugoinihongoapp.extensions.toDate
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.WordRepo
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.Keys
import kotlinx.coroutines.Dispatchers
import java.util.*
import javax.inject.Inject


class MarkDecrementService(context: Context) : AppService(context, Dispatchers.Unconfined) {

	@Inject lateinit var sharedPreferencesUtil: SharedPreferencesUtil
	@Inject lateinit var wordRepo: WordRepo

	init {
		context
			.createAppInjector()
			.inject(this)
	}

	override suspend fun run() {
		val decrementDays = sharedPreferencesUtil.getInt(Keys.wordMarkDecrementDays)

		wordRepo.decrementMarks(decrementDays) {
			Date().toDate().time
				.run {
					sharedPreferencesUtil.setLong(Keys.latestMarkDecrementEpoch, this)
				}
		}
	}
}
