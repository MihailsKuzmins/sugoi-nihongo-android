package jp.mihailskuzmins.sugoinihongoapp.features.sentence

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.ListItemSentenceSearchBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigate
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.AppFragment
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasClickAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterViewHolderBase
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.sentence.SentenceListModel
import jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems.SentenceListItem

class SentenceSearchListAdapter(private val mFragment: AppFragment) :
	ListAdapterBase<SentenceListModel, ListItemSentenceSearchBinding>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		DataBindingUtil.inflate<ListItemSentenceSearchBinding>(
				LayoutInflater.from(parent.context), R.layout.list_item_sentence_search, parent, false)
			.run(::ViewHolder)


	inner class ViewHolder(
		binding: ListItemSentenceSearchBinding
	) : ListAdapterViewHolderBase<SentenceListModel, ListItemSentenceSearchBinding>(binding), HasClickAction {

		override fun bindView(model: SentenceListModel, binding: ListItemSentenceSearchBinding) =
			with(binding) {
				listItem = SentenceListItem(model)
				viewHolder = this@ViewHolder
			}

		override fun clickAction(id: String) = mFragment.navigate(
			SentenceSearchListFragmentDirections
				.sentenceSearchListToSentenceDetailRead(id))
	}
}