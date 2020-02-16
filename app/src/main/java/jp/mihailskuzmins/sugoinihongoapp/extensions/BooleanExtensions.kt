package jp.mihailskuzmins.sugoinihongoapp.extensions

import jp.mihailskuzmins.sugoinihongoapp.resources.Action

inline fun Boolean.alsoIfTrue(action: Action): Boolean {
	if (this)
		action()

	return this
}

inline fun Boolean.alsoIfFalse(action: Action): Boolean {
	if (!this)
		action()

	return this
}

inline fun Boolean.invokeIfTrue(action: Action) {
	if (this)
		action()
}

inline fun Boolean.invokeIfFalse(action: Action) {
	if (!this)
		action()
}