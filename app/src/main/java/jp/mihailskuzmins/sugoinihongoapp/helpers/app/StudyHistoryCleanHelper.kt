package jp.mihailskuzmins.sugoinihongoapp.helpers.app

import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.components.AppInjector
import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.room.WordQuizRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class StudyHistoryCleanHelper(injector: AppInjector) : Runnable {

	@Inject
	lateinit var wordQuizRepo: WordQuizRepo

	init {
		injector.inject(this)
	}

	override fun run() {
		GlobalScope.launch {
			wordQuizRepo.cleanWordStudyHistory()
		}
	}
}