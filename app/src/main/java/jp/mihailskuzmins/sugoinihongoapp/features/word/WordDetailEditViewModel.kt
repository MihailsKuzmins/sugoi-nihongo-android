package jp.mihailskuzmins.sugoinihongoapp.features.word

import android.app.Application
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItem
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemEditText
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.initAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.KanaOrKanjiRule
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.KanaRule
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.NotBlankRule
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.rules.NotKanaOrKanjiRule
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsNavigationBack
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.getDataForSave
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.AppAlertDialog
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.configs.AlertConfig
import jp.mihailskuzmins.sugoinihongoapp.helpers.japanese.KanaKanjiDetector
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.WordRepo
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.Keys.Words
import javax.inject.Inject

class WordDetailEditViewModel(
		app: Application,
		private val mWordId: String) :
	DetailViewModelBase(app), SupportsNavigationBack {

	private val mNavBackSubject: Subject<Unit> = ReplaySubject.createWithSize(1)

	@Inject
	lateinit var wordRepo: WordRepo

	val originalItem = DetailItemEditText(app, R.string.model_original, maxLength = 32)
		.apply {
			addRules(NotBlankRule(), NotKanaOrKanjiRule())
			tag = Words.original
			hasFocus = true
		}

	val translationItem = DetailItemEditText(app, R.string.model_translation,  maxLength = 16)
		.apply {
			addRule(NotBlankRule())
			addRule(KanaOrKanjiRule())
			tag = Words.translation
		}

	val transcriptionItem = DetailItemEditText(app, R.string.model_transcription,  maxLength = 16)
		.apply {
			addRule(NotBlankRule())
			addRule(KanaRule())
			tag = Words.transcription
		}

	val notesItem = DetailItemEditText(app, R.string.word_notes, maxLength = 100)
		.apply { tag = Words.notes }

	val onSaveErrorAlertDialog = AppAlertDialog()

	init {
		translationItem.valueChanged
			.map(KanaKanjiDetector::hasKanji)
			.distinctUntilChanged()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { transcriptionItem.isEnabled = it }
			.addTo(cleanUp)
	}

	override val navBackObservable: Observable<Unit>
		get() = mNavBackSubject

	override fun initDetailItems(): Array<DetailItem> =
		arrayOf(originalItem, translationItem, transcriptionItem, notesItem)

	override fun initDetailItemValues(onInit: initAction) {
		if (mWordId.isBlank())
			onInit().run { return@initDetailItemValues }

		wordRepo.subscribeToDocument(mWordId) {
			originalItem.initialValue = it.original
			translationItem.initialValue = it.translation
			transcriptionItem.initialValue = it.transcription
			notesItem.initialValue = it.notes

			onInit()
		}.addTo(cleanUp)
	}

	fun save() {
		wordRepo.saveDocument(mWordId, getDataForSave(detailItems), ::publishNavBack, ::publishErrorAlert)
	}

	private fun publishNavBack() =
		mNavBackSubject.onNext(Unit)

	private fun publishErrorAlert(wordOriginal: String) =
		AlertConfig()
			.apply {
				title = context.getString(R.string.general_attention)
				message = context.getString(R.string.word_the_same_word_already_exists, wordOriginal)
			}.invoke { onSaveErrorAlertDialog.config = it }
}