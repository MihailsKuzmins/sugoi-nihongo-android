package jp.mihailskuzmins.sugoinihongoapp.features.word.study

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.navigation.NavDirections
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.databinding.DialogWordStudyWordBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.extensions.runIf
import jp.mihailskuzmins.sugoinihongoapp.features.base.ViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsNavDirections
import jp.mihailskuzmins.sugoinihongoapp.helpers.Box
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.BindableProp
import jp.mihailskuzmins.sugoinihongoapp.helpers.delegatedprops.ReactiveProp
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.AppAlertViewDialog
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.*
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.Word
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.createJapaneseTextWithBrackets
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.createJapaneseTextWithNewLine
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.room.WordQuizQuestionResultEntity
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.WordRepo
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.study.WordStudyRepo
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.room.WordQuizRepo
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.Keys
import jp.mihailskuzmins.sugoinihongoapp.resources.QuizQuestionResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

typealias WordQuizOptionVisibility = Pair<List<WordQuizQuestionOptionModel>, Boolean>

private const val mSubmitDebounceMilliseconds = 250L

class WordStudyFindRecogniseViewModel(app: Application, quizType: WordQuizType) :
	ViewModelBase(app), SupportsNavDirections {

	private val mQuestionsSubject: Subject<Stack<WordQuizQuestionModel>> = PublishSubject.create()
	private val mQuizTypeSubject: Subject<WordQuizType> = PublishSubject.create()
	private val mScoreAndMistakeCountSubject: Subject<WordQuizScoreAndMistakeCount> = BehaviorSubject.createDefault(WordQuizScoreAndMistakeCount(0, 0))
	private val mQuestionOptionsSubject: Subject<WordQuizOptionVisibility> = ReplaySubject.createWithSize(1)
	private val mNavDirectionsSubject: Subject<NavDirections> = PublishSubject.create()
	private val mIsTranscriptionCheckedSubject: Subject<Boolean> = ReplaySubject.createWithSize(1)

	private lateinit var mCurrentQuestion: WordQuizQuestionModel

	@Inject lateinit var wordRepo: WordRepo
	@Inject lateinit var wordStudyRepo: WordStudyRepo
	@Inject lateinit var wordQuizRepo: WordQuizRepo
	@Inject lateinit var sharedPreferencesUtil: SharedPreferencesUtil

	val isTranscriptionShownSettingValue: Boolean
	val isResultShownOnExit: Boolean

	val wordDialog = AppAlertViewDialog<WordQuizQuestionOptionModel, DialogWordStudyWordBinding>()

	var wordQuizId: Long = -1L
		private set

	var questionText by BindableProp(BR.questionText, "")
		@Bindable get
		private set

	var questionIndex by BindableProp(BR.questionIndex, 0)
		@Bindable get
		private set

	var score by BindableProp(BR.score, 0)
		@Bindable get
		private set

	var mistakeCount by BindableProp(BR.mistakeCount, 0)
		@Bindable get
		private set

	init {
		runBlocking {
			wordQuizId = wordQuizRepo.insertQuiz(quizType)
		}
	}

	init {
		isResultShownOnExit = sharedPreferencesUtil.getBoolean(Keys.wordQuizShowResultOnExit)

		sharedPreferencesUtil
			.getBoolean(Keys.wordQuizIsTranscriptionShown)
			.apply {
				isTranscriptionShownSettingValue = this
				mIsTranscriptionCheckedSubject.onNext(this)
			}
	}

	var isTranscriptionChecked by ReactiveProp(isTranscriptionShownSettingValue, mIsTranscriptionCheckedSubject::onNext)

	init {
		val minOptionCount = sharedPreferencesUtil.getInt(Keys.wordQuizMinOptionCount)
		val maxOptionCount = sharedPreferencesUtil.getInt(Keys.wordQuizMaxOptionCount)
		val questionCount = sharedPreferencesUtil.getInt(Keys.wordQuizQuestionCount)

		wordStudyRepo.getQuizFindRecogniseWord(questionCount.toLong()) { wordAndPropsIds, allWords ->
			wordAndPropsIds
				.map {
					val word = allWords.single { x -> x.id == it.wordId }
					val options = createQuestionOptions(allWords, it.wordId, minOptionCount, maxOptionCount)
					WordQuizQuestionModel(
						word,
						it.mark,
						options
					)
				}.asIterable()
				.run(Iterable<WordQuizQuestionModel>::shuffled)
				.also { this.questionCount = it.size }
				.run { Stack<WordQuizQuestionModel>().apply { addAll(this@run) } }
				.run { mQuestionsSubject.onNext(this) }
		}

		val questionObservable = Observables
			.combineLatest(
				mQuestionsSubject,
				mScoreAndMistakeCountSubject)
				{ x, y -> processResult(x, y) }

		Observables
			.combineLatest(
				questionObservable.filter { it.value != null },
				mQuizTypeSubject.distinctUntilChanged(),
				mIsTranscriptionCheckedSubject.distinctUntilChanged())
				{ x: Box<WordQuizQuestionModel?>, y: WordQuizType, z: Boolean -> Triple(x.value!!, y, z) }
			.debounce(mSubmitDebounceMilliseconds, TimeUnit.MILLISECONDS)
			.map { createQuestion(it.first, it.second, it.third) }
			.subscribe(mQuestionOptionsSubject::onNext)
			.addTo(cleanUp)

		// VM is initialised when QuestionOptions are set for the first quiz value
		mQuestionOptionsSubject
			.take(1)
			.subscribe { isInitialisedSubject.onNext(true) }
			.addTo(cleanUp)
	}

	init {
		mQuizTypeSubject.onNext(quizType)
	}

	val optionsObservable: Observable<WordQuizOptionVisibility>
		get() = mQuestionOptionsSubject

	val isTranscriptionCheckedObservable: Observable<Boolean>
		get() = mIsTranscriptionCheckedSubject.distinctUntilChanged().debounce(mSubmitDebounceMilliseconds, TimeUnit.MILLISECONDS)

	override val navDirectionsObservable: Observable<NavDirections>
		get() = mNavDirectionsSubject.distinctUntilChanged()

	// Do not make it @Bindable because it is set before the VM is bound to the UI
	var questionCount: Int = 0
		private set

	fun submitAnswer(model: WordQuizQuestionOptionModel) {
		val result = if (model.isCorrect) QuizQuestionResult.Correct
			else QuizQuestionResult.Wrong

		GlobalScope.launch {
			wordRepo.updateMark(mCurrentQuestion.wordId, mCurrentQuestion.mark, model.isCorrect)
		}

		GlobalScope.launch {
			createResultEntity(model)
				.run { wordQuizRepo.insertResult(this) }

			if (!isTranscriptionShownSettingValue)
				isTranscriptionChecked = false

			val scoreAndMistakeCount = getNewScoreAndMistakesCount(result)

			wordQuizRepo.updateQuiz(wordQuizId, scoreAndMistakeCount.score, scoreAndMistakeCount.mistakeCount)
			mScoreAndMistakeCountSubject.onNext(scoreAndMistakeCount)
		}
	}

	private fun processResult(questions: Stack<WordQuizQuestionModel>, scoreAndMistakeCount: WordQuizScoreAndMistakeCount): Box<WordQuizQuestionModel?> {
		score = scoreAndMistakeCount.score
		mistakeCount = scoreAndMistakeCount.mistakeCount

		if (questions.isEmpty()) {
			WordStudyFindRecogniseFragmentDirections
				.wordQuizFindRecogniseWordToWordQuizResultFindRecogniseWord(wordQuizId)
				.invoke(mNavDirectionsSubject::onNext)

			return Box(null)
		}

		mCurrentQuestion = questions.pop()
		questionIndex = questionCount - questions.size

		return Box(mCurrentQuestion)
	}

	private fun createQuestion(question: WordQuizQuestionModel, quizType: WordQuizType, isTranscriptionShown: Boolean): WordQuizOptionVisibility {
		val isFindWordQuiz = quizType == WordQuizType.FindWord

		questionText = if (isFindWordQuiz) question.word.original
			else question.word.createJapaneseTextWithNewLine(isTranscriptionShown)

		return Pair(question.options, isTranscriptionShown)
	}

	private fun createQuestionOptions(allWords: List<WordStudyModel>, currentWordId: String, minOptionCount: Int, maxOptionCount: Int): List<WordStudyModel> {
		val allWordsWithoutCurrent = allWords
			.filter { it.id != currentWordId }
			.toMutableList()

		// -1, because we do not consider the current word
		val optionCount = Random.nextInt(minOptionCount..maxOptionCount) - 1
		val optionsList = mutableListOf<WordStudyModel>()

		while (optionsList.size < optionCount) {
			if (allWordsWithoutCurrent.isEmpty())
				break

			Random
				.nextInt(allWordsWithoutCurrent.size)
				.run { allWordsWithoutCurrent.removeAt(this) }
				.run { optionsList.add(this) }
		}

		return optionsList
	}

	private fun createResultEntity(model: WordQuizQuestionOptionModel): WordQuizQuestionResultEntity {

		fun Word.createText() = "$original - ${createJapaneseTextWithBrackets()}"

		val submittedText = ""
			.runIf(!model.isCorrect) { model.createText() }

		return WordQuizQuestionResultEntity(
			wordQuizId,
			mCurrentQuestion.word.createText(),
			submittedText,
			model.isCorrect)
	}

	private fun getNewScoreAndMistakesCount(result: QuizQuestionResult) = when (result) {
		QuizQuestionResult.Correct -> WordQuizScoreAndMistakeCount(
			score + 1,
			mistakeCount
		)
		QuizQuestionResult.Wrong -> WordQuizScoreAndMistakeCount(
			score - 2,
			mistakeCount + 1
		)
	}
}