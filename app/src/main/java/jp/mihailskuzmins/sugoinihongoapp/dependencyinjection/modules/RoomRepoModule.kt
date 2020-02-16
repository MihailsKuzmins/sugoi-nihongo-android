package jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import jp.mihailskuzmins.sugoinihongoapp.helpers.SingletonHolder
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.room.AppRoomDatabase
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.room.WordQuizRepo
import javax.inject.Singleton

@Module
class RoomRepoModule private constructor(private val mContext: Context) {

	companion object : SingletonHolder<RoomRepoModule, Context>(::RoomRepoModule)

	@Provides
	@Singleton
	internal fun provideWordQuizRepo() =
		AppRoomDatabase.getInstance(mContext)
			.run { WordQuizRepo(getWordQuizQuestionResultDao()) }
}