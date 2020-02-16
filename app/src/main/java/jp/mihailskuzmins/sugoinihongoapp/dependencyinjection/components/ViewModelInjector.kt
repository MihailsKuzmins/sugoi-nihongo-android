package jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.components

import dagger.Component
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules.AssetRepoModule
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules.RepoModule
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules.RoomRepoModule
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules.UtilModule
import jp.mihailskuzmins.sugoinihongoapp.features.auth.AuthLoggedUserViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.auth.AuthSignInViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.auth.AuthSignUpViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.grammarrule.GrammarRuleDetailEditViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.grammarrule.GrammarRuleDetailReadViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.grammarrule.GrammarRuleListViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.grammarrule.GrammarRuleSearchListViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.sentence.SentenceDetailEditViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.sentence.SentenceDetailReadViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.sentence.SentenceListViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.sentence.SentenceSearchListViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.settings.SettingsUserInterfaceViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.settings.SettingsWordQuizViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.settings.about.SettingsAppSummaryViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.settings.about.SettingsThirdPartyLibraryListViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.settings.auth.SettingsAuthChangePasswordViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.settings.auth.SettingsAuthDeleteAccountViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.settings.auth.SettingsAuthUserViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.settings.backup.SettingsBackupViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.word.WordDetailEditViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.word.WordDetailReadViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.word.WordListViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.word.WordSearchListViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.word.favourites.WordFavouritesListViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.word.study.*
import javax.inject.Singleton

@Singleton
@Component(modules = [(RepoModule::class), (UtilModule::class), (AssetRepoModule::class), (RoomRepoModule::class)])
interface ViewModelInjector {

	fun inject(viewModel: GrammarRuleListViewModel)
	fun inject(viewModel: GrammarRuleSearchListViewModel)
	fun inject(viewModel: GrammarRuleDetailReadViewModel)
	fun inject(viewModel: GrammarRuleDetailEditViewModel)
	fun inject(viewModel: SentenceListViewModel)
	fun inject(viewModel: SentenceSearchListViewModel)
	fun inject(viewModel: SentenceDetailReadViewModel)
	fun inject(viewModel: SentenceDetailEditViewModel)
	fun inject(viewModel: WordListViewModel)
	fun inject(viewModel: WordFavouritesListViewModel)
	fun inject(viewModel: WordSearchListViewModel)
	fun inject(viewModel: WordDetailReadViewModel)
	fun inject(viewModel: WordDetailEditViewModel)
	fun inject(viewModel: SettingsWordQuizViewModel)
	fun inject(viewModel: SettingsUserInterfaceViewModel)
	fun inject(viewModel: SettingsBackupViewModel)
	fun inject(viewModel: SettingsAppSummaryViewModel)
	fun inject(viewModel: SettingsThirdPartyLibraryListViewModel)
	fun inject(viewModel: WordStudySelectTypeViewModel)
	fun inject(viewModel: WordStudyListOfWordsListViewModel)
	fun inject(viewModel: WordStudyFindRecogniseViewModel)
	fun inject(viewModel: WordStudyResultFindRecogniseListViewModel)
	fun inject(viewModel: AuthSignInViewModel)
	fun inject(viewModel: AuthSignUpViewModel)
	fun inject(viewModel: AuthLoggedUserViewModel)
	fun inject(viewModel: SettingsAuthUserViewModel)
	fun inject(viewModel: SettingsAuthChangePasswordViewModel)
	fun inject(viewModel: SettingsAuthDeleteAccountViewModel)
	fun inject(viewModel: WordStudyHistoryFindRecogniseListViewModel)

	@Component.Builder
	interface Builder {

		fun build(): ViewModelInjector
		fun repoModule(module: RepoModule): Builder
		fun utilModule(module: UtilModule): Builder
		fun assetRepoModule(module: AssetRepoModule): Builder
		fun roomRepoModule(module: RoomRepoModule): Builder
	}
}