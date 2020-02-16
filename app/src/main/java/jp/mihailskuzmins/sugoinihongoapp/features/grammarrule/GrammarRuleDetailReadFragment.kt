package jp.mihailskuzmins.sugoinihongoapp.features.grammarrule


import android.view.Menu
import android.view.MenuInflater
import butterknife.OnClick
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentGrammarRuleDetailReadBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.application
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigate
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.confirmDelete
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToClipboard
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToDeleteDialog
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNavigationBack
import jp.mihailskuzmins.sugoinihongoapp.resources.NavMode

class GrammarRuleDetailReadFragment : FragmentBase<GrammarRuleDetailReadViewModel>(R.layout.fragment_grammar_rule_detail_read) {

	private val mGrammarRuleId: String
		get() = GrammarRuleDetailReadFragmentArgs
			.fromBundle(arguments!!)
			.grammarRuleId

	override fun getNavigationId() = R.id.grammarRuleDetailReadFragment

	override fun createViewModel() =
		provideViewModel {
			GrammarRuleDetailReadViewModel(application, mGrammarRuleId)
		}

	override fun bindView() {
		getBinding<FragmentGrammarRuleDetailReadBinding>().viewModel = viewModel
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

	@OnClick(R.id.fragment_grammar_rule_detail_read_fab)
	fun onFabClick() {
		val title = context!!.getString(R.string.grammar_rule_edit_grammar_rule)

		navigate(GrammarRuleDetailReadFragmentDirections
			.grammarRuleDetailReadToGrammarRuleDetailEdit(viewModel.grammarRuleIdItem.value, title))
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)

		inflater.inflate(R.menu.menu_grammar_rule_detail_read, menu)
	}

	override fun getOptionsItemSelectedFunction(itemId: Int) = when (itemId) {
		R.id.menu_grammar_rule_detail_read_menu_item_delete -> { -> confirmDelete(R.string.grammar_rule_delete_confirmation) }
		else -> super.getOptionsItemSelectedFunction(itemId)
	}
}
