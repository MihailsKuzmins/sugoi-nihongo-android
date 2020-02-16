package jp.mihailskuzmins.sugoinihongoapp.activities

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.setThemeMode
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.AppFragment
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasOverriddenBackNavigation
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.SharedPreferencesUtil
import javax.inject.Inject

abstract class ActivityBase : AppCompatActivity() {

    private val mCleanUp = CompositeDisposable()

    override fun onStart() {
        super.onStart()
        initSubscriptions(mCleanUp)
    }

    override fun onStop() {
        mCleanUp.clear()
        super.onStop()
    }

    override fun onDestroy() {
        mCleanUp.dispose()
        super.onDestroy()
    }

    final override fun onBackPressed() {
        val fragment = getCurrentFragment()

        if (!fragment.beforeNavigatingBack())
            return

        if (fragment is HasOverriddenBackNavigation && fragment.overrideBackNavigation())
            return

        super.onBackPressed()
    }

    protected open fun initSubscriptions(d: CompositeDisposable) {}

    protected fun getCurrentFragment() =
        supportFragmentManager
            .primaryNavigationFragment!!
            .childFragmentManager
            .fragments[0] as AppFragment
}