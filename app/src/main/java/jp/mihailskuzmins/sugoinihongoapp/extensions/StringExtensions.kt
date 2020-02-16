package jp.mihailskuzmins.sugoinihongoapp.extensions

import java.io.File
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

fun String.removeWhiteSpaces() =
	this.trim()
		.replace("\\s+".toRegex(), " ")

fun String.toLowerCaseEx() =
	this.toLowerCase(Locale.getDefault())

fun String.toIntOrNullEx(): Int? {
	if (isBlank())
		return 0

	return toIntOrNull()
}

fun String.hashMd5() =
	MessageDigest.getInstance("MD5")
		.digest(this.toByteArray(StandardCharsets.UTF_8))
		.run { String(this) }

fun String.trimWithEllipsis(maxLength: Int): String {
	val length = minOf(this.length, maxLength)

	return substring(0 until length)
		.runIf(this.length > maxLength) { plus("...") }
}

fun StringBuilder.allIndicesOf(vararg chars: Char): List<Int> {
	val list = mutableListOf<Int>()

	this.forEachIndexed { i, x ->
		if (x in chars)
			list.add(i)
	}

	return list
}

fun combinePath(vararg strings: String): String =
	strings.joinToString(File.separator)
