package jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import io.reactivex.disposables.Disposable
import jp.mihailskuzmins.sugoinihongoapp.extensions.runIf
import jp.mihailskuzmins.sugoinihongoapp.extensions.toDate
import jp.mihailskuzmins.sugoinihongoapp.extensions.toLowerCaseEx
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.grammarrule.GrammarRuleDetailModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.grammarrule.GrammarRuleListModel
import jp.mihailskuzmins.sugoinihongoapp.resources.Action
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.Collections
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.Keys.GrammarRules
import timber.log.Timber
import java.util.*

typealias GrammarRulesAction = (List<GrammarRuleListModel>) -> Unit
typealias GrammarRuleAction = (GrammarRuleDetailModel) -> Unit

class GrammarRuleRepo: FirestoreRepoBase() {

	private val mCollection = Collections.grammarRules

	private val mGrammarRuleMandatoryProps = setOf(GrammarRules.body, GrammarRules.header)

	fun subscribeToCollection(hasLimit: Boolean = false, grammarRulesAction: GrammarRulesAction): Disposable {
		val query = {x: Query -> x
			.orderBy(GrammarRules.dateCreated, Query.Direction.DESCENDING)
			.orderBy(GrammarRules.header)
			.runIf(hasLimit) { limit(25) }}

		return addCollectionSnapshotListener(mCollection, query) {
			val comparator = compareByDescending<DocumentSnapshot> { x -> x.getDate(GrammarRules.dateCreated) }
				.thenBy { x -> x.getString(GrammarRules.header)?.toLowerCaseEx() }

			it.asSequence()
				.sortedWith(comparator)
				.map { x -> x.toListModel() }
				.toList()
				.run(grammarRulesAction)
		}
	}

	fun getCollection(documentsAction: DocumentsAction) =
		getCollection(mCollection, documentsAction = documentsAction)

	fun subscribeToDocument(grammarRuleId: String, grammarRuleAction: GrammarRuleAction) =
		addDocumentSnapshotListener(mCollection, grammarRuleId) {
			grammarRuleAction(it.toDetailModel())
		}

	fun saveDocument(grammarRuleId: String, data: Map<String, Any?>, onSaved: Action) {
		saveDocument(mCollection, grammarRuleId, data, mGrammarRuleMandatoryProps) { it[GrammarRules.dateCreated] = Date().toDate() }
		onSaved()
	}

	fun deleteGrammarRule(grammarRuleId: String) {
		deleteDocument(mCollection, grammarRuleId)
		Timber.i("GrammarRule deleted: $grammarRuleId")
	}
}

private fun DocumentSnapshot.toListModel() =
	GrammarRuleListModel(this.id).apply {
		header = this@toListModel.getString(GrammarRules.header)!!
		body = this@toListModel.getString(GrammarRules.body)!!
	}

private fun DocumentSnapshot.toDetailModel() =
	GrammarRuleDetailModel(this.id).apply {
		header = this@toDetailModel.getString(GrammarRules.header)!!
		body = this@toDetailModel.getString(GrammarRules.body)!!
	}