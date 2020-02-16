package jp.mihailskuzmins.sugoinihongoapp.features.word.favourites


import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasNoResultsTextView
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListFragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNoResultsTextView
import jp.mihailskuzmins.sugoinihongoapp.models.word.WordListModel

class WordFavouritesListFragment :
	ListFragmentBase<WordListModel, WordFavouritesListViewModel>(R.layout.fragment_word_favourites_list),
	HasNoResultsTextView {

	@BindView(R.id.fragment_word_favourites_list_recycler_view)
	override lateinit var recyclerView: RecyclerView

	@BindView(R.id.fragment_word_favourites_list_no_results_text_view)
	override lateinit var noResultsTextView: TextView

	override fun getNavigationId() =
		R.id.wordFavouritesListFragment

	override fun createViewModel() =
		provideViewModel<WordFavouritesListViewModel>()

	override fun createRecyclerViewAdapter() =
		WordFavouritesListAdapter(this)

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
