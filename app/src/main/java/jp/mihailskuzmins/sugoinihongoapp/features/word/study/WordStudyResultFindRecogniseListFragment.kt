package jp.mihailskuzmins.sugoinihongoapp.features.word.study


import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentWordStudyResultFindRecogniseBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.application
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListFragmentBase
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.room.WordQuizQuestionResultEntity

class WordStudyResultFindRecogniseListFragment :
	ListFragmentBase<WordQuizQuestionResultEntity, WordStudyResultFindRecogniseListViewModel>(R.layout.fragment_word_study_result_find_recognise) {

	@BindView(R.id.fragment_word_study_result_find_recognise_word_recycler_view)
	override lateinit var recyclerView: RecyclerView

	override fun getNavigationId() =
		R.id.wordStudyResultFindRecogniseListFragment

	override fun createViewModel() =
		provideViewModel {
			val wordQuizId = WordStudyResultFindRecogniseListFragmentArgs
				.fromBundle(arguments!!)
				.wordQuizId

			WordStudyResultFindRecogniseListViewModel(application, wordQuizId)
		}

	override fun createRecyclerViewAdapter() =
		WordStudyResultFindRecogniseListAdapter(context!!)

	override fun initRecyclerView() {
		super.initRecyclerView()
		ViewCompat.setNestedScrollingEnabled(recyclerView, false)
	}

	override fun bindView() {
		getBinding<FragmentWordStudyResultFindRecogniseBinding>().viewModel = viewModel
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
			isOpenSettingsAvailable = false
		}
}
