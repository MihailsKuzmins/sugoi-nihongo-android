package jp.mihailskuzmins.sugoinihongoapp.persistence.models.room

import androidx.room.*
import jp.mihailskuzmins.sugoinihongoapp.resources.RoomConstants
import jp.mihailskuzmins.sugoinihongoapp.resources.RoomConstants.WordQuizQuestionResult

@Entity(tableName = RoomConstants.wordQuizQuestionResultTable,
	foreignKeys = [
		ForeignKey(entity = WordQuizEntity::class, parentColumns = [RoomConstants.WordQuiz.wordQuizId], childColumns = [WordQuizQuestionResult.wordQuizId], onDelete = ForeignKey.CASCADE)
	],
	indices = [
		Index(value = [WordQuizQuestionResult.wordQuizId])
	])
data class WordQuizQuestionResultEntity(

	@ColumnInfo(name = WordQuizQuestionResult.wordQuizId)
	val wordQuizId: Long,

	@ColumnInfo(name = WordQuizQuestionResult.questionText)
	val questionText: String,

	@ColumnInfo(name = WordQuizQuestionResult.submittedText)
	val submittedText: String,

	@ColumnInfo(name = WordQuizQuestionResult.isCorrect)
	val isCorrect: Boolean,

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = WordQuizQuestionResult.wordId)
	var wordId: Int? = null)