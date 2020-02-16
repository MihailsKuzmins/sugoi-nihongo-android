package jp.mihailskuzmins.sugoinihongoapp.features.grammarrule


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
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.grammarrule.GrammarRuleListModel

class GrammarRuleSearchListFragment :
	ListFragmentBase<GrammarRuleListModel, GrammarRuleSearchListViewModel>(R.layout.fragment_grammar_rule_search_list),
	FilterableListFragment, HasNoResultsTextView {

	@BindView(R.id.fragment_grammar_rule_search_list_recycler_view)
	override lateinit var recyclerView: RecyclerView

	@BindView(R.id.fragment_grammar_rule_search_list_no_results_text_view)
	override lateinit var noResultsTextView: TextView

	override lateinit var searchView: SearchView

	override fun getNavigationId() =
		R.id.grammarRuleSearchListFragment

	override fun createViewModel() =
		provideViewModel<GrammarRuleSearchListViewModel>()

	override fun createRecyclerViewAdapter() =
		GrammarRuleSearchListAdapter(this)

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
