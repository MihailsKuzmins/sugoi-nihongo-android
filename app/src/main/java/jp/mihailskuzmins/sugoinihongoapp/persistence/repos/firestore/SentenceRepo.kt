package jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import io.reactivex.disposables.Disposable
import jp.mihailskuzmins.sugoinihongoapp.extensions.applyString
import jp.mihailskuzmins.sugoinihongoapp.extensions.runIf
import jp.mihailskuzmins.sugoinihongoapp.extensions.toDate
import jp.mihailskuzmins.sugoinihongoapp.extensions.toLowerCaseEx
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.sentence.SentenceDetailModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.sentence.SentenceListModel
import jp.mihailskuzmins.sugoinihongoapp.resources.Action
import jp.mihailskuzmins.sugoinihongoapp.resources.ActionT
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.Keys.Sentences
import java.util.*

typealias SentencesAction = (sentences: List<SentenceListModel>) -> Unit
typealias SentenceAction = (sentence: SentenceDetailModel) -> Unit

class SentenceRepo : FirestoreRepoBase() {

	private val mCollection = FirestoreConstants.Collections.sentences
	private val mSentenceMandatoryProps = setOf(Sentences.original, Sentences.translation)

	fun subscribeToCollection(hasLimit: Boolean = false, sentencesAction: SentencesAction): Disposable {
		val query = {x: Query -> x
			.orderBy(Sentences.dateCreated, Query.Direction.DESCENDING)
			.orderBy(Sentences.original)
			.runIf(hasLimit) { limit(25) }}

		return addCollectionSnapshotListener(mCollection, query) {
			val comparator = compareByDescending<DocumentSnapshot> { x -> x.getDate(Sentences.dateCreated) }
				.thenBy { x -> x.getString(Sentences.original)?.toLowerCaseEx() }

			it.asSequence()
				.sortedWith(comparator)
				.map { x -> x.toListModel() }
				.toList()
				.run(sentencesAction)
		}
	}

	fun getCollection(documentsAction: DocumentsAction) =
		getCollection(mCollection, documentsAction = documentsAction)

	fun subscribeToDocument(sentenceId: String, sentenceAction: SentenceAction) =
		addDocumentSnapshotListener(mCollection, sentenceId) {
			sentenceAction(it.toDetailModel())
		}

	fun saveDocument(sentenceId: String, data: Map<String, Any?>, onSaved: Action, onError: ActionT<String>) {
		fun saveSentence() {
			saveDocument(mCollection, sentenceId, data, mSentenceMandatoryProps) { it[Sentences.dateCreated] = Date().toDate() }
			onSaved()
		}

		if (!data.containsKey(Sentences.original))
			saveSentence()
		else {
			val checkQuery = {x: Query -> x
				.whereEqualTo(Sentences.original, data[Sentences.original])}

			getCollection(mCollection, checkQuery) {
				if (it.isEmpty())
					saveSentence()
				else
					onError(data[Sentences.original] as String)
			}
		}
	}

	fun deleteSentence(sentenceId: String) =
		deleteDocument(mCollection, sentenceId)
}

private fun DocumentSnapshot.toListModel() =
	SentenceListModel(this.id).apply {
		original = this@toListModel.getString(Sentences.original)!!
		translation = this@toListModel.getString(Sentences.translation)!!
	}

private fun DocumentSnapshot.toDetailModel() =
	SentenceDetailModel(
		this.id
	).apply {
		original = this@toDetailModel.getString(Sentences.original)!!
		translation = this@toDetailModel.getString(Sentences.translation)!!
		this@toDetailModel.applyString(Sentences.transcription) {transcription = it}
	}