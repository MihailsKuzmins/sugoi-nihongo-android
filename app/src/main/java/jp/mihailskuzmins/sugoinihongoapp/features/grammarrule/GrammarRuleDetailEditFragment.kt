package jp.mihailskuzmins.sugoinihongoapp.features.grammarrule


import butterknife.OnClick
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentGrammarRuleDetailEditBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.application
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig
import jp.mihailskuzmins.sugoinihongoapp.features.base.mixins.subscribeToNavigationBack
import jp.mihailskuzmins.sugoinihongoapp.resources.NavMode

class GrammarRuleDetailEditFragment : FragmentBase<GrammarRuleDetailEditViewModel>(R.layout.fragment_grammar_rule_detail_edit) {

	override fun getNavigationId() =
		R.id.grammarRuleDetailEditFragment

	override fun createViewModel() =
		provideViewModel {
			val grammarRuleId = GrammarRuleDetailEditFragmentArgs
				.fromBundle(arguments!!)
				.grammarRuleId ?: ""

			GrammarRuleDetailEditViewModel(application, grammarRuleId)
		}

	override fun bindView() {
		getBinding<FragmentGrammarRuleDetailEditBinding>().viewModel = viewModel
	}

	override fun initSubscriptions(d: CompositeDisposable) {
		super.initSubscriptions(d)

		subscribeToNavigationBack(NavMode.Main)
			.addTo(d)
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
		}

	@OnClick(R.id.fragment_grammar_rule_detail_edit_fab)
	fun onFabClick() =
		viewModel.save()
}
