package jp.mihailskuzmins.sugoinihongoapp.features.settings.backup

import android.app.Application
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.backupDirectory
import jp.mihailskuzmins.sugoinihongoapp.extensions.getTimeCreated
import jp.mihailskuzmins.sugoinihongoapp.extensions.isBackupFile
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ItemsAction
import jp.mihailskuzmins.sugoinihongoapp.features.base.list.ListViewModelBase
import jp.mihailskuzmins.sugoinihongoapp.helpers.rx.RxFileObserver
import jp.mihailskuzmins.sugoinihongoapp.persistence.models.miscellaneous.SettingsLocalBackupListModel

class SettingsLocalBackupListViewModel(app: Application) : ListViewModelBase<SettingsLocalBackupListModel>(app) {

	override fun initItems(itemsAction: ItemsAction<SettingsLocalBackupListModel>) {
		val backupFilesObservable = RxFileObserver(context.backupDirectory, ::isBackupFile)
			.filesObservable

		backupFilesObservable
			.map { it
				.map { x -> SettingsLocalBackupListModel(x.name, x.length(), x.getTimeCreated()) }
				.sortedByDescending(SettingsLocalBackupListModel::timeCreated)
			}
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { it.run(itemsAction) }
			.addTo(cleanUp)

		backupFilesObservable
			.connect()
			.addTo(cleanUp)
	}
}