package jp.mihailskuzmins.sugoinihongoapp.features.word.study

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.ListItemWordQuizBinding
import jp.mihailskuzmins.sugoinihongoapp.databinding.ListItemWordQuizHeaderBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigate
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.extensions.toDate
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.AppFragment
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterViewHolderBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.StickyListAdapterBase
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordQuizListModel
import jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems.WordQuizHeaderListItem
import jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems.WordQuizListItem

class WordStudyHistoryFindRecogniseListAdapter(
	private val mFragment: AppFragment
) : StickyListAdapterBase<WordQuizListModel, ListItemWordQuizBinding, ListItemWordQuizHeaderBinding>() {

	override fun onCreateViewHolder(parent: ViewGroup) =
		DataBindingUtil.inflate<ListItemWordQuizBinding>(
			LayoutInflater.from(parent.context), R.layout.list_item_word_quiz, parent, false)
			.run(::ViewHolder)

	override fun onCreateHeaderViewHolder(parent: ViewGroup) =
		DataBindingUtil.inflate<ListItemWordQuizHeaderBinding>(
			LayoutInflater.from(parent.context), R.layout.list_item_word_quiz_header, parent, false)
			.run(::HeaderViewHolder)

	override fun getHeaderId(model: WordQuizListModel) =
		model.date.toDate().time

	inner class ViewHolder(
		binding: ListItemWordQuizBinding
	) : ListAdapterViewHolderBase<WordQuizListModel, ListItemWordQuizBinding>(binding) {

		override fun bindView(model: WordQuizListModel, binding: ListItemWordQuizBinding) = with(binding) {
			listItem = WordQuizListItem(model)
			viewHolder = this@ViewHolder
		}

		fun clickAction(wordQuizId: Long) =
			WordStudyHistoryFindRecogniseListFragmentDirections
				.wordStudyHistoryFindRecogniseToWordStudyResultFindRecognise(wordQuizId)
				.invoke { mFragment.navigate(it) }
	}

	inner class HeaderViewHolder(
		binding: ListItemWordQuizHeaderBinding
	) : ListAdapterViewHolderBase<WordQuizListModel, ListItemWordQuizHeaderBinding>(binding) {

		override fun bindView(model: WordQuizListModel, binding: ListItemWordQuizHeaderBinding) {
			binding.listItem = WordQuizHeaderListItem(model)
		}
	}
}