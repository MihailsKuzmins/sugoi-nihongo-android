package jp.mihailskuzmins.sugoinihongoapp.helpers.app

import android.content.Context
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.AuthUtil
import javax.inject.Inject

abstract class AuthHelperBase(protected val context: Context) : Runnable {

	@Inject
	lateinit var authUtil: AuthUtil

	override fun run() {
		if (!authUtil.isAuthenticated)
			return

		runInternal()
	}

	protected abstract fun runInternal()
}