package jp.mihailskuzmins.sugoinihongoapp.persistence.models.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordQuizType
import jp.mihailskuzmins.sugoinihongoapp.resources.RoomConstants
import jp.mihailskuzmins.sugoinihongoapp.resources.RoomConstants.WordQuiz
import java.util.*

@Entity(tableName = RoomConstants.wordQuizTable)
data class WordQuizEntity(
	@ColumnInfo(name = WordQuiz.wordQuizType) val wordQuizType: WordQuizType,
	@PrimaryKey(autoGenerate = true) @ColumnInfo(name = WordQuiz.wordQuizId) var wordQuizId: Long? = null) {

	@ColumnInfo(name = WordQuiz.score)
	var score: Int = 0

	@ColumnInfo(name = WordQuiz.mistakeCount)
	var mistakeCount: Int = 0

	@ColumnInfo(name = WordQuiz.date)
	var date: Date = Date()
}