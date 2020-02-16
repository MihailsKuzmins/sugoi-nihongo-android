package jp.mihailskuzmins.sugoinihongoapp.features.word


import butterknife.OnClick
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentWordDetailEditBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.application
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNavigationBack
import jp.mihailskuzmins.sugoinihongoapp.resources.NavMode

class WordDetailEditFragment : FragmentBase<WordDetailEditViewModel>(R.layout.fragment_word_detail_edit) {

	override fun getNavigationId() =
		R.id.wordDetailEditFragment

	override fun createViewModel() =
		provideViewModel {
			val wordId = WordDetailEditFragmentArgs
				.fromBundle(arguments!!)
				.wordId ?: ""

			WordDetailEditViewModel(application, wordId)
		}

	override fun bindView() {
		getBinding<FragmentWordDetailEditBinding>().viewModel = viewModel
	}

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		subscribeToNavigationBack(NavMode.Main)
			.addTo(d)

		viewModel.onSaveErrorAlertDialog
			.subscribe(this)
			.addTo(d)
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
		}

	@OnClick(R.id.fragment_word_detail_edit_fab)
	fun onFabClick() =
		viewModel.save()
}
