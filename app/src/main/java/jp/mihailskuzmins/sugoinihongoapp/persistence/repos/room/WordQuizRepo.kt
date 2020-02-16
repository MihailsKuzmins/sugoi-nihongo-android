package jp.mihailskuzmins.sugoinihongoapp.persistence.repos.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordQuizType
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.room.WordQuizEntity
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.room.WordQuizQuestionResultEntity
import jp.mihailskuzmins.sugoinihongoapp.resources.RoomConstants
import jp.mihailskuzmins.sugoinihongoapp.resources.RoomConstants.WordQuiz
import jp.mihailskuzmins.sugoinihongoapp.resources.RoomConstants.WordQuizQuestionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordQuizRepo(private val mDao: WordQuizQuestionResultDao) {

	suspend fun insertQuiz(wordQuizType: WordQuizType): Long = withContext(Dispatchers.Default) {
		mDao.insertQuiz(WordQuizEntity(wordQuizType))
	}

	suspend fun updateQuiz(wordQuizId: Long, score: Int, mistakeCount: Int) = withContext(Dispatchers.Default) {
		mDao.updateQuiz(wordQuizId, score, mistakeCount)
	}

	suspend fun selectQuiz(wordQuizId: Long) = withContext(Dispatchers.Default) {
		mDao.selectQuiz(wordQuizId)
	}

	suspend fun selectLatestQuizzes() = withContext(Dispatchers.Default) {
		mDao.selectLatestQuizzes()
	}

	suspend fun cleanWordStudyHistory() = withContext(Dispatchers.Default) {
		mDao.deleteEmptyQuizzes()
		mDao.deleteOldQuizzes()
	}

	suspend fun insertResult(entity: WordQuizQuestionResultEntity) = withContext(Dispatchers.Default) {
		mDao.insertResult(entity)
	}

	suspend fun getResults(wordQuizId: Long): List<WordQuizQuestionResultEntity> = withContext(Dispatchers.Default) {
		mDao.selectResults(wordQuizId)
	}
}

const val tblWordQuiz = RoomConstants.wordQuizTable
const val tblWordQuizResult = RoomConstants.wordQuizQuestionResultTable
const val blnHasResults = "SELECT ${WordQuizQuestionResult.wordQuizId} FROM $tblWordQuizResult WHERE ${tblWordQuizResult}.${WordQuizQuestionResult.wordQuizId} = ${tblWordQuiz}.${WordQuiz.wordQuizId} LIMIT 1"


@Dao
interface WordQuizQuestionResultDao {

	@Insert
	fun insertQuiz(entity: WordQuizEntity): Long

	@Query("UPDATE $tblWordQuiz SET ${WordQuiz.score} = :score, ${WordQuiz.mistakeCount} = :mistakeCount WHERE ${WordQuiz.wordQuizId} = :wordQuizId")
	fun updateQuiz(wordQuizId: Long, score: Int, mistakeCount: Int)

	@Query("SELECT * FROM $tblWordQuiz WHERE ${WordQuiz.wordQuizId} = :wordQuizId")
	fun selectQuiz(wordQuizId: Long): WordQuizEntity

	@Query("SELECT * FROM $tblWordQuiz WHERE EXISTS ($blnHasResults) ORDER BY ${WordQuiz.date} DESC LIMIT ${RoomConstants.wordQuizHistoryMaxCount}")
	fun selectLatestQuizzes(): List<WordQuizEntity>

	@Query("DELETE FROM $tblWordQuiz WHERE NOT EXISTS ($blnHasResults)")
	fun deleteEmptyQuizzes()

	@Query("DELETE FROM $tblWordQuiz WHERE ${WordQuiz.wordQuizId} IN (SELECT ${WordQuiz.wordQuizId} FROM $tblWordQuiz ORDER BY ${WordQuiz.date} DESC LIMIT -1 OFFSET ${RoomConstants.wordQuizHistoryMaxCount})")
	fun deleteOldQuizzes()

	@Insert
	fun insertResult(entity: WordQuizQuestionResultEntity)

	@Query("SELECT * FROM ${RoomConstants.wordQuizQuestionResultTable} WHERE ${WordQuizQuestionResult.wordQuizId} = :wordQuizId")
	fun selectResults(wordQuizId: Long): List<WordQuizQuestionResultEntity>
}