package jp.mihailskuzmins.sugoinihongoapp.features.settings

import android.app.Application
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.DetailViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItem
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemSeekBar
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.detailitems.DetailItemSwitch
import jp.mihailskuzmins.sugoinihongoapp.features.base.detail.initAction
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.Int
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.Keys
import jp.mihailskuzmins.sugoinihongoapp.userinterface.views.SectionHeader
import javax.inject.Inject

class SettingsWordQuizViewModel(app: Application) : DetailViewModelBase(app) {

	@Inject
	lateinit var sharedPreferencesUtil: SharedPreferencesUtil

	val isTranscriptionShownItem = DetailItemSwitch(app, R.string.word_show_transcription, R.string.settings_word_quiz_kana_is_shown)
		.apply {
			addValueChangedAction { sharedPreferencesUtil.setBoolean(Keys.wordQuizIsTranscriptionShown, it) }
		}

	val isResultShowOnExitItem = DetailItemSwitch(app, R.string.study_show_result_on_exit, R.string.word_quiz_enable_disable_result_on_exit)
		.apply {
			addValueChangedAction { sharedPreferencesUtil.setBoolean(Keys.wordQuizShowResultOnExit, it) }
		}

	val questionCountItem = DetailItemSeekBar(app, R.string.settings_word_quiz_question_count, maxValue = Int.wordQuizMaxQuestionsCount)
		.apply {
			addValueChangedAction { sharedPreferencesUtil.setInt(Keys.wordQuizQuestionCount, it) }
		}

	val minOptionCountItem = DetailItemSeekBar(app, R.string.settings_word_quiz_min_option_count, maxValue = Int.wordQuizMaxOptionCount, minValue = Int.wordQuizMinOptionCount)
		.apply {
			addValueChangedAction { sharedPreferencesUtil.setInt(Keys.wordQuizMinOptionCount, it) }
		}

	val maxOptionCountItem = DetailItemSeekBar(app, R.string.settings_word_quiz_max_option_count, maxValue = Int.wordQuizMaxOptionCount)
		.apply {
			addValueChangedAction { sharedPreferencesUtil.setInt(Keys.wordQuizMaxOptionCount, it) }
		}

	val isMarkDecrementEnabledItem = DetailItemSwitch(app, R.string.settings_word_quiz_decrement_mark, R.string.settings_word_quiz_mark_is_decremented)
		.apply {
			addValueChangedAction { sharedPreferencesUtil.setBoolean(Keys.wordMarkIsDecrementEnabled, it) }
		}

	val decrementMarkDaysItem = DetailItemSeekBar(app, R.string.settings_word_quiz_decrement_mark_after_days, maxValue = Int.wordMarkMaxDecrementDays)
		.apply {
			addValueChangedAction { sharedPreferencesUtil.setInt(Keys.wordMarkDecrementDays, it) }
		}

	val markSectionHeader = SectionHeader(app, R.string.word_mark)

	init {
		minOptionCountItem.valueChanged
			.subscribe { maxOptionCountItem.minValue = it }
			.addTo(cleanUp)

		maxOptionCountItem.valueChanged
			.subscribe { minOptionCountItem.maxValue = it }
			.addTo(cleanUp)

		isMarkDecrementEnabledItem.valueChanged
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { decrementMarkDaysItem.isEnabled = it }
			.addTo(cleanUp)
	}

	override fun initDetailItems(): Array<DetailItem> =
		arrayOf(isTranscriptionShownItem, isResultShowOnExitItem, questionCountItem, minOptionCountItem, maxOptionCountItem,
			isMarkDecrementEnabledItem, decrementMarkDaysItem)

	override fun initDetailItemValues(onInit: initAction) {
		isTranscriptionShownItem.initialValue = sharedPreferencesUtil.getBoolean(Keys.wordQuizIsTranscriptionShown)
		isResultShowOnExitItem.initialValue = sharedPreferencesUtil.getBoolean(Keys.wordQuizShowResultOnExit)
		questionCountItem.initialValue = sharedPreferencesUtil.getInt(Keys.wordQuizQuestionCount)
		minOptionCountItem.initialValue = sharedPreferencesUtil.getInt(Keys.wordQuizMinOptionCount)
		maxOptionCountItem.initialValue = sharedPreferencesUtil.getInt(Keys.wordQuizMaxOptionCount)
		isMarkDecrementEnabledItem.initialValue = sharedPreferencesUtil.getBoolean(Keys.wordMarkIsDecrementEnabled)
		decrementMarkDaysItem.initialValue = sharedPreferencesUtil.getInt(Keys.wordMarkDecrementDays)

		onInit()
	}
}