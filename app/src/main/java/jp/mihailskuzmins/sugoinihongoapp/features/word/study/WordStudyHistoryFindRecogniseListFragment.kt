package jp.mihailskuzmins.sugoinihongoapp.features.word.study


import android.widget.TextView
import butterknife.BindView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasNoResultsTextView
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasStickyExpandableListView
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.StickyListFragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNoResultsTextView
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordQuizListModel
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView

class WordStudyHistoryFindRecogniseListFragment :
	StickyListFragmentBase<WordQuizListModel, WordStudyHistoryFindRecogniseListViewModel>(R.layout.fragment_word_study_history_find_recognise_list),
	HasStickyExpandableListView, HasNoResultsTextView {

	@BindView(R.id.fragment_word_study_history_find_recognise_list_list_view)
	override lateinit var listView: ExpandableStickyListHeadersListView

	@BindView(R.id.fragment_word_study_history_find_recognise_list_no_results_text_view)
	override lateinit var noResultsTextView: TextView

	override fun getNavigationId() =
		R.id.wordStudyHistoryFindRecogniseListFragment

	override fun createViewModel() =
		provideViewModel<WordStudyHistoryFindRecogniseListViewModel>()

	override fun createAdapter() =
		WordStudyHistoryFindRecogniseListAdapter(this)

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		subscribeToNoResultsTextView()
			.addTo(d)
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
		}
}
