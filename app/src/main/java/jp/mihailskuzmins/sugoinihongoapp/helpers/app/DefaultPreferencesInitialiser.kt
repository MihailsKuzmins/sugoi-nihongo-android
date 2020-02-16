package jp.mihailskuzmins.sugoinihongoapp.helpers.app

import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.components.AppInjector
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.Int
import jp.mihailskuzmins.sugoinihongoapp.resources.PreferencesConstants.Keys
import javax.inject.Inject

class DefaultPreferencesInitialiser(injector: AppInjector) : Runnable {

	@Inject
	lateinit var sharedPreferencesUtil: SharedPreferencesUtil

	init {
		injector.inject(this)
	}

	override fun run() {
		// Word quizzes
		sharedPreferencesUtil.setIntIfNotSet(Keys.wordQuizQuestionCount, Int.wordQuizMaxQuestionsCount)
		sharedPreferencesUtil.setIntIfNotSet(Keys.wordQuizMinOptionCount, Int.wordQuizMinOptionCount)
		sharedPreferencesUtil.setIntIfNotSet(Keys.wordQuizMaxOptionCount, Int.wordQuizMaxOptionCount)
		sharedPreferencesUtil.setBooleanIfNotSet(Keys.wordQuizIsTranscriptionShown, true)
		sharedPreferencesUtil.setIntIfNotSet(Keys.wordMarkDecrementDays, Int.wordMarkMaxDecrementDays)
		sharedPreferencesUtil.setBooleanIfNotSet(Keys.wordQuizShowResultOnExit, true)
		sharedPreferencesUtil.setBooleanIfNotSet(Keys.wordMarkIsDecrementEnabled, true)

		// Grammar rule exercises
		sharedPreferencesUtil.setIntIfNotSet(Keys.grammarRuleExerciseCount, Int.grammarRuleMaxExerciseCount)

		// User interface
		sharedPreferencesUtil.setBooleanIfNotSet(Keys.uiShowEntryId, false)
		sharedPreferencesUtil.setBooleanIfNotSet(Keys.uiShowWordMarkTable, true)
		sharedPreferencesUtil.setBooleanIfNotSet(Keys.uiIsDarkMode, false)

		// Backup
		sharedPreferencesUtil.setBooleanIfNotSet(Keys.isBackupEnabled, true)
		sharedPreferencesUtil.setIntIfNotSet(Keys.backupFileCount, Int.backupFileMaxCount)
		sharedPreferencesUtil.setIntIfNotSet(Keys.backupCreationFrequencyDays, 1)
	}
}