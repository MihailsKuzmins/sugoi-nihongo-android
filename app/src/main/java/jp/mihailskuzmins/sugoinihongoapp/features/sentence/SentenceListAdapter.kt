package jp.mihailskuzmins.sugoinihongoapp.features.sentence

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.ListItemSentenceBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigate
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.AppFragment
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasClickAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterViewHolderBase
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.sentence.SentenceListModel
import jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems.SentenceListItem

class SentenceListAdapter(private val mFragment: AppFragment) :
	ListAdapterBase<SentenceListModel, ListItemSentenceBinding>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		DataBindingUtil.inflate<ListItemSentenceBinding>(
				LayoutInflater.from(parent.context), R.layout.list_item_sentence, parent, false)
			.run(::ViewHolder)


	inner class ViewHolder(binding: ListItemSentenceBinding) :
		ListAdapterViewHolderBase<SentenceListModel, ListItemSentenceBinding>(binding), HasClickAction {

		override fun bindView(model: SentenceListModel, binding: ListItemSentenceBinding) =
			with (binding) {
				listItem = SentenceListItem(model)
				viewHolder = this@ViewHolder
			}

		override fun clickAction(id: String) = mFragment.navigate(
			SentenceListFragmentDirections
				.sentenceListToSentenceDetailRead(id))
	}
}