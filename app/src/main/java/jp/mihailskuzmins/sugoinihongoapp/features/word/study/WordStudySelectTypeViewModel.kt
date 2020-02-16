package jp.mihailskuzmins.sugoinihongoapp.features.word.study

import android.app.Application
import android.content.Context
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.navigation.NavDirections
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.features.base.ViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsNavDirections
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsNavigation
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.BindableProp
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordQuizType
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.word.WordMarkModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.word.getMarkState
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.miscellaneous.WordMarkTableModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.miscellaneous.WordMarkTableRowModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.miscellaneous.WordMarkTableTotalModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.WordRepo
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.Keys
import jp.mihailskuzmins.sugoinihongoapp.userinterface.views.ButtonMessage
import javax.inject.Inject

class WordStudySelectTypeViewModel(app: Application) : ViewModelBase(app), SupportsNavigation, SupportsNavDirections {

	private val mMarkStateMapSubject: Subject<Map<WordMarkModel.MarkState, Int>> = ReplaySubject.createWithSize(1)
	private val mMarkListSubject: Subject<List<WordMarkTableModel>> = ReplaySubject.createWithSize(1)
	private val mNavActionIdSubject: Subject<Int> = PublishSubject.create()
	private val mNavDirectionsSubject: Subject<NavDirections> = PublishSubject.create()

	@Inject lateinit var wordRepo: WordRepo
	@Inject lateinit var sharedPreferencesUtil: SharedPreferencesUtil

	val findWordButton = ButtonMessage(app, R.string.word_quiz_find_word, { publishFindRecogniseWordNav(
		WordQuizType.FindWord) })
	val recogniseWordButton = ButtonMessage(app, R.string.word_quiz_recognise_word, { publishFindRecogniseWordNav(
		WordQuizType.RecogniseWord) })
	val listOfWordsButton = ButtonMessage(app, R.string.word_quiz_list_of_words, { mNavActionIdSubject.onNext(R.id.wordQuizSelectType_to_wordQuizListOfWords) }, R.string.word_quiz_cannot_start_list_of_words)

	override val navActionIdObservable: Observable<Int>
		get() = mNavActionIdSubject.distinctUntilChanged()

	override val navDirectionsObservable: Observable<NavDirections>
		get() = mNavDirectionsSubject.distinctUntilChanged()

	var isMarkTableVisible by BindableProp(BR.markTableVisible, false)
		@Bindable get
		private set

	init {
		wordRepo.subscribeToMarksCollection {
			it.entries
				.groupBy { x -> x.key.getMarkState() }
				.mapValues { x -> x.value.sumBy(Map.Entry<Int, Int>::value) }
				.invoke(mMarkStateMapSubject::onNext)

			it.entries.asSequence()
				.map { x -> WordMarkTableRowModel(x.key, x.value) }
				.sortedBy(WordMarkTableRowModel::mark)
				.toMutableList<WordMarkTableModel>()
				.apply {
					it.values
						.sum()
						.run { add(WordMarkTableTotalModel(this)) }
				}.invoke(mMarkListSubject::onNext)

			isInitialisedSubject.onNext(true)
		}.addTo(cleanUp)

		val referToSettingsMessage = createCannotStartQuizMessage(app)
		val addMoreWordsMessage = createAddMoreWordsMessage(app)

		val maxOptionsChangedObservable = sharedPreferencesUtil
			.observeInt(Keys.wordQuizMaxOptionCount)
			.publish()

		val itemCountObservable = mMarkStateMapSubject
			.map {
				it.asSequence()
					.filter { x -> x.key != WordMarkModel.MarkState.Excluded }
					.sumBy { x -> x.value }
			}
			.distinctUntilChanged()
			.publish()

		val hasItemsObservable = itemCountObservable
			.map { it > 0 }
			.distinctUntilChanged()
			.publish()

		// Quiz buttons
		maxOptionsChangedObservable
			.map {
				StringBuilder(app.getString(R.string.word_quiz_cannot_start_quiz, it))
					.appendln()
					.append(referToSettingsMessage)
					.append(addMoreWordsMessage)
					.toString()
			}.subscribe { recogniseWordButton.message = it }
			.addTo(cleanUp)

		Observables
			.combineLatest(
				itemCountObservable,
				maxOptionsChangedObservable)
				{ x: Int, y: Int -> x >= y }
			.distinctUntilChanged()
			.subscribe {
				findWordButton.isEnabled = it
				recogniseWordButton.isEnabled = it
				recogniseWordButton.isMessageVisible = !it
			}.addTo(cleanUp)

		hasItemsObservable
			.subscribe {
				listOfWordsButton.isEnabled = it
				listOfWordsButton.isMessageVisible = !it
			}.addTo(cleanUp)

		// Mark table
		Observables
			.combineLatest(
				hasItemsObservable,
				sharedPreferencesUtil.observeBool(Keys.uiShowWordMarkTable))
				{ x: Boolean, y: Boolean -> x && y }
			.distinctUntilChanged()
			.subscribe { isMarkTableVisible = it }
			.addTo(cleanUp)

		hasItemsObservable
			.connect()
			.addTo(cleanUp)

		itemCountObservable
			.connect()
			.addTo(cleanUp)

		maxOptionsChangedObservable
			.connect()
			.addTo(cleanUp)
	}

	val markStateMapObservable: Observable<Map<WordMarkModel.MarkState, Int>>
		get() = mMarkStateMapSubject.distinctUntilChanged()

	val markMapObservable: Observable<List<WordMarkTableModel>>
		get() = mMarkListSubject.distinctUntilChanged()

	private fun createCannotStartQuizMessage(context: Context): String {
		val settingsNavigation = listOf(R.string.word_quiz, R.string.settings_word_quiz_max_option_count)
			.joinToString(separator = " -> ") { context.getString(it) }

		return context.getString(R.string.settings_refer_to_settings, settingsNavigation)
	}

	private fun createAddMoreWordsMessage(context: Context): String {
		val text = context.getString(R.string.word_or_add_more_words)
		return "; $text"
	}

	private fun publishFindRecogniseWordNav(quizType: WordQuizType) =
		WordStudySelectTypeFragmentDirections
			.wordQuizSelectTypeToWordQuizFindRecogniseWord(quizType)
			.invoke(mNavDirectionsSubject::onNext)
}