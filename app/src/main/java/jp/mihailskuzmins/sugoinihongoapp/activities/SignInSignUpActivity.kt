package jp.mihailskuzmins.sugoinihongoapp.activities

import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.createAppInjector
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.helpers.rx.RxBus
import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants.RxBusContracts

class SignInSignUpActivity : ActivityBase() {

    @BindView(R.id.activity_sign_in_sign_up_progress_bar_linear_layout)
    lateinit var progressBarLinearLayout: LinearLayout

    @BindView(R.id.activity_sign_in_sign_up_bottom_navigation_view)
    lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_sign_up)
        ButterKnife.bind(this)

        findNavController(R.id.activity_sign_in_sign_up_navigation_fragment)
            .invoke(navView::setupWithNavController)
    }

    override fun initSubscriptions(d: CompositeDisposable) {
        val isAuthInProgress = RxBus.listen(Boolean::class.java, RxBusContracts.isAuthInProgress)
            .distinctUntilChanged()
            .publish()

        Observables
            .combineLatest(
                RxBus.listen(Boolean::class.java, RxBusContracts.bottomNavVisible),
                isAuthInProgress)
                { isVisible: Boolean, isAuth: Boolean -> isVisible && !isAuth }
                .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { navView.isVisible = it }
            .addTo(d)

        isAuthInProgress
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { progressBarLinearLayout.isVisible = it }
            .addTo(d)

        isAuthInProgress
            .connect()
            .addTo(d)
    }
}
