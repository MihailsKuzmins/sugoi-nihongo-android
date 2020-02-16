package jp.mihailskuzmins.sugoinihongoapp.extensions

import android.os.Build
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.util.zip.ZipOutputStream

fun File.writeToZip(zipOutputStream: ZipOutputStream) {
	FileInputStream(this).use {
		it.readBytes()
			.run { zipOutputStream.write(this) }
	}
}

fun File.getTimeCreated(): Long {
	// Below API 26, there is no method to get CreationDate
	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
		return lastModified()

	return Files.readAttributes(toPath(), BasicFileAttributes::class.java)
		.creationTime()
		.toMillis()
}