package jp.mihailskuzmins.sugoinihongoapp.userinterface.controls.tableview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewStub
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.getDimen
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.getResource
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.removeViewsFrom


class TableView : LinearLayout {

	private val mTitleTextView: TextView
	private val mHeaderRowView: ViewStub
	private val mItemsLinearLayout: LinearLayout

	constructor(context: Context) : super(context)
	constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
	constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)

	init {
		orientation = VERTICAL

		val spacingVertical = context.getDimen(R.dimen.spacing_vertical).toInt()

		mTitleTextView = TextView(context).apply {
			isVisible = false
			textSize = context.getDimen(R.dimen.font_size_text)

			layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
				topMargin = spacingVertical
				bottomMargin = spacingVertical
				textAlignment = TEXT_ALIGNMENT_CENTER
			}
		}

		mHeaderRowView = ViewStub(context).apply {
			isVisible = false
		}

		mItemsLinearLayout = LinearLayout(context).apply {
			orientation = VERTICAL
			showDividers = SHOW_DIVIDER_MIDDLE or SHOW_DIVIDER_BEGINNING or SHOW_DIVIDER_END
			dividerDrawable = getDividerResourceDrawable()
		}

		addView(mTitleTextView)
		addView(mItemsLinearLayout)
	}

	fun setTitle(@StringRes stringRes: Int) {
		mTitleTextView.apply {
			text = context.getString(stringRes)
			isVisible = true
		}
	}

	fun setHeaderRow(@LayoutRes layoutRes: Int) {
		if (hasHeaderRow)
			return

		mHeaderRowView
			.apply { layoutResource = layoutRes }
			.run {
				mItemsLinearLayout.addView(this, 0)
				isVisible = true
			}
	}

	fun <TModel, TBinding: ViewDataBinding> setItems(adapter: TableViewAdapterBase<TModel, TBinding>) {
		val skipViews = if (hasHeaderRow) 1
			else 0

		mItemsLinearLayout.removeViewsFrom(skipViews)

		for (position in 0 until adapter.getItemsCount()) {
			val view = adapter
				.onCreateViewHolder(this, adapter.getItemType(position))
				.apply { adapter.onBindViewHolder(this, position) }
				.run { view }

			mItemsLinearLayout.addView(view)
		}
	}

	private val hasHeaderRow: Boolean
		get() = mHeaderRowView.layoutResource != 0

	private fun getDividerResourceDrawable() =
		context
			.getResource(android.R.attr.listDivider)
			.run { context.getDrawable(this) }
}