package jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.dataSaver

import com.google.firebase.firestore.CollectionReference

interface DataSaver {

	fun saveData(collectionReference: CollectionReference): String
}