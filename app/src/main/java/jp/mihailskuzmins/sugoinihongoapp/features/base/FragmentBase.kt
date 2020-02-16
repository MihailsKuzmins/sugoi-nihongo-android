package jp.mihailskuzmins.sugoinihongoapp.features.base

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.hideKeyboard
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigate
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.AppFragment
import jp.mihailskuzmins.sugoinihongoapp.helpers.AnonymousDisposable
import jp.mihailskuzmins.sugoinihongoapp.helpers.rx.RxBus
import jp.mihailskuzmins.sugoinihongoapp.resources.Action
import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants.RxBusContracts
import java.util.concurrent.TimeUnit

abstract class FragmentBase<TViewModel: ViewModelBase>(@LayoutRes private val layoutId: Int) :
	Fragment(), AppFragment {

	private val mCleanUp = CompositeDisposable()
	private lateinit var mBinding: ViewDataBinding

	private lateinit var mFragmentConfig: FragmentConfig

	lateinit var viewModel: TViewModel
		private set

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setHasOptionsMenu(true)
		mFragmentConfig = createConfig()

		viewModel = createViewModel()
			.apply { activator.activate() }
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
		val unbinder = ButterKnife.bind(this, mBinding.root)

		AnonymousDisposable { unbinder.unbind() }
			.addTo(mCleanUp)

		return mBinding.root
	}

	override fun onStart() {
		super.onStart()
		initSubscriptions(mCleanUp)
		initConfig()
	}

	override fun onPause() {
		activity!!.hideKeyboard()
		super.onPause()
	}


	override fun onDestroyView() {
		mCleanUp.clear()
		super.onDestroyView()
	}

	override fun onDestroy() {
		mCleanUp.dispose()
		super.onDestroy()
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)

		if (mFragmentConfig.isOpenSettingsAvailable)
			inflater.inflate(R.menu.menu_open_settings, menu)
	}

	final override fun onOptionsItemSelected(item: MenuItem): Boolean {
		getOptionsItemSelectedFunction(item.itemId)
			?.let {
				it()
				return@onOptionsItemSelected true
			}

		return false
	}

	open fun getOptionsItemSelectedFunction(itemId: Int): Action? =
		when (itemId) {
			R.id.menu_open_settings_menu_item_settings -> { -> navigate(R.id.any_to_settingsMain) }
			else -> null
		}

	override fun beforeNavigatingBack() = true

	@Suppress("UNCHECKED_CAST")
	protected fun <TBinding: ViewDataBinding> getBinding() =
		mBinding as TBinding

	protected open fun initSubscriptions(d: CompositeDisposable) {
		viewModel.isInitialised
			.filter { it }
			.take(1)
			.subscribe { bindView() }
			.addTo(d)

		Observables
			.combineLatest(
				Observable.timer(250, TimeUnit.MILLISECONDS),
				viewModel.isInitialised.startWith(false))
				{ _, y -> y.not() }
			.distinctUntilChanged()
			.subscribe { RxBus.sentMessage(it, Boolean::class.java, RxBusContracts.progressBarVisible) }
			.addTo(d)
	}

	protected abstract fun bindView()
	protected abstract fun createViewModel(): TViewModel

	protected open fun createConfig() =
		FragmentConfig()

	private fun initConfig() {
		RxBus.sentMessage(mFragmentConfig.isBottomNavVisible, Boolean::class.java, RxBusContracts.bottomNavVisible)
	}
}