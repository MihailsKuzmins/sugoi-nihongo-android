package jp.mihailskuzmins.sugoinihongoapp.features.settings.backup

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.databinding.ListItemSettingsBackupBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.backupDirectory
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.share
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces.HasClickAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListAdapterViewHolderBase
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.miscellaneous.SettingsLocalBackupListModel
import jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems.SettingsLocalBackupListItem
import java.io.File

class SettingsLocalBackupListAdapter (private val mContext: Context) :
	ListAdapterBase<SettingsLocalBackupListModel, ListItemSettingsBackupBinding>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		DataBindingUtil.inflate<ListItemSettingsBackupBinding>(
				LayoutInflater.from(mContext), R.layout.list_item_settings_backup, parent, false)
			.run(::ViewHolder)


	inner class ViewHolder(binding: ListItemSettingsBackupBinding) :
		ListAdapterViewHolderBase<SettingsLocalBackupListModel, ListItemSettingsBackupBinding>(binding), HasClickAction {

		override fun bindView(model: SettingsLocalBackupListModel, binding: ListItemSettingsBackupBinding) =
			with(binding) {
				listItem = SettingsLocalBackupListItem(model, mContext)
				viewHolder = this@ViewHolder
			}

		override fun clickAction(id: String) =
			File(mContext.backupDirectory, id)
				.invoke(mContext::share)
	}
}