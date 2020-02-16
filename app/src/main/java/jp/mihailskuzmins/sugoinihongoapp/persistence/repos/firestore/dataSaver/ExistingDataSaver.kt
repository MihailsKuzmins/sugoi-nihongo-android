package jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.dataSaver

import com.google.firebase.firestore.CollectionReference

class ExistingDataSaver(private val mId: String, private val mData: Map<String, Any?>) : DataSaver {

	override fun saveData(collectionReference: CollectionReference): String {
		if (mData.isNotEmpty())
			collectionReference.document(mId).update(mData)

		return mId
	}
}