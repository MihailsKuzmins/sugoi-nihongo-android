package jp.mihailskuzmins.sugoinihongoapp.extensions

import android.os.Build
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun Date.toDate(): Date {
	val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

	return formatter
		.parse(formatter.format(this))
}

fun Date.formatDate(format: Int = DateFormat.LONG): String =
	DateFormat
		.getDateInstance(format, Locale.getDefault())
		.format(this)

fun Date.formatDateTime(dateFormat: Int = DateFormat.LONG, timeFormat: Int = DateFormat.LONG): String =
	DateFormat
		.getDateTimeInstance(dateFormat, timeFormat, Locale.getDefault())
		.format(this)

fun Date.formatDateTime(pattern: String): String =
	SimpleDateFormat(pattern, Locale.getDefault())
		.run { format(this@formatDateTime) }

fun Date.format(pattern: String): String =
	SimpleDateFormat(pattern, Locale.getDefault())
		.format(this)

fun Date.add(field: Int, amount: Int): Date {
	val calendar = Calendar.getInstance()
	calendar.time = this
	calendar.add(field, amount)

	return calendar.time
}

fun dateFrom(year: Int, month: Int, day: Int): Date {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
		val instant = LocalDate
			.of(year, month, day)
			.atStartOfDay()
			.atZone(ZoneId.systemDefault())
			.toInstant()

		return Date.from(instant)
	}

	val calendar = Calendar.getInstance(Locale.getDefault())
	calendar.set(year, month, day)

	return calendar.time
}