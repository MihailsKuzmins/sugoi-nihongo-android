package jp.mihailskuzmins.sugoinihongoapp.features.sentence

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
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.SentenceRepo
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.Keys.Sentences
import javax.inject.Inject

class SentenceDetailEditViewModel(
		app: Application,
		private val mSentenceId: String) :
	DetailViewModelBase(app), SupportsNavigationBack {

	private val mSentenceMaxLength = 100
	private val mNavBackSubject: Subject<Unit> = ReplaySubject.createWithSize(1)

	@Inject
	lateinit var sentenceRepo: SentenceRepo

	val originalItem = DetailItemEditText(app, R.string.model_original, maxLength = mSentenceMaxLength)
		.apply {
			addRules(NotBlankRule(), NotKanaOrKanjiRule())
			tag = Sentences.original
			hasFocus = true
		}

	val translationItem = DetailItemEditText(app, R.string.model_translation, maxLength = mSentenceMaxLength)
		.apply {
			addRules(NotBlankRule(), KanaOrKanjiRule(true))
			tag = Sentences.translation
		}

	val transcriptionItem = DetailItemEditText(app, R.string.model_transcription, maxLength = mSentenceMaxLength)
		.apply {
			addRules(NotBlankRule(), KanaRule(true))
			tag = Sentences.transcription
		}

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
		arrayOf(originalItem, translationItem, transcriptionItem)

	override fun initDetailItemValues(onInit: initAction) {
		if (mSentenceId.isBlank())
			onInit().run { return@initDetailItemValues }

		sentenceRepo.subscribeToDocument(mSentenceId) {
			originalItem.initialValue = it.original
			translationItem.initialValue = it.translation
			transcriptionItem.initialValue = it.transcription

			onInit()
		}.addTo(cleanUp)
	}

	fun save() {
		sentenceRepo.saveDocument(mSentenceId, getDataForSave(detailItems), ::publishNavBack, ::publishErrorAlert)
	}

	private fun publishNavBack() =
		mNavBackSubject.onNext(Unit)

	private fun publishErrorAlert(sentenceOriginal: String) {
		AlertConfig()
			.apply {
				title = context.getString(R.string.general_attention)
				message = context.getString(R.string.sentence_the_same_sentence_already_exists, sentenceOriginal)
			}.invoke { onSaveErrorAlertDialog.config = it }
	}

}