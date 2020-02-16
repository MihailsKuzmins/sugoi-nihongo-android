package jp.mihailskuzmins.sugoinihongoapp.features.grammarrule

import android.app.Application
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItem
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemTextView
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.initAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasClipboardAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsDelete
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToDeleteConfirm
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.AppConfirmDialog
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.ClipboardUtil
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.GrammarRuleRepo
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants
import javax.inject.Inject

class GrammarRuleDetailReadViewModel(
		app: Application,
		private val mGrammarRuleId: String) :
	DetailViewModelBase(app), HasClipboardAction, SupportsDelete {

	@Inject lateinit var grammarRuleRepo: GrammarRuleRepo
	@Inject lateinit var sharedPreferencesUtil: SharedPreferencesUtil
	@Inject override lateinit var clipboardUtil: ClipboardUtil

	private val mClipboardSetSubject: Subject<String> = PublishSubject.create()
	private val mNavBackSubject: Subject<Unit> = ReplaySubject.createWithSize(1)

	val grammarRuleIdItem = DetailItemTextView(app, R.string.grammar_rule_grammar_rule_id, false)
	val headerItem = DetailItemTextView(app, R.string.grammar_rule_header)
	val bodyItem = DetailItemTextView(app, R.string.grammar_rule_body)

	override val deleteConfirmDialog = AppConfirmDialog()

	init {
		headerItem.onLongClickAction = { setClipboardValue(headerItem.value) }
	}

	init {
		sharedPreferencesUtil
			.observeBool(PreferencesConstants.Keys.uiShowEntryId)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { grammarRuleIdItem.isVisible = it }
			.addTo(cleanUp)

		subscribeToDeleteConfirm()
			.addTo(cleanUp)
	}

	override val clipboardValue: Observable<String>
		get() = mClipboardSetSubject

	override val navBackObservable: Observable<Unit>
		get() = mNavBackSubject

	override fun initDetailItems(): Array<DetailItem> =
		arrayOf(grammarRuleIdItem, headerItem, bodyItem)

	override fun initDetailItemValues(onInit: initAction) {
		grammarRuleRepo.subscribeToDocument(mGrammarRuleId) {
			grammarRuleIdItem.value = it.id
			headerItem.value = it.header
			bodyItem.value = it.body

			onInit()
		}.addTo(cleanUp)
	}

	override fun delete() {
		onCleared()
		grammarRuleRepo.deleteGrammarRule(mGrammarRuleId)
	}

	override fun publishDelete() =
		mNavBackSubject.onNext(Unit)

	private fun setClipboardValue(value: String) = with(value) {
		setClipboard(this)
		mClipboardSetSubject.onNext(this)
	}
}