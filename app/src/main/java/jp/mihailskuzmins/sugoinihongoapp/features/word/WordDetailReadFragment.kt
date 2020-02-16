package jp.mihailskuzmins.sugoinihongoapp.features.word


import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import butterknife.OnClick
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentWordDetailReadBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.application
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.invalidateOptionsMenu
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigate
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.confirmDelete
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToClipboard
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToDeleteDialog
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNavigationBack
import jp.mihailskuzmins.sugoinihongoapp.resources.NavMode

class WordDetailReadFragment : FragmentBase<WordDetailReadViewModel>(R.layout.fragment_word_detail_read) {

	private var isFavourite: Boolean
		get() = viewModel.isFavouriteItem.value
		set(value) {
			viewModel.isFavouriteItem.value = value
		}

	override fun onPause() {
		viewModel.saveProps()
		super.onPause()
	}

	override fun getNavigationId() =
		R.id.wordDetailReadFragment

	override fun createViewModel() =
		provideViewModel {
			val wordId = WordDetailReadFragmentArgs
				.fromBundle(arguments!!)
				.wordId

			WordDetailReadViewModel(application, wordId)
		}

	override fun bindView() {
		getBinding<FragmentWordDetailReadBinding>().viewModel = viewModel
	}

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		viewModel.isFavouriteItem.valueChanged
			.distinctUntilChanged()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { invalidateOptionsMenu() }
			.addTo(d)

		subscribeToClipboard()
			.addTo(d)

		subscribeToNavigationBack(NavMode.Main)
			.addTo(d)

		subscribeToDeleteDialog()
			.addTo(d)
	}

	@OnClick(R.id.fragment_word_detail_read_fab)
	fun onFabClick() {
		val title = context!!.getString(R.string.word_edit_word)

		navigate(WordDetailReadFragmentDirections
			.wordDetailReadToWordDetailEdit(viewModel.wordIdItem.value, title))
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)

		inflater.inflate(R.menu.menu_word_detail_read, menu)
	}

	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)

		menu.findItem(R.id.menu_word_detail_read_favourites)
			.invoke { applyFavouritesToggle(it, isFavourite) }
	}

	override fun getOptionsItemSelectedFunction(itemId: Int) = when(itemId) {
		R.id.menu_word_detail_read_favourites -> { -> isFavourite = !isFavourite }
		R.id.menu_word_detail_read_menu_item_delete -> { -> confirmDelete(R.string.word_delete_confirmation) }
		else -> super.getOptionsItemSelectedFunction(itemId)
	}

	private fun applyFavouritesToggle(menuItem: MenuItem, isFavourite: Boolean) {
		if (menuItem.isChecked == isFavourite)
			return

		val iconRes = if (isFavourite) R.drawable.ic_favorite_red_24dp
		else R.drawable.ic_favorite_border_black_24dp

		val titleRes = if (isFavourite) R.string.general_remove_from_favourites
		else R.string.general_add_to_favourites

		with(menuItem) {
			isChecked = isFavourite
			icon = context!!.getDrawable(iconRes)
			title = context!!.getString(titleRes)
		}
	}
}
