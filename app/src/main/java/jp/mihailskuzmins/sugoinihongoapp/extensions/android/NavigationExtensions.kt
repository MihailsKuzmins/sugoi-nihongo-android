package jp.mihailskuzmins.sugoinihongoapp.extensions.android

import android.app.Activity
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.extensions.hasValue
import jp.mihailskuzmins.sugoinihongoapp.extensions.invokeIfTrue
import jp.mihailskuzmins.sugoinihongoapp.extensions.runIf
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.AppFragment
import jp.mihailskuzmins.sugoinihongoapp.resources.NavMode

fun AppFragment.navigate(@IdRes resourceId: Int, @IdRes popUpTo: Int? = null, navMode: NavMode = NavMode.Main) {

	fun navigate() {
		val navOptions = createNavOptions(popUpTo)

		activity!!
			.findNavController(navMode)
			.navigate(resourceId, Bundle(), navOptions)
	}

	canNavigate(navMode)
		.invokeIfTrue(::navigate)
}

fun AppFragment.navigate(navDirections: NavDirections, @IdRes popUpTo: Int? = null, navMode: NavMode = NavMode.Main) {

	fun navigate() {
		val navOptions = createNavOptions(popUpTo)

		activity!!
			.findNavController(navMode)
			.navigate(navDirections, navOptions)
	}

	canNavigate(navMode)
		.invokeIfTrue(::navigate)
}

fun Activity.navigateUp(fragment: AppFragment, navMode: NavMode = NavMode.Main): Boolean {
	if (!canNavigate(fragment, navMode))
		return false

	return findNavController(navMode)
		.navigateUp()
}

fun AppFragment.navigateUp(navMode: NavMode = NavMode.Main) =
	this.activity!!
		.navigateUp(this, navMode)

private fun Activity.canNavigate(fragment: AppFragment, navMode: NavMode) =
	findNavController(navMode).currentDestination?.id == fragment.navigationId

private fun AppFragment.canNavigate(navMode: NavMode) =
	activity!!.findNavController(navMode).currentDestination?.id == navigationId

private fun Activity.findNavController(navMode: NavMode): NavController {
	val id = when (navMode) {
		NavMode.Main -> R.id.activity_main_navigation_fragment
		NavMode.SignInSignUp -> R.id.activity_sign_in_sign_up_navigation_fragment
	}

	return findNavController(id)
}

private fun createNavOptions(@IdRes popUpTo: Int?) =
	NavOptions.Builder()
		.setEnterAnim(R.anim.slide_in_right)
		.setExitAnim(R.anim.slide_out_left)
		.setPopEnterAnim(R.anim.slide_in_left)
		.setPopExitAnim(R.anim.slide_out_right)
		.runIf(popUpTo.hasValue) { setPopUpTo(popUpTo!!, false) }
		.build()