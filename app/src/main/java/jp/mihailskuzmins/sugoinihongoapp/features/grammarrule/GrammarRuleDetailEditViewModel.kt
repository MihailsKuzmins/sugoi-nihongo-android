package jp.mihailskuzmins.sugoinihongoapp.features.grammarrule

import android.app.Application
import android.text.InputType
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItem
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemEditText
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.initAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.NotBlankRule
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsNavigationBack
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.getDataForSave
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.GrammarRuleRepo
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.Keys.GrammarRules
import javax.inject.Inject

class GrammarRuleDetailEditViewModel(
		app: Application,
		private val mGrammarRuleId: String) :
	DetailViewModelBase(app), SupportsNavigationBack {

	private val mNavBackSubject: Subject<Unit> = ReplaySubject.createWithSize(1)

	@Inject
	lateinit var grammarRuleRepo: GrammarRuleRepo

	val headerItem = DetailItemEditText(app, R.string.grammar_rule_header, InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, maxLength = 64)
		.apply {
			addRule(NotBlankRule())
			tag = GrammarRules.header
			hasFocus = true
		}

	val bodyItem = DetailItemEditText(app, R.string.grammar_rule_body, maxLength = 4096)
		.apply {
			addRule(NotBlankRule())
			tag = GrammarRules.body
		}

	override val navBackObservable: Observable<Unit>
		get() = mNavBackSubject

	override fun initDetailItems(): Array<DetailItem> =
		arrayOf(headerItem, bodyItem)

	override fun initDetailItemValues(onInit: initAction) {
		if (mGrammarRuleId.isBlank())
			onInit().run { return@initDetailItemValues }

		grammarRuleRepo.subscribeToDocument(mGrammarRuleId) {
			headerItem.initialValue = it.header
			bodyItem.initialValue = it.body

			onInit()
		}.addTo(cleanUp)
	}

	fun save() {
		grammarRuleRepo.saveDocument(mGrammarRuleId, getDataForSave(detailItems), ::publishNavBack)
	}

	private fun publishNavBack() =
		mNavBackSubject.onNext(Unit)
}