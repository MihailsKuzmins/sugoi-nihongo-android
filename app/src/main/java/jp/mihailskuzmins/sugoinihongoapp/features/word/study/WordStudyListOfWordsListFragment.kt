package jp.mihailskuzmins.sugoinihongoapp.features.word.study


import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListFragmentBase
import jp.mihailskuzmins.sugoinihongoapp.helpers.DoubleTapToFinishHelper
import jp.mihailskuzmins.sugoinihongoapp.models.wordStudy.WordStudyModel

class WordStudyListOfWordsListFragment :
	ListFragmentBase<WordStudyModel, WordStudyListOfWordsListViewModel>(R.layout.fragment_word_study_list_of_words_list) {

	private lateinit var mDoubleTapToFinishHelper: DoubleTapToFinishHelper

	@BindView(R.id.fragment_word_study_list_of_words_list_recycler_view)
	override lateinit var recyclerView: RecyclerView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		mDoubleTapToFinishHelper = DoubleTapToFinishHelper(activity!!)
	}

	override fun getNavigationId() =
		R.id.wordStudyListOfWordsListFragment

	override fun createViewModel() =
		provideViewModel<WordStudyListOfWordsListViewModel>()

	override fun beforeNavigatingBack() =
		mDoubleTapToFinishHelper.canNavigateBack()

	override fun createRecyclerViewAdapter() =
		WordStudyListOfWordsListAdapter(viewModel)

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		viewModel.wordDialog
			.subscribe(this)
			.addTo(d)
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
			isOpenSettingsAvailable = false
		}
}
