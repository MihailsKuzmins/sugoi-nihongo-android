package jp.mihailskuzmins.sugoinihongoapp.features.word.study

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.ListItemWordStudyQuestionResultBinding
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterViewHolderBase
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.room.WordQuizQuestionResultEntity
import jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems.WordQuizQuestionResultListItem

class WordStudyResultFindRecogniseListAdapter(private val mContext: Context) :
	ListAdapterBase<WordQuizQuestionResultEntity, ListItemWordStudyQuestionResultBinding>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		DataBindingUtil.inflate<ListItemWordStudyQuestionResultBinding>(
				LayoutInflater.from(mContext), R.layout.list_item_word_study_question_result, parent, false)
			.run(::ViewHolder)


	inner class ViewHolder(binding: ListItemWordStudyQuestionResultBinding) :
		ListAdapterViewHolderBase<WordQuizQuestionResultEntity, ListItemWordStudyQuestionResultBinding>(binding) {

		override fun bindView(model: WordQuizQuestionResultEntity, binding: ListItemWordStudyQuestionResultBinding) {
			binding.listItem = WordQuizQuestionResultListItem(mContext, model)
		}
	}
}