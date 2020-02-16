package jp.mihailskuzmins.sugoinihongoapp.features.word

import android.app.Application
import android.content.Context
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.formatDate
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItem
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemPieChart
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemSwitch
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemTextView
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.initAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasClipboardAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsDelete
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.getDataForSave
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToDeleteConfirm
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.AppConfirmDialog
import jp.mihailskuzmins.sugoinihongoapp.helpers.piechart.PieDataSetBuilder
import jp.mihailskuzmins.sugoinihongoapp.helpers.piechart.RelativeValueFormatter
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.ClipboardUtil
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.word.WordDetailModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.WordRepo
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.DefaultValues
import jp.mihailskuzmins.sugoinihongoapp.resources.FirestoreConstants.Keys.Words
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants
import jp.mihailskuzmins.sugoinihongoapp.userinterface.views.SectionHeader
import javax.inject.Inject

class WordDetailReadViewModel(app: Application, private val mWordId: String) :
	DetailViewModelBase(app), HasClipboardAction, SupportsDelete {

	@Inject lateinit var wordRepo : WordRepo
	@Inject lateinit var sharedPreferencesUtil: SharedPreferencesUtil
	@Inject override lateinit var clipboardUtil: ClipboardUtil

	private val mClipboardSetSubject: Subject<String> = PublishSubject.create()
	private val mNavBackSubject: Subject<Unit> = ReplaySubject.createWithSize(1)

	val wordIdItem = DetailItemTextView(app, R.string.word_word_id, false)
	val originalItem = DetailItemTextView(app, R.string.model_original)
	val translationItem = DetailItemTextView(app, R.string.model_translation)
	val transcriptionItem = DetailItemTextView(app, R.string.model_transcription)
	val notesItem = DetailItemTextView(app, R.string.word_notes)

	val isStudiableItem = DetailItemSwitch(app, R.string.settings_include_in_quiz, R.string.word_available_in_quiz)
		.apply { tag = Words.isStudiable }

	val isFavouriteItem = DetailItemSwitch(app, R.string.word_in_favourites, R.string.word_store_special_words)
		.apply { tag = Words.isFavourite }

	val markItem = DetailItemTextView(app, R.string.word_mark)
	val lastAccessedItem = DetailItemTextView(app, R.string.word_last_accessed)
	val accuracyItem = DetailItemPieChart(app, R.string.word_accuracy)

	val wordPropsSectionHeader = SectionHeader(app, R.string.word_properties)

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
			.subscribe { wordIdItem.isVisible = it }
			.addTo(cleanUp)

		subscribeToDeleteConfirm()
			.addTo(cleanUp)
	}

	override val clipboardValue: Observable<String>
		get() = mClipboardSetSubject

	override val navBackObservable: Observable<Unit>
		get() = mNavBackSubject

	override fun initDetailItems(): Array<DetailItem> =
		arrayOf(wordIdItem, originalItem, translationItem, transcriptionItem, notesItem,
			isStudiableItem, isFavouriteItem, markItem, lastAccessedItem, accuracyItem)

	override fun initDetailItemValues(onInit: initAction) {
		wordRepo.subscribeToDocument(mWordId) {
			wordIdItem.value = it.id
			originalItem.value = it.original
			translationItem.value = it.translation
			transcriptionItem.value = it.transcription
			notesItem.value = it.notes

			isStudiableItem.initialValue = it.isStudiable
			isFavouriteItem.initialValue = it.isFavourite ?: DefaultValues.boolean
			markItem.value = it.mark.toString()

			lastAccessedItem.value = if (it.dateLastAccessed == DefaultValues.date) context.getString(R.string.general_never)
			else it.dateLastAccessed.formatDate()

			accuracyItem.initChartData(createPieDataSet(it))
			createAccuracyItemCenterText(it)?.let { x -> accuracyItem.centerText = x }

			onInit()
		}.addTo(cleanUp)
	}

	fun saveProps() {
		arrayOf<DetailItem>(isStudiableItem, isFavouriteItem)
			.run(::getDataForSave)
			.invoke { wordRepo.saveSecondaryInfo(mWordId, it) }
	}

	private fun createPieDataSet(model: WordDetailModel): PieDataSet {
		val context: Context = getApplication()
		val timesCorrect = (model.timesCorrect ?: DefaultValues.int).toFloat()
		val timesAccessed = model.timesAccessed ?: DefaultValues.int
		val timesWrong = timesAccessed.toFloat() - timesCorrect

		val pieEntries = arrayOf(
			PieEntry(timesCorrect, context.getString(R.string.general_correct)),
			PieEntry(timesWrong, context.getString(R.string.general_wrong)))

		val colors = arrayOf(
			android.R.color.holo_green_dark,
			android.R.color.holo_red_light
		)

		return PieDataSetBuilder(context, pieEntries, colors)
			.setValueFormatter(RelativeValueFormatter(timesAccessed))
			.setValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE)
			.build()
	}

	override fun delete() {
		onCleared()
		wordRepo.deleteWord(mWordId)
	}

	override fun publishDelete() =
		mNavBackSubject.onNext(Unit)

	private fun createAccuracyItemCenterText(model: WordDetailModel): String? {
		if (model.timesAccessed == null)
			return null

		val timesCorrect = model.timesCorrect ?: DefaultValues.int
		val timesAccessed = model.timesAccessed!!.toFloat()

		val percentValue = timesCorrect / timesAccessed * 100F
		return "${"%.2f".format(percentValue)}%"
	}

	private fun setClipboardValue(value: String) = with(value) {
		setClipboard(this)
		mClipboardSetSubject.onNext(this)
	}
}