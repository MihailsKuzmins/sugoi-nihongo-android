package jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.components

import dagger.Component
import jp.mihailskuzmins.sugoinihongoapp.activities.MainActivity
import jp.mihailskuzmins.sugoinihongoapp.activities.SignInSignUpActivity
import jp.mihailskuzmins.sugoinihongoapp.activities.SplashScreenActivity
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules.RepoModule
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules.RoomRepoModule
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules.StorageModule
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules.UtilModule
import jp.mihailskuzmins.sugoinihongoapp.features.word.study.WordStudyFindRecogniseFragment
import jp.mihailskuzmins.sugoinihongoapp.features.word.study.WordStudyListOfWordsListAdapter
import jp.mihailskuzmins.sugoinihongoapp.helpers.DoubleTapToFinishHelper
import jp.mihailskuzmins.sugoinihongoapp.helpers.app.*
import jp.mihailskuzmins.sugoinihongoapp.helpers.services.BackupCreateService
import jp.mihailskuzmins.sugoinihongoapp.helpers.services.BackupUploadToFirebaseService
import jp.mihailskuzmins.sugoinihongoapp.helpers.services.MarkDecrementService
import jp.mihailskuzmins.sugoinihongoapp.persistence.storage.BackupStorage
import javax.inject.Singleton

@Singleton
@Component(modules = [UtilModule::class, RepoModule::class, StorageModule::class, RoomRepoModule::class])
interface AppInjector {

	fun inject(obj: DefaultPreferencesInitialiser)
	fun inject(obj: ThemeModeInitialiser)
	fun inject(obj: MarkDecrementHelper)
	fun inject(obj: WordStudyListOfWordsListAdapter)
	fun inject(obj: DoubleTapToFinishHelper)
	fun inject(obj: BackupHelper)
	fun inject(obj: WordStudyFindRecogniseFragment)
	fun inject(obj: MarkDecrementService)
	fun inject(obj: BackupCreateService)
	fun inject(obj: BackupUploadToFirebaseService)
	fun inject(obj: BackupStorage)
	fun inject(obj: StudyHistoryCleanHelper)
	fun inject(obj: SplashScreenActivity)

	@Component.Builder
	interface Builder {

		fun build(): AppInjector
		fun utilModule(module: UtilModule): Builder
		fun repoModule(module: RepoModule): Builder
		fun storageModule(module: StorageModule): Builder
		fun roomRepoModule(module: RoomRepoModule): Builder
	}
}