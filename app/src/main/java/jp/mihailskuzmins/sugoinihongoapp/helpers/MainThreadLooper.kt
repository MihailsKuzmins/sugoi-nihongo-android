package jp.mihailskuzmins.sugoinihongoapp.helpers

import android.os.Handler
import android.os.Looper
import jp.mihailskuzmins.sugoinihongoapp.resources.Action

object MainThreadLooper {

	@Volatile
	private var handler: Handler? = null

	fun runInMainThread(action: Action) {
		if (handler?.looper != Looper.getMainLooper())
			handler = Handler(Looper.getMainLooper())

		handler?.post(action)
	}
}