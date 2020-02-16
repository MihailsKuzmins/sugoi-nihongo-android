package jp.mihailskuzmins.sugoinihongoapp.persistence.repos.room

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.execAll
import jp.mihailskuzmins.sugoinihongoapp.helpers.SingletonHolder
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordQuizType
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.room.WordQuizEntity
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.room.WordQuizQuestionResultEntity
import jp.mihailskuzmins.sugoinihongoapp.resources.RoomConstants
import jp.mihailskuzmins.sugoinihongoapp.resources.RoomConstants.WordQuiz
import jp.mihailskuzmins.sugoinihongoapp.resources.RoomConstants.WordQuizQuestionResult
import java.util.*

@Database(
	entities = [
		WordQuizEntity::class,
		WordQuizQuestionResultEntity::class],
	version = 2,
	exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppRoomDatabase : RoomDatabase() {

	abstract fun getWordQuizQuestionResultDao(): WordQuizQuestionResultDao

	companion object : SingletonHolder<AppRoomDatabase, Context>({ x ->
		Room
			.databaseBuilder(x, AppRoomDatabase::class.java, "app_db")
			.addMigrations(migration1To2)
			.build()
	})
}

class Converters {

	@TypeConverter
	fun fromLongToDate(value: Long) = Date(value)

	@TypeConverter
	fun fromDateToLong(value: Date) = value.time

	@TypeConverter
	fun fromIntToWordQuizType(value: Int) = WordQuizType.toEnum(value)

	@TypeConverter
	fun fromWordQuizTypeToInt(value: WordQuizType) = value.ordinalValue
}

private val migration1To2 = object : Migration(1, 2) {
	override fun migrate(database: SupportSQLiteDatabase) {
		val dropGrammarRuleTaskSelection = "DROP TABLE grammarRuleTaskSelectionTable"
		val dropGrammarRuleExerciseResult = "DROP TABLE grammarRuleExerciseResultTable"
		val dropGrammarRuleTaskResult = "DROP TABLE grammarRuleExerciseResultTaskTable"
		val dropWordQuizResult = "DROP TABLE ${RoomConstants.wordQuizQuestionResultTable}"

		val createWordQuizTable = """
				CREATE TABLE `${RoomConstants.wordQuizTable}` (
					`${WordQuiz.wordQuizId}` INTEGER PRIMARY KEY AUTOINCREMENT,
					`${WordQuiz.wordQuizType}` INTEGER NOT NULL,
					`${WordQuiz.score}` INTEGER NOT NULL,
					`${WordQuiz.mistakeCount}` INTEGER NOT NULL,
					`${WordQuiz.date}` INTEGER NOT NULL
				)
			""".trimMargin()


		val createWordQuizResultTable = """
			CREATE TABLE ${RoomConstants.wordQuizQuestionResultTable} (
				${WordQuizQuestionResult.wordId} INTEGER PRIMARY KEY AUTOINCREMENT,
				${WordQuizQuestionResult.wordQuizId} INTEGER NOT NULL,
				${WordQuizQuestionResult.questionText} TEXT NOT NULL,
				${WordQuizQuestionResult.submittedText} TEXT NOT NULL,
				${WordQuizQuestionResult.isCorrect} INTEGER DEFAULT 0 NOT NULL,
				
				CONSTRAINT FK_${WordQuizQuestionResult.wordQuizId}_${RoomConstants.wordQuizTable}
					FOREIGN KEY (${WordQuizQuestionResult.wordQuizId})
					REFERENCES ${RoomConstants.wordQuizTable} (${WordQuiz.wordQuizId})
					ON DELETE CASCADE
			)
		""".trimIndent()

		val addIndexForWordQuizId = """
			CREATE INDEX index_${RoomConstants.wordQuizQuestionResultTable}_${WordQuizQuestionResult.wordQuizId} ON ${RoomConstants.wordQuizQuestionResultTable} (${WordQuizQuestionResult.wordQuizId})
		""".trimIndent()

		database.execAll(
			dropGrammarRuleTaskSelection,
			dropGrammarRuleExerciseResult,
			dropGrammarRuleTaskResult,
			dropWordQuizResult,
			createWordQuizTable,
			createWordQuizResultTable,
			addIndexForWordQuizId)
	}
}
