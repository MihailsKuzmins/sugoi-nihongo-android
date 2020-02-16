package jp.mihailskuzmins.sugoinihongoapp.features.word

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.ListItemWordSearchBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigate
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.AppFragment
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasClickAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterViewHolderBase
import jp.mihailskuzmins.sugoinihongoapp.models.word.WordSearchListModel
import jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems.WordListItem

class WordSearchListAdapter(private val mFragment: AppFragment) :
	ListAdapterBase<WordSearchListModel, ListItemWordSearchBinding>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		DataBindingUtil.inflate<ListItemWordSearchBinding>(
				LayoutInflater.from(parent.context), R.layout.list_item_word_search, parent, false)
			.run(::ViewHolder)


	inner class ViewHolder(binding: ListItemWordSearchBinding) :
		ListAdapterViewHolderBase<WordSearchListModel, ListItemWordSearchBinding>(binding), HasClickAction {

		override fun bindView(model: WordSearchListModel, binding: ListItemWordSearchBinding) =
			with(binding) {
				listItem = WordListItem(model)
				viewHolder = this@ViewHolder
			}

		override fun clickAction(id: String) = mFragment.navigate(
			WordSearchListFragmentDirections
				.wordSearchListToWordDetailRead(id))
	}
}