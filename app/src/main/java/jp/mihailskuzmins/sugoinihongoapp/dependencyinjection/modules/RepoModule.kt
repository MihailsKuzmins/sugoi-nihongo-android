package jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules

import dagger.Module
import dagger.Provides
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.GrammarRuleRepo
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.SentenceRepo
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.WordRepo
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.study.WordStudyRepo
import javax.inject.Singleton

@Module
object RepoModule {

	@Provides
	@Singleton
	internal fun provideGrammarRuleRepo() =
		GrammarRuleRepo()

	@Provides
	@Singleton
	internal fun provideSentenceRepo() =
		SentenceRepo()

	@Provides
	@Singleton
	internal fun provideWordRepo() =
		WordRepo()

	@Provides
	@Singleton
	internal fun provideWordStudyRepo() =
		WordStudyRepo()
}