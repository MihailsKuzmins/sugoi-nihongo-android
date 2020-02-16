package jp.mihailskuzmins.sugoinihongoapp.features.sentence


import android.view.Menu
import android.view.MenuInflater
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.FilterableListFragment
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasNoResultsTextView
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListFragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNoResultsTextView
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.sentence.SentenceListModel

class SentenceSearchListFragment :
	ListFragmentBase<SentenceListModel, SentenceSearchListViewModel>(R.layout.fragment_sentence_search_list),
	FilterableListFragment, HasNoResultsTextView {

	@BindView(R.id.fragment_sentence_search_list_recycler_view)
	override lateinit var recyclerView: RecyclerView

	@BindView(R.id.fragment_sentence_search_list_no_results_text_view)
	override lateinit var noResultsTextView: TextView

	override lateinit var searchView: SearchView

	override fun getNavigationId() =
		R.id.sentenceSearchListFragment

	override fun createViewModel() =
		provideViewModel<SentenceSearchListViewModel>()

	override fun createRecyclerViewAdapter() =
		SentenceListAdapter(this)

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		subscribeToNoResultsTextView()
			.addTo(d)
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
			isOpenSettingsAvailable = false
		}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super<ListFragmentBase>.onCreateOptionsMenu(menu, inflater)
		super<FilterableListFragment>.onCreateOptionsMenu(menu, inflater)
	}
}
