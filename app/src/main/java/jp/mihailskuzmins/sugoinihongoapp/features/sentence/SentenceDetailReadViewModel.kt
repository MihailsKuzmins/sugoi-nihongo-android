package jp.mihailskuzmins.sugoinihongoapp.features.sentence

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
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.SentenceRepo
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants
import javax.inject.Inject

class SentenceDetailReadViewModel(
		app: Application,
		private val mSentenceId: String) :
	DetailViewModelBase(app), HasClipboardAction, SupportsDelete {

	@Inject lateinit var sentenceRepo : SentenceRepo
	@Inject lateinit var sharedPreferencesUtil: SharedPreferencesUtil
	@Inject override lateinit var clipboardUtil: ClipboardUtil

	private val mClipboardSetSubject: Subject<String> = PublishSubject.create()
	private val mNavBackSubject: Subject<Unit> = ReplaySubject.createWithSize(1)

	val sentenceIdItem = DetailItemTextView(app, R.string.sentence_sentence_id, false)
	val originalItem = DetailItemTextView(app, R.string.model_original)
	val translationItem = DetailItemTextView(app, R.string.model_translation)
	val transcriptionItem = DetailItemTextView(app, R.string.model_transcription)

	override val deleteConfirmDialog = AppConfirmDialog()

	init {
		originalItem.onLongClickAction = { setClipboardValue(originalItem.value) }
		translationItem.onLongClickAction = { setClipboardValue(translationItem.value) }
		transcriptionItem.onLongClickAction = { setClipboardValue(transcriptionItem.value) }
	}

	init {
		sharedPreferencesUtil
			.observeBool(PreferencesConstants.Keys.uiShowEntryId)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { sentenceIdItem.isVisible = it }
			.addTo(cleanUp)

		subscribeToDeleteConfirm()
			.addTo(cleanUp)
	}

	override val clipboardValue: Observable<String>
		get() = mClipboardSetSubject

	override val navBackObservable: Observable<Unit>
		get() = mNavBackSubject

	override fun initDetailItems(): Array<DetailItem> =
		arrayOf(sentenceIdItem, originalItem, translationItem, transcriptionItem)

	override fun initDetailItemValues(onInit: initAction) {
		sentenceRepo.subscribeToDocument(mSentenceId) {
			sentenceIdItem.value = it.id
			originalItem.value = it.original
			translationItem.value = it.translation
			transcriptionItem.value = it.transcription

			onInit()
		}.addTo(cleanUp)
	}

	override fun delete() {
		onCleared()
		sentenceRepo.deleteSentence(mSentenceId)
	}

	override fun publishDelete() =
		mNavBackSubject.onNext(Unit)

	private fun setClipboardValue(value: String) = with(value) {
		setClipboard(this)
		mClipboardSetSubject.onNext(this)
	}
}