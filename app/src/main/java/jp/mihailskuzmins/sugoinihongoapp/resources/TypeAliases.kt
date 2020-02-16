package jp.mihailskuzmins.sugoinihongoapp.resources

import java.io.File

typealias Action = () -> Unit
typealias ActionT<T> = (T) -> Unit
typealias FileFilter = (File, String) -> Boolean

typealias OnSaveInstanceStateListener = (hasFocus: Boolean) -> Unit
typealias OnAttachedToWindowListener = () -> Unit