package jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import io.reactivex.disposables.Disposable
import jp.mihailskuzmins.sugoinihongoapp.extensions.*
import jp.mihailskuzmins.sugoinihongoapp.models.word.WordListModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.word.WordDetailModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.word.WordMarkModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.extensions.createWordListComparator
import jp.mihailskuzmins.sugoinihongoapp.resources.Action
import jp.mihailskuzmins.sugoinihongoapp.resources.ActionT
import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants.Models
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.DefaultValues
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.Keys.Words
import java.util.*

typealias WordsAction = (words: List<WordListModel>) -> Unit
typealias WordAction = (word: WordDetailModel) -> Unit
typealias WordMarksAction = (wordMarks: Map<Int, Int>) -> Unit

class WordRepo: FirestoreRepoBase() {

	private val mWordsCollection = FirestoreConstants.Collections.words
	private val mWordMandatoryProps = setOf(Words.dateLastAccessed, Words.isStudiable, Words.mark, Words.original, Words.translation)

	fun subscribeToCollection(hasLimit: Boolean = false, wordsAction: WordsAction): Disposable {
		val queryFunc = {x: Query -> x
			.orderBy(Words.dateCreated, Query.Direction.DESCENDING)
			.orderBy(Words.original)
			.runIf(hasLimit) { limit(25L) }}

		return addCollectionSnapshotListener(mWordsCollection, queryFunc) {
			it.asSequence()
				.sortedWith(createWordListComparator())
				.map(DocumentSnapshot::toListModel)
				.toList()
				.invoke(wordsAction)
		}
	}

	fun subscribeToMarksCollection(wordMarksAction: WordMarksAction) =
		addCollectionSnapshotListener(mWordsCollection) {
			it.asSequence()
				.map(DocumentSnapshot::toMarkModel)
				.groupingBy { x ->
					if (x.isStudiable) x.mark
						else Models.nonStudiableMark
				}.eachCount()
				.invoke(wordMarksAction)
		}

	fun subscribeToFavourites(wordsAction: WordsAction): Disposable {
		val propsQueryFunc = { x: Query -> x
			.whereEqualTo(Words.isFavourite, true) }

		return addCollectionSnapshotListener(mWordsCollection, propsQueryFunc) {
			it.map(DocumentSnapshot::toListModel)
				.invoke(wordsAction)
		}
	}

	fun getCollection(documentsAction: DocumentsAction) =
		getCollection(mWordsCollection, documentsAction = documentsAction)

	fun subscribeToDocument(wordId: String, wordAction: WordAction) =
		addDocumentSnapshotListener(mWordsCollection, wordId) {
			it.toDetailModel()
				.invoke(wordAction)
		}

	fun saveDocument(wordId: String, data: Map<String, Any?>, onSaved: Action, onError: ActionT<String>) {
		fun saveWord() {
			saveDocument(mWordsCollection, wordId, data, mWordMandatoryProps) {
				it[Words.dateCreated] = Date().toDate()
				it[Words.dateLastAccessed] = DefaultValues.date
				it[Words.isStudiable] = DefaultValues.isStudiable
				it[Words.mark] = DefaultValues.int
			}

			onSaved()
		}

		if (!data.containsKey(Words.original))
			saveWord()
		else {
			val checkQuery = { x: Query -> x
				.whereEqualTo(Words.original, data[Words.original])
				.limit(1)}

			getCollection(mWordsCollection, checkQuery) {
				if (it.isEmpty())
					saveWord()
				else
					onError(data[Words.original] as String)
			}
		}
	}

	fun saveSecondaryInfo(wordId: String, data: Map<String, Any?>) {
		if (wordId.isBlank())
			return

		saveDocument(mWordsCollection, wordId, data, mWordMandatoryProps)
	}

	fun deleteWord(wordId: String) =
		deleteDocument(mWordsCollection, wordId)

	fun updateMark(wordId: String, mark: Int, isCorrect: Boolean) {
		val canUpdateMark = if (isCorrect) mark < Models.wordMarkMax
		else mark > Models.wordMarkMin

		mutableMapOf(
			Words.dateLastAccessed to Date(),
			Words.timesAccessed to FieldValue.increment(1L))
			.applyIf(canUpdateMark) {
				val updatedMark = if (isCorrect) mark + 1
				else mark - 1

				put(Words.mark, updatedMark)
			}
			.applyIf(isCorrect) { put(Words.timesCorrect, FieldValue.increment(1L)) }
			.run { saveDocument(mWordsCollection, wordId, this, mWordMandatoryProps) }
	}

	fun decrementMarks(decrementDays: Int, action: Action) {
		val decrementDate = Date().toDate()
			.add(Calendar.DAY_OF_YEAR, -decrementDays)

		// updateDate should not exceed today's date
		val updatedDate = decrementDate
			.add(Calendar.DAY_OF_YEAR, minOf(decrementDays, 5))

		val query = { x: Query -> x
			.whereLessThan(Words.dateLastAccessed, decrementDate)
			.whereGreaterThan(Words.dateLastAccessed, DefaultValues.date) }

		getCollection(mWordsCollection, query) {
			it.asSequence()
				// Firestore does not allow filtering on multiple fields
				.filter { x -> x.getInt(Words.mark)!! > Models.wordMarkMin }
				.forEach { x ->
					val data = mapOf(
						Words.mark to FieldValue.increment(-1L),
						Words.dateLastAccessed to updatedDate)

					saveDocument(mWordsCollection, x.id, data, mWordMandatoryProps)
				}

			action()
		}
	}
}

private fun DocumentSnapshot.toListModel() =
	WordListModel(
		id,
		getString(Words.original)!!,
		getString(Words.translation)!!,
		getString(Words.transcription).orEmpty(),
		getInt(Words.mark)!!,
		getBoolean(Words.isStudiable)!!)

private fun DocumentSnapshot.toDetailModel() =
	WordDetailModel(this.id).apply {
		original = this@toDetailModel.getString(Words.original)!!
		translation = this@toDetailModel.getString(Words.translation)!!
		this@toDetailModel.applyString(Words.transcription) { transcription = it }
		this@toDetailModel.applyString(Words.notes) { notes = it }

		dateLastAccessed = this@toDetailModel.getDate(Words.dateLastAccessed)!!
		this@toDetailModel.applyBoolean(Words.isFavourite) { isFavourite = it }
		isStudiable = this@toDetailModel.getBoolean(Words.isStudiable)!!
		mark = this@toDetailModel.getInt(Words.mark)!!
		this@toDetailModel.applyInt(Words.timesAccessed) { timesAccessed = it }
		this@toDetailModel.applyInt(Words.timesCorrect) { timesCorrect = it }
	}

private fun DocumentSnapshot.toMarkModel() =
	WordMarkModel().apply {
		this@toMarkModel.applyInt(Words.mark) { mark = it }
		this@toMarkModel.applyBoolean(Words.isStudiable) { isStudiable = it }
	}