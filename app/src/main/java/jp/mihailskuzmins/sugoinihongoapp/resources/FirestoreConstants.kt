package jp.mihailskuzmins.sugoinihongoapp.resources

import java.util.*

object FirestoreConstants {

	object Collections {

		const val grammarRules = "grammarRules"
		const val sentences = "sentences"
		const val words = "words"
	}

	object Keys {

		const val userId = "userId"

		object GrammarRules {

			const val body = "body"
			const val dateCreated = "dateCreated"
			const val header = "header"
		}

		object Sentences {

			const val dateCreated = "dateCreated"
			const val original = "original"
			const val transcription = "transcription"
			const val translation = "translation"
		}

		object Words {

			const val dateCreated = "dateCreated"
			const val dateLastAccessed = "dateLastAccessed"
			const val isFavourite = "isFavourite"
			const val isStudiable = "isStudiable"
			const val original = "original"
			const val mark = "mark"
			const val notes = "notes"
			const val timesAccessed = "timesAccessed"
			const val timesCorrect = "timesCorrect"
			const val transcription = "transcription"
			const val translation = "translation"
		}
	}

	object DefaultValues {

		const val int: Int = 0
		const val double: Double = 0.0
		const val boolean: Boolean = false
		val date: Date = GregorianCalendar(1930, 0, 1).time

		const val isStudiable = true
	}
}