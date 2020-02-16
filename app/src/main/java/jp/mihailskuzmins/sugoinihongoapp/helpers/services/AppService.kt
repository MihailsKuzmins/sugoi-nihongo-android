package jp.mihailskuzmins.sugoinihongoapp.helpers.services

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class AppService(
	protected val context: Context,
	private val mDispatcher: CoroutineDispatcher) {

	fun runService() {
		GlobalScope.launch(mDispatcher) {
			run()
		}
	}

	protected abstract suspend fun run()
}