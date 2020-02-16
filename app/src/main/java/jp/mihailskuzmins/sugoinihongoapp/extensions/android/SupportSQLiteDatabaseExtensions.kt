package jp.mihailskuzmins.sugoinihongoapp.extensions.android

import androidx.sqlite.db.SupportSQLiteDatabase

fun SupportSQLiteDatabase.execAll(vararg queries: String) {
	queries.forEach {
		execSQL(it)
	}
}