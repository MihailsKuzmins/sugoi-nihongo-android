package jp.mihailskuzmins.sugoinihongoapp.features.word

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.ListItemWordBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigate
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.AppFragment
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasClickAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterViewHolderBase
import jp.mihailskuzmins.sugoinihongoapp.models.word.WordListModel
import jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems.WordListItem

class WordListAdapter(private val mFragment: AppFragment) :
	ListAdapterBase<WordListModel, ListItemWordBinding>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		DataBindingUtil.inflate<ListItemWordBinding>(
				LayoutInflater.from(parent.context), R.layout.list_item_word, parent, false)
			.run(::ViewHolder)


	inner class ViewHolder(binding: ListItemWordBinding) :
		ListAdapterViewHolderBase<WordListModel, ListItemWordBinding>(binding), HasClickAction {

		override fun bindView(model: WordListModel, binding: ListItemWordBinding) =
			with(binding) {
				listItem = WordListItem(model)
				viewHolder = this@ViewHolder
			}

		override fun clickAction(id: String) =
			WordListFragmentDirections
				.wordListToWordDetailRead(id)
				.invoke { mFragment.navigate(it) }

	}
}