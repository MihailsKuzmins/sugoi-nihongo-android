package jp.mihailskuzmins.sugoinihongoapp.features.sentence


import android.view.Menu
import android.view.MenuInflater
import butterknife.OnClick
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentSentenceDetailReadBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.application
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigate
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.confirmDelete
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToClipboard
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToDeleteDialog
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNavigationBack
import jp.mihailskuzmins.sugoinihongoapp.resources.NavMode

class SentenceDetailReadFragment : FragmentBase<SentenceDetailReadViewModel>(R.layout.fragment_sentence_detail_read) {

	override fun getNavigationId() =
		R.id.sentenceDetailReadFragment

	override fun createViewModel() =
		provideViewModel {
			val sentenceId = SentenceDetailReadFragmentArgs
				.fromBundle(arguments!!)
				.sentenceId

			SentenceDetailReadViewModel(application, sentenceId)
		}

	override fun bindView() {
		getBinding<FragmentSentenceDetailReadBinding>().viewModel = viewModel
	}

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		subscribeToClipboard()
			.addTo(d)

		subscribeToNavigationBack(NavMode.Main)
			.addTo(d)

		subscribeToDeleteDialog()
			.addTo(d)
	}

	@OnClick(R.id.fragment_sentence_detail_read_fab)
	fun onFabClick() {
		val title = context!!.getString(R.string.sentence_edit_sentence)

		navigate(SentenceDetailReadFragmentDirections
			.sentenceDetailReadToSentenceDetailEdit(viewModel.sentenceIdItem.value, title))
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)

		inflater.inflate(R.menu.menu_sentence_detail_read, menu)
	}

	override fun getOptionsItemSelectedFunction(itemId: Int) =
		when (itemId) {
			R.id.menu_sentence_detail_read_menu_item_delete -> { -> confirmDelete(R.string.sentence_delete_confirmation) }
			else -> super.getOptionsItemSelectedFunction(itemId)
		}
}
