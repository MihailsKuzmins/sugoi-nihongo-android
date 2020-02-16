package jp.mihailskuzmins.sugoinihongoapp.features.word


import android.view.Menu
import android.view.MenuInflater
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.invalidateOptionsMenu
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigate
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.setItemEnabled
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasFabClickAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasNoResultsTextView
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListFragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNoResultsTextView
import jp.mihailskuzmins.sugoinihongoapp.models.word.WordListModel

class WordListFragment : ListFragmentBase<WordListModel, WordListViewModel>(R.layout.fragment_word_list),
	HasFabClickAction, HasNoResultsTextView {

	@BindView(R.id.fragment_word_list_recycler_view)
	override lateinit var recyclerView: RecyclerView

	@BindView(R.id.fragment_word_list_no_results_text_view)
	override lateinit var noResultsTextView: TextView

	override fun getNavigationId() =
		R.id.wordListFragment

	override fun createViewModel() =
		provideViewModel<WordListViewModel>()

	override fun createRecyclerViewAdapter() =
		WordListAdapter(this)

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		viewModel.hasItemsObservable
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { invalidateOptionsMenu() }
			.addTo(d)

		subscribeToNoResultsTextView()
			.addTo(d)
	}

	@OnClick(R.id.fragment_word_list_fab)
	override fun onFabClicked() {
		val title = context!!.getString(R.string.word_add_word)

		navigate(WordListFragmentDirections
			.wordListToWordDetailEdit(null, title))
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)

		inflater.inflate(R.menu.menu_word_list, menu)
	}

	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)

		menu.setItemEnabled(R.id.menu_word_list_menu_item_quiz, viewModel.hasItems)
		menu.setItemEnabled(R.id.menu_word_list_menu_item_search, viewModel.hasItems)
	}

	override fun getOptionsItemSelectedFunction(itemId: Int) = when (itemId) {
		R.id.menu_word_list_menu_item_quiz -> { -> navigate(R.id.wordList_to_wordQuizSelectType) }
		R.id.menu_word_list_menu_item_search -> { -> navigate(R.id.wordList_to_wordSearchList) }
		R.id.menu_word_list_menu_item_favourites -> { -> navigate(R.id.wordList_to_wordListFavourites) }
		else -> super.getOptionsItemSelectedFunction(itemId)
	}
}
