package jp.mihailskuzmins.sugoinihongoapp.resources

object AppConstants {

	const val disabledAlpha = 0.4F
	const val backupExtension = ".bkp.zip"

	object Urls {

		const val appWebsiteUrl = "https://sugoi-nihongo-app.firebaseapp.com/"
		const val apache2AttributionUrl = "http://www.apache.org/licenses/LICENSE-2.0"
	}

	object Auth {

		const val minPasswordLength = 8
	}

	object Models {

		const val grammarRuleListItemBodyMaxLength = 25
		const val nonStudiableMark = -1
		const val wordMarkExcellent = 9
		const val wordMarkGood = 7
		const val wordMarkAverage = 4
		const val wordMarkMax = 10
		const val wordMarkMin = 0
	}

	object RxBusContracts {

		const val bottomNavVisible = "bottomNavVisible"
		const val progressBarVisible = "progressBarVisible"
		const val isAuthInProgress = "isAuthInProgress"
	}

	object DateFormats {

		const val dateTimeFormat = "ddMMyyyy_HHmmss"
	}
}