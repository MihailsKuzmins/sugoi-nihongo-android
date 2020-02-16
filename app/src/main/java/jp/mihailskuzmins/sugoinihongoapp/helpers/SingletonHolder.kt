package jp.mihailskuzmins.sugoinihongoapp.helpers

open class SingletonHolder<out T: Any, in A>(creator: (A) -> T) {

	private var mCreator: ((A) -> T)? = creator
	@Volatile private var mInstance: T? = null

	open fun getInstance(arg: A): T {
		val instance = mInstance
		if (instance != null)
			return instance

		return synchronized(this) {
			val instance2 = mInstance
			if (instance2 != null)
				instance2
			else {
				val created = mCreator!!(arg)
				mInstance = created
				mCreator = null
				created
			}
		}
	}
}