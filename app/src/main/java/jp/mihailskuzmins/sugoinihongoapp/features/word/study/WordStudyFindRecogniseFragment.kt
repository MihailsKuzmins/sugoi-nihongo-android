package jp.mihailskuzmins.sugoinihongoapp.features.word.study


import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.view.children
import butterknife.BindView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.DialogWordStudyWordBinding
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentWordStudyFindRecogniseBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.application
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.invalidateOptionsMenu
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigate
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.extensions.applyIf
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasOverriddenBackNavigation
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNavDirections
import jp.mihailskuzmins.sugoinihongoapp.helpers.DoubleTapToFinishHelper
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.configs.AlertViewConfig
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordQuizQuestionOptionModel
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordQuizType
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.createJapaneseTextWithNewLine
import jp.mihailskuzmins.sugoinihongoapp.resources.NavMode

class WordStudyFindRecogniseFragment :
	FragmentBase<WordStudyFindRecogniseViewModel>(R.layout.fragment_word_study_find_recognise),
	HasOverriddenBackNavigation {

	private val mPopUpToFragmentId = R.id.wordStudySelectTypeFragment

	private lateinit var mDoubleTapToFinishHelper: DoubleTapToFinishHelper

	private val mOptionsLinearLayout: LinearLayout
		get() = optionsScrollView.children.single() as LinearLayout

	private val mShouldHideTranscription: Boolean
		get() = viewModel.isTranscriptionShownSettingValue

	private val mQuizType: WordQuizType
		get() = WordStudyFindRecogniseFragmentArgs
			.fromBundle(arguments!!)
			.quizType

	@BindView(R.id.fragment_word_study_find_recognise_word_options_scroll_view)
	lateinit var optionsScrollView: ScrollView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		mDoubleTapToFinishHelper = DoubleTapToFinishHelper(activity!!)
	}

	override fun getNavigationId() =
		R.id.wordQuizFindRecogniseWordFragment

	override fun bindView() {
		getBinding<FragmentWordStudyFindRecogniseBinding>().viewModel = viewModel
	}

	override fun createViewModel() =
		provideViewModel {
			WordStudyFindRecogniseViewModel(application, mQuizType)
		}


	override fun beforeNavigatingBack() = mDoubleTapToFinishHelper.canNavigateBack()

	override fun overrideBackNavigation(): Boolean {
		if (!viewModel.isResultShownOnExit || viewModel.questionIndex <= 1) {
			return false
		}

		WordStudyFindRecogniseFragmentDirections
			.wordQuizFindRecogniseWordToWordQuizResultFindRecogniseWord(viewModel.wordQuizId)
			.invoke { navigate(it, mPopUpToFragmentId) }

		return true
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)

		if (mShouldHideTranscription)
			return

		inflater.inflate(R.menu.menu_word_study_find_recognise, menu)
	}

	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)

		if (mShouldHideTranscription)
			return

		menu.findItem(R.id.menu_word_quiz_find_recognise_word_menu_item_toggle_transcription)
			.invoke { applyTranscriptionToggle(it, viewModel.isTranscriptionChecked) }
	}

	override fun getOptionsItemSelectedFunction(itemId: Int) =
		when (itemId) {
			R.id.menu_word_quiz_find_recognise_word_menu_item_toggle_transcription -> { ->
				viewModel.isTranscriptionChecked = viewModel.isTranscriptionChecked.not()
			}
			else -> super.getOptionsItemSelectedFunction(itemId)
		}

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		viewModel.optionsObservable
			.map { it.first.map { x -> createOptionButton(x, it.second) } }
			.observeOn(AndroidSchedulers.mainThread())
			.doOnEach { mOptionsLinearLayout.removeAllViews() }
			.subscribe { it.forEach(mOptionsLinearLayout::addView) }
			.addTo(d)

		viewModel.isTranscriptionCheckedObservable
			.filter { mShouldHideTranscription.not() }
			.skip(1)
			.distinctUntilChanged()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { invalidateOptionsMenu() }
			.addTo(d)

		viewModel.wordDialog
			.subscribe(this)
			.addTo(d)

		subscribeToNavDirections(NavMode.Main, mPopUpToFragmentId)
			.addTo(d)
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
			isOpenSettingsAvailable = false
		}

	private fun createOptionButton(model: WordQuizQuestionOptionModel, isTranscriptionShown: Boolean): Button {
		val isFindWordQuiz = mQuizType == WordQuizType.FindWord
		val buttonText = if (isFindWordQuiz) model.createJapaneseTextWithNewLine(isTranscriptionShown)
			else model.original

		return Button(context!!).apply {
			text = buttonText
			setOnClickListener { viewModel.submitAnswer(model) }
			setOnLongClickListener { alertWord(model); true }
		}.applyIf(isFindWordQuiz) {
			setTextSize(TypedValue.COMPLEX_UNIT_SP, 23F)
		}
	}

	private fun alertWord(model: WordQuizQuestionOptionModel) {
		AlertViewConfig<WordQuizQuestionOptionModel, DialogWordStudyWordBinding>(
			R.layout.dialog_word_study_word, model) { x, binding -> binding.model = x }
			.apply {
				title = model.original
			}.invoke { viewModel.wordDialog.config = it }
	}

	private fun applyTranscriptionToggle(menuItem: MenuItem, isChecked: Boolean) {
		if (menuItem.isChecked == isChecked)
			return

		val iconRes = if (isChecked) R.drawable.ic_visibility_off_black_24dp
		else R.drawable.ic_visibility_black_24dp

		val titleRes = if (isChecked) R.string.word_hide_transcription
		else R.string.word_show_transcription

		with (menuItem) {
			this.isChecked = isChecked
			icon = context!!.getDrawable(iconRes)
			title = context!!.getString(titleRes)
		}
	}
}
