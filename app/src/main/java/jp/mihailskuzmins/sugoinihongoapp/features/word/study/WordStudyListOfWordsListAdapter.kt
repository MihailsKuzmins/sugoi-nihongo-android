package jp.mihailskuzmins.sugoinihongoapp.features.word.study

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.DialogWordStudyWordBinding
import jp.mihailskuzmins.sugoinihongoapp.databinding.ListItemWordStudyListOfWordsBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterViewHolderBase
import jp.mihailskuzmins.sugoinihongoapp.helpers.dialoglifecycle.configs.AlertViewConfig
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordStudyModel

class WordStudyListOfWordsListAdapter(private val mViewModel: WordStudyListOfWordsListViewModel) :
	ListAdapterBase<WordStudyModel, ListItemWordStudyListOfWordsBinding>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		DataBindingUtil.inflate<ListItemWordStudyListOfWordsBinding>(
				LayoutInflater.from(parent.context), R.layout.list_item_word_study_list_of_words, parent, false)
			.run(::ViewHolder)

	inner class ViewHolder(binding: ListItemWordStudyListOfWordsBinding) :
		ListAdapterViewHolderBase<WordStudyModel, ListItemWordStudyListOfWordsBinding>(binding) {

		override fun bindView(model: WordStudyModel, binding: ListItemWordStudyListOfWordsBinding) =
			with(binding) {
				this.model = model
				viewHolder = this@ViewHolder
			}

		fun clickAction(model: WordStudyModel) =
			AlertViewConfig<WordStudyModel, DialogWordStudyWordBinding>(
					R.layout.dialog_word_study_word, model) { x, binding -> binding.model = x }
				.invoke { mViewModel.wordDialog.config = it }
	}
}