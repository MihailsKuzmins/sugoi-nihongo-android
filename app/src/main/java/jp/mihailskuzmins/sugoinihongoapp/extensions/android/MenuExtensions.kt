package jp.mihailskuzmins.sugoinihongoapp.extensions.android

import android.view.Menu
import androidx.annotation.IdRes
import androidx.appcompat.widget.SearchView

fun Menu.setItemEnabled(@IdRes menuItemId: Int, isEnabled: Boolean) {
	findItem(menuItemId).apply {
		this.isEnabled = isEnabled

		icon?.apply {
			alpha = if (isEnabled) 255
				else 50
		}
	}
}

fun Menu.findSearchView(@IdRes menuItemId: Int) =
	findItem(menuItemId).actionView as SearchView