package jp.mihailskuzmins.sugoinihongoapp.extensions

import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants
import java.io.File
import java.net.InetAddress

@Suppress("UNUSED_PARAMETER")
fun isBackupFile(directory: File, name: String) =
	name.endsWith(AppConstants.backupExtension)

fun isInternetAvailable() = try {
		InetAddress
			.getByName("google.com")
			.run { !hostName.isNullOrEmpty() }
	} catch(ex: Exception) {
		false
}

inline fun <reified R> Any.getPrivateField(name: String): R =
	this.javaClass.getDeclaredField(name)
		.apply { isAccessible = true }
		.run { get(this@getPrivateField) }
		.run(R::class.java::cast)!!