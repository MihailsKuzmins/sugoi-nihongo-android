package jp.mihailskuzmins.sugoinihongoapp.persistence.models

import jp.mihailskuzmins.sugoinihongoapp.extensions.runIf

interface Word {

	val original: String
	val translation: String
	val transcription: String
}

fun Word.createJapaneseTextWithNewLine(isTranscriptionShown: Boolean) =
	StringBuilder(translation)
		.runIf(isTranscriptionShown && transcription.isNotBlank()) {
			appendln().append(transcription)
		}.toString()

fun Word.createJapaneseTextWithBrackets() =
	StringBuilder(translation)
		.runIf(transcription.isNotBlank()) { append(" [${transcription}]") }
		.toString()