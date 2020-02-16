package jp.mihailskuzmins.sugoinihongoapp.features.base

import android.app.Application
import android.content.Context
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.AndroidViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.components.DaggerViewModelInjector
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.components.ViewModelInjector
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules.AssetRepoModule
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules.RepoModule
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules.RoomRepoModule
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules.UtilModule
import jp.mihailskuzmins.sugoinihongoapp.features.auth.AuthLoggedUserViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.auth.AuthSignInViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.auth.AuthSignUpViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.SupportsActivation
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
import jp.mihailskuzmins.sugoinihongoapp.helpers.BaseObservable
import jp.mihailskuzmins.sugoinihongoapp.helpers.ViewModelActivator

abstract class ViewModelBase(app: Application) : AndroidViewModel(app), BaseObservable, SupportsActivation {

	private val injector: ViewModelInjector = DaggerViewModelInjector
		.builder()
		.repoModule(RepoModule)
		.utilModule(UtilModule.getInstance(app))
		.assetRepoModule(AssetRepoModule.getInstance(app))
		.roomRepoModule(RoomRepoModule.getInstance(app))
		.build()

	init {
		inject()
	}

	final override val activator = ViewModelActivator()
	final override var callbacks: PropertyChangeRegistry? = null

	protected val cleanUp: CompositeDisposable = CompositeDisposable()
	protected val isInitialisedSubject: Subject<Boolean> = ReplaySubject.createWithSize(1)

	val context: Context
		get() = getApplication()

	val isInitialised: Observable<Boolean>
		get() = isInitialisedSubject.distinctUntilChanged()

	override fun onCleared() {
		cleanUp.dispose()
		// Do not call in Fragments so that it is not attached to the fragment lifecycle
		activator.dispose()
		super.onCleared()
	}

	private fun inject() {
		when (this) {
			is GrammarRuleListViewModel -> injector.inject(this)
			is GrammarRuleSearchListViewModel -> injector.inject(this)
			is GrammarRuleDetailReadViewModel -> injector.inject(this)
			is GrammarRuleDetailEditViewModel -> injector.inject(this)
			is SentenceListViewModel -> injector.inject(this)
			is SentenceSearchListViewModel -> injector.inject(this)
			is SentenceDetailReadViewModel -> injector.inject(this)
			is SentenceDetailEditViewModel -> injector.inject(this)
			is WordListViewModel -> injector.inject(this)
			is WordFavouritesListViewModel -> injector.inject(this)
			is WordSearchListViewModel -> injector.inject(this)
			is WordDetailReadViewModel -> injector.inject(this)
			is WordDetailEditViewModel -> injector.inject(this)
			is SettingsWordQuizViewModel -> injector.inject(this)
			is SettingsUserInterfaceViewModel -> injector.inject(this)
			is SettingsBackupViewModel -> injector.inject(this)
			is SettingsAppSummaryViewModel -> injector.inject(this)
			is SettingsThirdPartyLibraryListViewModel -> injector.inject(this)
			is WordStudySelectTypeViewModel -> injector.inject(this)
			is WordStudyListOfWordsListViewModel -> injector.inject(this)
			is WordStudyFindRecogniseViewModel -> injector.inject(this)
			is WordStudyResultFindRecogniseListViewModel -> injector.inject(this)
			is AuthSignInViewModel -> injector.inject(this)
			is AuthSignUpViewModel -> injector.inject(this)
			is AuthLoggedUserViewModel -> injector.inject(this)
			is SettingsAuthUserViewModel -> injector.inject(this)
			is SettingsAuthChangePasswordViewModel -> injector.inject(this)
			is SettingsAuthDeleteAccountViewModel -> injector.inject(this)
			is WordStudyHistoryFindRecogniseListViewModel -> injector.inject(this)
		}
	}
}