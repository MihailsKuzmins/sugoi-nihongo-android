package jp.mihailskuzmins.sugoinihongoapp.extensions

import com.google.firebase.firestore.DocumentSnapshot

inline fun DocumentSnapshot.applyString(key: String, func: (x: String) -> Unit) =
	this.getString(key)?.let(func)

inline fun DocumentSnapshot.applyInt(key: String, func: (x: Int) -> Unit) =
	this.getInt(key)?.let(func)

inline fun DocumentSnapshot.applyBoolean(key: String, func: (x: Boolean) -> Unit) =
	this.getBoolean(key)?.let(func)

fun DocumentSnapshot.getInt(key: String) =
	this.getLong(key)?.toInt()