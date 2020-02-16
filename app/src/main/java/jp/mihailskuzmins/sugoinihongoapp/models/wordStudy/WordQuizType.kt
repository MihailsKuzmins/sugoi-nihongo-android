package jp.mihailskuzmins.sugoinihongoapp.models.wordStudy

private const val wordQuizTypeFindWord = 1
private const val wordQuizTypeRecogniseWord = 2

enum class WordQuizType(val ordinalValue: Int) {

	FindWord(wordQuizTypeFindWord),
	RecogniseWord(wordQuizTypeRecogniseWord);

	companion object {
		fun toEnum(ordinalValue: Int): WordQuizType = when(ordinalValue) {
			wordQuizTypeFindWord -> FindWord
			wordQuizTypeRecogniseWord -> RecogniseWord
			else -> throw Error("Enum value not found for $ordinalValue")
		}
	}
}