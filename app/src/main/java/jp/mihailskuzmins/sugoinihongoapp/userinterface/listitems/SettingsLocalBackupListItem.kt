package jp.mihailskuzmins.sugoinihongoapp.userinterface.listitems

import android.content.Context
import android.text.format.Formatter
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.miscellaneous.SettingsLocalBackupListModel

class SettingsLocalBackupListItem(
	model: SettingsLocalBackupListModel,
	context: Context) {

	val name = model.name
	val size: String = Formatter.formatShortFileSize(context, model.size)
}