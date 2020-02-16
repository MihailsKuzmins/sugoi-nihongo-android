package jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.dataSaver

import com.google.firebase.firestore.CollectionReference
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.Keys

class NewDataSaver(data: Map<String, Any?>, userId: String) : DataSaver {

	private val mData: Map<String, Any?>

	init {
		val tempData = mutableMapOf<String, Any?>()
		tempData.putAll(data)
		tempData[Keys.userId] = userId

		mData = tempData.toMap()
	}

	override fun saveData(collectionReference: CollectionReference): String {
		val docReference = collectionReference.document()
		docReference.set(mData)

		return docReference.id
	}
}