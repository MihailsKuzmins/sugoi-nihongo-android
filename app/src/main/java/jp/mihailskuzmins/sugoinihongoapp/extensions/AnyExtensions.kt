package jp.mihailskuzmins.sugoinihongoapp.extensions

inline fun <T> T.invoke(func: (T) -> Unit) {
	func(this)
}

inline fun <reified T> Any.cast() =
	this as T


inline fun <T> T.runIf(predicate: Boolean, func: T.() -> T): T =
	if (predicate) func(this)
		else this

inline fun <T> T.runIf(predicate: (T) -> Boolean, func: T.() -> T): T =
	if (predicate(this)) func(this)
		else this

inline fun <T, R> T.runIf(predicate: Boolean, funcTrue: T.() -> R, funcFalse: T.() -> R): R =
	if (predicate) funcTrue(this)
		else funcFalse(this)

inline fun <T> T.applyIf(predicate: Boolean, func: T.() -> Unit): T {
	if (predicate)
		func(this)

	return this
}

inline fun <T> T.invokeIf(predicate: (T) -> Boolean, func: (T) -> Unit) {
	if (predicate(this))
		func(this)
}

inline fun <T> T?.letIfNull(func: () -> T): T =
	this ?: func()

fun <T: Any> T.isEqual(value: T) =
	when (this) {
		is String -> this.trim() == (value as String).trim()
		else -> this == value
	}

inline fun <T> List<T>.sumByLong(selector: (T) -> Long): Long {
	var sum = 0L
	for (element in this) {
		sum += selector(element)
	}
	return sum
}

val <T: Any?> T?.hasValue: Boolean
	get() = this != null