package jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.findSearchView
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.hideKeyboard
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.showKeyboard

interface FilterableListFragment : SearchView.OnQueryTextListener, AppFragment {

	val viewModel: FilterableListViewModel
	var searchView: SearchView

	override fun onQueryTextChange(newText: String?): Boolean {
		newText?.let {
			viewModel.searchText = it
			return@onQueryTextChange true
		}

		return false
	}

	override fun onQueryTextSubmit(query: String?): Boolean {
		activity!!.hideKeyboard()

		return true
	}

	fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.menu_search_list, menu)

		menu.findSearchView(R.id.menu_search_list_menu_item_search)
			.also { searchView = it }
			.run { applySearchViewAttributes(context) }
	}
}

private fun FilterableListFragment.applySearchViewAttributes(context: Context) {
	with(searchView) {
		queryHint = "${context.getString(R.string.general_search)}..."

		// hack to make SearchView focusable
		isIconified = true
		isIconified = false

		setIconifiedByDefault(false) // doesn't display the clear icon if search text is empty
		setOnQueryTextListener(this@applySearchViewAttributes)

		// take full width
		maxWidth = Int.MAX_VALUE

		// query text for screen rotation
		setQuery(viewModel.searchText, false)
	}

	searchView
		.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
		.apply {
			setTextColor(ContextCompat.getColor(context, R.color.colorDetailTextLabel))
			setHintTextColor(ContextCompat.getColor(context, R.color.colorSearchTextPlaceholder))

			requestFocus()
			showKeyboard()
		}
}