package jp.mihailskuzmins.sugoinihongoapp.resources

object PreferencesConstants {

	object Int {

		const val wordQuizMaxQuestionsCount = 30
		const val wordQuizMinOptionCount = 4
		const val wordQuizMaxOptionCount = 6
		const val wordMarkMaxDecrementDays = 30
		const val grammarRuleMaxExerciseCount = 10
		const val backupFileMaxCount = 30
		const val backupCreationFrequencyMaxDays = 7
	}

	object Keys {

		const val wordQuizQuestionCount = "wordQuizQuestionCount"
		const val wordQuizMinOptionCount = "wordQuizMinOptionCount"
		const val wordQuizMaxOptionCount = "wordQuizMaxOptionCount"
		const val wordQuizIsTranscriptionShown = "wordQuizIsTranscriptionShown"
		const val wordQuizShowResultOnExit = "wordQuizShowResultOnExit"
		const val wordMarkIsDecrementEnabled = "wordMarkIsDecrementEnabled"
		const val wordMarkDecrementDays = "wordMarkDecrementDays"
		const val grammarRuleExerciseCount = "grammarRuleExerciseCount"
		const val uiShowEntryId = "uiShowEntryId"
		const val uiShowWordMarkTable = "uiShowWordMarkTable"
		const val isBackupEnabled = "isBackupEnabled"
		const val backupFileCount = "backupFileCount"
		const val backupCreationFrequencyDays = "backupCreationFrequencyDays"
		const val latestMarkDecrementEpoch = "latestMarkDecrementEpoch"
		const val latestBackupCreationEpoch = "latestBackupCreationEpoch"
		const val uiIsDarkMode = "uiIsDarkMode"
	}

	object SecureKeys {

		const val currentEmailKey = "hroFGMBJcGdLShZUsPAr"
	}
}