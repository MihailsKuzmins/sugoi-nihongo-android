package jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.extensions

import com.google.firebase.firestore.DocumentSnapshot
import jp.mihailskuzmins.sugoinihongoapp.extensions.toLowerCaseEx
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants
import java.util.*

fun createWordListComparator(): Comparator<DocumentSnapshot> =
	compareByDescending<DocumentSnapshot> { x -> x.getDate(FirestoreConstants.Keys.Words.dateCreated) }
		.thenBy { x -> x.getString(FirestoreConstants.Keys.Words.original)?.toLowerCaseEx() }