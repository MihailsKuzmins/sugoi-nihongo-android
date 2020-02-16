package jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.reactivex.disposables.Disposable
import jp.mihailskuzmins.sugoinihongoapp.extensions.applyIf
import jp.mihailskuzmins.sugoinihongoapp.helpers.AnonymousDisposable
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.dataSaver.ExistingDataSaver
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.dataSaver.NewDataSaver
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.DefaultValues
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.Keys
import timber.log.Timber
import java.util.*

typealias DocumentsAction = (documents: List<DocumentSnapshot>) -> Unit
typealias DocumentAction = (document: DocumentSnapshot) -> Unit
typealias OnSetAction = (data: MutableMap<String, Any?>) -> Unit
typealias QueryFunc = (query: Query) -> Query?

abstract class FirestoreRepoBase {

	private val mDatabase by lazy { FirebaseFirestore.getInstance() }
	private val mAuth by lazy { FirebaseAuth.getInstance() }

	private val mUserId: String
		get() = mAuth.currentUser!!.uid

	protected fun addCollectionSnapshotListener(collection: String, queryFunc: QueryFunc? = null, documentsAction: DocumentsAction): Disposable {
		val listenerRegistration = createCollectionQuery(collection, queryFunc)
			.addSnapshotListener { querySnapshot, ex ->
				ex?.let {
					Timber.e(it)
					documentsAction(emptyList())
					return@addSnapshotListener
				}

				documentsAction(querySnapshot!!.documents)
			}

		return AnonymousDisposable { listenerRegistration.remove() }
	}

	protected fun getCollection(collection: String, queryFunc: QueryFunc? = null, documentsAction: DocumentsAction) {
		createCollectionQuery(collection, queryFunc)
			.get()
			.addOnCompleteListener { task ->
				task.exception?.let {
					Timber.e(it)
					documentsAction(emptyList())
					return@addOnCompleteListener
				}

				task.result?.let {
					documentsAction(it.documents)
					return@addOnCompleteListener
				}

				Timber.e("No such document")
				documentsAction(emptyList())
			}
	}

	protected fun addDocumentSnapshotListener(collection: String, id: String, documentAction: DocumentAction): Disposable {
		val listenerRegistration = mDatabase
			.collection(collection)
			.document(id)
			.addSnapshotListener { documentSnapshot, ex ->
				ex?.let {
					Timber.e(it)
					return@addSnapshotListener
				}

				documentSnapshot?.let {
					// Null - when deleted
					if (it.data != null)
						documentAction(it)
				}
			}

		return AnonymousDisposable { listenerRegistration.remove() }
	}

	protected fun saveDocument(collection: String, id: String, data: Map<String, Any?>, mandatoryProps: Set<String>? = null, onSetAction: OnSetAction? = null): String {
		val isNewDocument = id.isBlank()

		val saveData = createSaveData(data, isNewDocument, mandatoryProps, onSetAction)

		val dataSaver = if (isNewDocument) NewDataSaver(saveData, mUserId)
			else ExistingDataSaver(id, saveData)

		val collectionRef = mDatabase.collection(collection)
		return dataSaver.saveData(collectionRef)
	}

	protected fun deleteDocument(collection: String, id: String) {
		mDatabase.collection(collection)
			.document(id)
			.delete()
	}

	private fun createCollectionQuery(collection: String, queryFunc: QueryFunc?): Query {
		val collectionRef = mDatabase.collection(collection)
		val ref =  queryFunc?.invoke(collectionRef) ?: collectionRef

		return ref.whereEqualTo(Keys.userId, mUserId)
	}

	internal fun createSaveData(data: Map<String, Any?>, isNewDocument: Boolean, mandatoryProps: Set<String>?, onSetAction: OnSetAction?): MutableMap<String, Any?> {
		fun getStringValue(value: String): Any? =
			if (value.isBlank()) if (isNewDocument) null else FieldValue.delete()
			else value.trim()

		fun <T: Any?> getFirestoreValue(value: T, defaultValue: T): Any? =
			if (value == defaultValue) if (isNewDocument) null else FieldValue.delete()
			else value

		fun mapValue(value: Any?): Any? = when (value) {
			is String -> getStringValue(value)
			is Boolean -> getFirestoreValue(value, DefaultValues.boolean)
			is Int -> getFirestoreValue(value, DefaultValues.int)
			is Double -> getFirestoreValue(value, DefaultValues.double)
			is Date -> getFirestoreValue(value, DefaultValues.date)
			else -> getFirestoreValue(value, null)
		}

		val hasMandatoryProps = !mandatoryProps.isNullOrEmpty()

		return data
			.mapValues {
				val value = it.value

				if (hasMandatoryProps && it.key in mandatoryProps!!)
					if (value is String) value.trim() else value
				else
					mapValue(value)
			}
			.filter { (hasMandatoryProps && it.key in mandatoryProps!!) || it.value != null }
			.toMutableMap()
			.applyIf(isNewDocument) { onSetAction?.invoke(this) }
	}
}