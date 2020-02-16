package jp.mihailskuzmins.sugoinihongoapp.extensions.android

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import jp.mihailskuzmins.sugoinihongoapp.features.base.ViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.ViewModelFactory

val Fragment.application: Application
	get() = activity!!.application

fun Fragment.invalidateOptionsMenu() =
	activity!!.invalidateOptionsMenu()

fun Fragment.finishActivity() =
	activity!!.finish()

inline fun <reified T: ViewModelBase> Fragment.provideViewModel(noinline factoryFunc: (() -> T)? = null): T {
	val provider = if (factoryFunc == null) {
		ViewModelProvider(this)
	} else {
		ViewModelProvider(this, ViewModelFactory(factoryFunc))
	}

	return provider.get(T::class.java)
}