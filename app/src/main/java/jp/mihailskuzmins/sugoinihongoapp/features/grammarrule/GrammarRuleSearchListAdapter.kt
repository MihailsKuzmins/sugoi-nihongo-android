package jp.mihailskuzmins.sugoinihongoapp.features.grammarrule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.ListItemGrammarRuleSearchBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigate
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.AppFragment
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasClickAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterViewHolderBase
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.grammarrule.GrammarRuleListModel
import jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems.GrammarRuleListItem

class GrammarRuleSearchListAdapter(private val mFragment: AppFragment) :
	ListAdapterBase<GrammarRuleListModel, ListItemGrammarRuleSearchBinding>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		DataBindingUtil.inflate<ListItemGrammarRuleSearchBinding>(
				LayoutInflater.from(parent.context), R.layout.list_item_grammar_rule_search, parent, false)
			.run(::ViewHolder)


	inner class ViewHolder(binding: ListItemGrammarRuleSearchBinding) :
		ListAdapterViewHolderBase<GrammarRuleListModel, ListItemGrammarRuleSearchBinding>(binding), HasClickAction {

		override fun bindView(model: GrammarRuleListModel, binding: ListItemGrammarRuleSearchBinding) =
			with (binding) {
				listItem = GrammarRuleListItem(model)
				viewHolder = this@ViewHolder
			}

		override fun clickAction(id: String) = mFragment.navigate(
			GrammarRuleSearchListFragmentDirections
				.grammarRuleSearchListToGrammarRuleDetailRead(id))
	}
}