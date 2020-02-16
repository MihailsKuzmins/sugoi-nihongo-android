package jp.mihailskuzmins.sugoinihongoapp.features.settings.about

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewStub
import androidx.databinding.DataBindingUtil
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.ListItemSettingsThirdPartyLibraryBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.invokeIf
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterViewHolderBase
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.asset.ThirdPartyLibraryModel
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.asset.toLayoutResource
import jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems.SettingsThirdPartyLibraryListItem

class SettingsThirdPartyLibraryListAdapter :
	ListAdapterBase<ThirdPartyLibraryModel, ListItemSettingsThirdPartyLibraryBinding>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		DataBindingUtil.inflate<ListItemSettingsThirdPartyLibraryBinding>(
				LayoutInflater.from(parent.context), R.layout.list_item_settings_third_party_library, parent, false)
			.run(::ViewHolder)


	inner class ViewHolder(binding: ListItemSettingsThirdPartyLibraryBinding) :
		ListAdapterViewHolderBase<ThirdPartyLibraryModel, ListItemSettingsThirdPartyLibraryBinding>(binding) {

		override fun bindView(model: ThirdPartyLibraryModel, binding: ListItemSettingsThirdPartyLibraryBinding) {
			binding.listItem = SettingsThirdPartyLibraryListItem(model.name, model.copyright, model.projectUrl)

			binding
				.root
				.findViewById<ViewStub>(R.id.list_item_settings_third_party_library_view_stub)
				.invokeIf({ it?.layoutResource == 0 }, {
					it.layoutResource = model.attribution.toLayoutResource()
					it.inflate()
				})
		}
	}
}