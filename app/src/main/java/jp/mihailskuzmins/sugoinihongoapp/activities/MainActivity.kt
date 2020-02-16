package jp.mihailskuzmins.sugoinihongoapp.activities

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.createAppInjector
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigateUp
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasOverriddenBackNavigation
import jp.mihailskuzmins.sugoinihongoapp.helpers.rx.RxBus
import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants.RxBusContracts


class MainActivity : ActivityBase() {

	@BindView(R.id.activity_main_toolbar)
	lateinit var toolbar: Toolbar

	@BindView(R.id.activity_main_progress_bar_linear_layout)
	lateinit var progressBarLinearLayout: LinearLayout

	@BindView(R.id.activity_main_bottom_navigation_view)
	lateinit var navView: BottomNavigationView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		ButterKnife.bind(this)

		setSupportActionBar(toolbar)

		val navController = findNavController(R.id.activity_main_navigation_fragment)
		navView.setupWithNavController(navController)
		setupActionBarWithNavController(navController)
	}

	override fun onSupportNavigateUp(): Boolean {
		val fragment = getCurrentFragment()
		val canNavigateBack = fragment.beforeNavigatingBack()

		if (!canNavigateBack)
			return false

		if (fragment is HasOverriddenBackNavigation && fragment.overrideBackNavigation())
			return false

		return navigateUp(fragment)
	}

	override fun initSubscriptions(d: CompositeDisposable) {
		RxBus.listen(Boolean::class.java, RxBusContracts.bottomNavVisible)
			.distinctUntilChanged()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { navView.isVisible = it }
			.addTo(d)

		RxBus.listen(Boolean::class.java, RxBusContracts.progressBarVisible)
			.mergeWith(RxBus.listen(Boolean::class.java, RxBusContracts.isAuthInProgress))
			.distinctUntilChanged()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { progressBarLinearLayout.isVisible = it }
			.addTo(d)
	}
}
