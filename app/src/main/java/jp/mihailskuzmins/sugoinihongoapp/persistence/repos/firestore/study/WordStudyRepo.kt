package jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.study

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import jp.mihailskuzmins.sugoinihongoapp.extensions.getInt
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordStudyModel
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordStudyQuizModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.FirestoreRepoBase
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.Collections
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.Keys.Words

typealias WordQuizQuestionsAction = (wordIds: Sequence<WordStudyQuizModel>, allWords: List<WordStudyModel>) -> Unit


class WordStudyRepo: FirestoreRepoBase() {

	private val mWordsCollection = Collections.words

	fun getQuizFindRecogniseWord(limit: Long, wordQuizQuestionsAction: WordQuizQuestionsAction) {
		val comparator = compareBy<DocumentSnapshot> { x -> x.getInt(Words.mark)!! }
			.thenBy { x -> x.getDate(Words.dateLastAccessed)!! }

		getCollection(mWordsCollection) {
			val allWords = it
				.map(DocumentSnapshot::toWordModel)

			val wordIds = it
				.asSequence()
				.filter { x -> x.getBoolean(Words.isStudiable)!! }
				.sortedWith(comparator)
				.take(limit.toInt())
				.map(DocumentSnapshot::toWordStudyQuizModel)

			wordQuizQuestionsAction(wordIds, allWords)
		}

	}

	fun getQuizListOfWords(limit: Long, wordsAction: (List<WordStudyModel>) -> Unit) {
		val query = { x: Query -> x
			.whereEqualTo(Words.isStudiable, true)
			.orderBy(Words.mark)
			.orderBy(Words.dateLastAccessed)
			.limit(limit) }

		getCollection(mWordsCollection, query) {
			it.map(DocumentSnapshot::toWordModel)
				.invoke(wordsAction)
		}
	}
}

private fun DocumentSnapshot.toWordModel() =
	WordStudyModel(
		id,
		getString(Words.original)!!,
		getString(Words.translation)!!,
		getString(Words.transcription).orEmpty()
	)

private fun DocumentSnapshot.toWordStudyQuizModel() =
	WordStudyQuizModel(
		id,
		getInt(Words.mark)!!
	)