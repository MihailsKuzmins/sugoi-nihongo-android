package jp.mihailskuzmins.sugoinihongoapp.features.sentence


import butterknife.OnClick
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentSentenceDetailEditBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.application
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNavigationBack
import jp.mihailskuzmins.sugoinihongoapp.resources.NavMode

class SentenceDetailEditFragment : FragmentBase<SentenceDetailEditViewModel>(R.layout.fragment_sentence_detail_edit) {

	override fun getNavigationId() =
		R.id.sentenceDetailEditFragment

	override fun createViewModel() =
		provideViewModel {
			val sentenceId = SentenceDetailEditFragmentArgs
				.fromBundle(arguments!!)
				.sentenceId ?: ""

			SentenceDetailEditViewModel(application, sentenceId)
		}

	override fun bindView() {
		getBinding<FragmentSentenceDetailEditBinding>().viewModel = viewModel
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

	@OnClick(R.id.fragment_sentence_detail_edit_fab)
	fun onFabClick() =
		viewModel.save()
}
