package jp.mihailskuzmins.sugoinihongoapp.features.word.study

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.TableRowWordMarkBinding
import jp.mihailskuzmins.sugoinihongoapp.databinding.TableRowWordMarkTotalBinding
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.miscellaneous.WordMarkTableModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.miscellaneous.WordMarkTableRowModel
import jp.mihailskuzmins.sugoinihongoapp.userinterface.controls.tableview.TableViewAdapterBase

class WordStudySelectTypeMarkTableAdapter(
	items: List<WordMarkTableModel>
) : TableViewAdapterBase<WordMarkTableModel, ViewDataBinding>(items) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolderBase<WordMarkTableModel, out ViewDataBinding> =
		when (viewType) {
			ViewType.Row.viewType -> {
				val binding: TableRowWordMarkBinding = parent.inflate(R.layout.table_row_word_mark)
				WordQuizSelectTypeRowTableViewHolder(binding)
			}
			ViewType.Total.viewType -> {
				val binding: TableRowWordMarkTotalBinding = parent.inflate(R.layout.table_row_word_mark_total)
				WordQuizSelectTypeTotalTableViewHolder(binding)
			}
			else -> throw IllegalArgumentException("Out of range")
		}

	override fun getItemType(position: Int) =
		if (position == getItemsCount() - 1) ViewType.Total.viewType
			else ViewType.Row.viewType

	private fun <T: ViewDataBinding> ViewGroup.inflate(@LayoutRes layoutRes: Int): T =
		DataBindingUtil.inflate(
			LayoutInflater.from(context), layoutRes, this, false)


	class WordQuizSelectTypeRowTableViewHolder(
		binding: TableRowWordMarkBinding
	) : TableViewHolderBase<WordMarkTableModel, TableRowWordMarkBinding>(binding) {

		override fun bindView(model: WordMarkTableModel, binding: TableRowWordMarkBinding) {
			binding.mark = (model as WordMarkTableRowModel).mark
			binding.markCount = model.markCount
		}
	}

	class WordQuizSelectTypeTotalTableViewHolder(
		binding: TableRowWordMarkTotalBinding
	) : TableViewHolderBase<WordMarkTableModel, TableRowWordMarkTotalBinding>(binding) {

		override fun bindView(model: WordMarkTableModel, binding: TableRowWordMarkTotalBinding) {
			binding.markCount = model.markCount
		}
	}

	enum class ViewType(val viewType: Int) {
		Row(0),
		Total(1)
	}
}