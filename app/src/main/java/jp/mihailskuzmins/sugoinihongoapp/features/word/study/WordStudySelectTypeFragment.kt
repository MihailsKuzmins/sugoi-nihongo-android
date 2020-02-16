package jp.mihailskuzmins.sugoinihongoapp.features.word.study


import android.os.Bundle
import android.view.*
import butterknife.BindView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentWordStudySelectTypeBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigate
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.extensions.enums.toColor
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNavDirections
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNavigation
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.firestore.word.WordMarkModel.MarkState
import jp.mihailskuzmins.sugoinihongoapp.resources.NavMode
import jp.mihailskuzmins.sugoinihongoapp.userinterface.controls.colorbar.WordMarkColorBar
import jp.mihailskuzmins.sugoinihongoapp.userinterface.controls.tableview.TableView

class WordStudySelectTypeFragment : FragmentBase<WordStudySelectTypeViewModel>(R.layout.fragment_word_study_select_type) {

	@BindView(R.id.fragment_word_study_select_type_color_bar)
	lateinit var colorBar: WordMarkColorBar

	@BindView(R.id.fragment_word_study_select_type_table_view_mark)
	lateinit var markTableView: TableView

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = super.onCreateView(inflater, container, savedInstanceState)

		colorBar.initColors(
			getMarkStateAndColor(MarkState.Excellent),
			getMarkStateAndColor(MarkState.Good),
			getMarkStateAndColor(MarkState.Average),
			getMarkStateAndColor(MarkState.Bad),
			getMarkStateAndColor(MarkState.Excluded))

		markTableView.setTitle(R.string.general_mark_summary)
		markTableView.setHeaderRow(R.layout.table_header_word_mark)

		return view
	}

	override fun getNavigationId() =
		R.id.wordStudySelectTypeFragment

	override fun createViewModel() =
		provideViewModel<WordStudySelectTypeViewModel>()

	override fun bindView() {
		getBinding<FragmentWordStudySelectTypeBinding>().viewModel = viewModel
	}

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		viewModel.markStateMapObservable
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { colorBar.setItemWeights(it) }
			.addTo(d)

		viewModel.markMapObservable
			.observeOn(AndroidSchedulers.mainThread())
			.map { WordStudySelectTypeMarkTableAdapter(it) }
			.subscribe { markTableView.setItems(it) }
			.addTo(d)

		subscribeToNavigation(NavMode.Main)
			.addTo(d)

		subscribeToNavDirections(NavMode.Main)
			.addTo(d)
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
		}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)

		inflater.inflate(R.menu.menu_word_study_select_type, menu)
	}

	override fun getOptionsItemSelectedFunction(itemId: Int) = when(itemId) {
		R.id.menu_word_study_select_type_menu_item_history -> { ->
			WordStudySelectTypeFragmentDirections
				.wordStudySelectTypeToWordStudyHistoryFindRecognise()
				.invoke { navigate(it) }
		}
		else -> super.getOptionsItemSelectedFunction(itemId)
	}

	private fun getMarkStateAndColor(markState: MarkState): Pair<MarkState, Int> =
		markState to markState.toColor()
}
