package jp.mihailskuzmins.sugoinihongoapp.features.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory<T: ViewModelBase>(private val mCreator: () -> T) : ViewModelProvider.Factory {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>)
			= mCreator() as T
}