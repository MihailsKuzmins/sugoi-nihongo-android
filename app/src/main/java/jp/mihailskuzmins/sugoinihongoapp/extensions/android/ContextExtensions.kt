package jp.mihailskuzmins.sugoinihongoapp.extensions.android

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import androidx.annotation.DimenRes
import androidx.core.content.FileProvider
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.components.AppInjector
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.components.DaggerAppInjector
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules.RepoModule
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules.RoomRepoModule
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules.StorageModule
import jp.mihailskuzmins.sugoinihongoapp.dependencyinjection.modules.UtilModule
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import java.io.File
import java.net.URLConnection

val Context.backupDirectory: File
	get() = File(getExternalFilesDir(null), "Backups")

val Context.backupSyncDirectory: File
	get() = File(filesDir, "BackupsSync")

fun Context.createAppInjector(): AppInjector =
	DaggerAppInjector
		.builder()
		.repoModule(RepoModule)
		.storageModule(StorageModule.getInstance(this))
		.utilModule(UtilModule.getInstance(this))
		.roomRepoModule(RoomRepoModule.getInstance(this))
		.build()

fun Context.browse(url: String) {
	val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
		.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

	startActivity(intent)
}

fun Context.getResource(resourceId: Int): Int {
	val out = TypedValue()
	theme.resolveAttribute(resourceId, out, true)

	return out.resourceId
}

fun Context.getDimen(@DimenRes dimenRes: Int): Float {
	val dimen = resources.getDimension(dimenRes)
	val density = resources.displayMetrics.density

	return dimen / density
}

fun Context.share(file: File) {
	if (!file.exists())
		return

	val sharingFileText = getString(R.string.general_share_file)
	val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)

	Intent(Intent.ACTION_SEND)
		.setType(URLConnection.guessContentTypeFromName(file.name))
		.putExtra(Intent.EXTRA_STREAM, uri)
		.putExtra(Intent.EXTRA_SUBJECT, sharingFileText)
		.putExtra(Intent.EXTRA_TEXT, sharingFileText)
		.invoke { startActivity(Intent.createChooser(it, getString(R.string.general_share_file))) }
}