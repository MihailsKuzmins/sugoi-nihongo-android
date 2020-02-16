package jp.mihailskuzmins.sugoinihongoapp.persistence.repos.asset

import android.content.Context
import jp.mihailskuzmins.sugoinihongoapp.extensions.allIndicesOf
import jp.mihailskuzmins.sugoinihongoapp.extensions.combinePath
import jp.mihailskuzmins.sugoinihongoapp.extensions.runIf
import jp.mihailskuzmins.sugoinihongoapp.extensions.toLowerCaseEx


private const val keyValueDelimiter = ","
private const val choonpuChar = 'ー'

class KanaRepo(context: Context) : AssetRepoBase(context) {

	private val mKanaToRomajiMap by lazy { loadTable(ConversionType.KanaToRomaji) }
	private val mRomajiToHiraganaMap by lazy { loadTable(ConversionType.RomajiToHiragana) }
	private val mRomajiToKatakanaMap by lazy { loadTable(ConversionType.RomajiToKatakana) }

	fun romajiToKatakana(romajiString: String) = StringBuilder(romajiString.toLowerCaseEx())
		.run(::replaceLongVowelsWithChoonpu)
		.run { replaceDoubleConsonantsWithSokuon(this, Kana.Katakana) }
		.run { replaceStringFromConversionTable(this, ConversionType.RomajiToKatakana) }
		.toString()

	fun romajiToHiragana(romajiString: String) = StringBuilder(romajiString.toLowerCaseEx())
		.run { replaceDoubleConsonantsWithSokuon(this, Kana.Hiragana) }
		.run { replaceStringFromConversionTable(this, ConversionType.RomajiToHiragana) }
		.toString()

	fun kanaToRomaji(kanaString: String) = StringBuilder(kanaString)
		.run { replaceStringFromConversionTable(this, ConversionType.KanaToRomaji) }
		.run(::replaceChoonpuWithLongVowels)
		.run(::replaceSokuonWithDoubleConsonants)
		.toString()

	internal fun replaceStringFromConversionTable(stringBuilder: StringBuilder, conversionType: ConversionType): StringBuilder {
		var offset = 0

		// Do not want to expose ConversionTable and want to make the properties lazy
		// so use ConversionType for UnitTesting
		val conversionTable = when(conversionType) {
			ConversionType.KanaToRomaji -> mKanaToRomajiMap
			ConversionType.RomajiToHiragana -> mRomajiToHiraganaMap
			ConversionType.RomajiToKatakana -> mRomajiToKatakanaMap
		}

		while (offset < stringBuilder.length) {
			val maxKeyLength = minOf(conversionTable.maxKeyLength, stringBuilder.length - offset)

			val kanaText = (maxKeyLength downTo 1)
				.asSequence()
				.map {
					val key = stringBuilder.substring(offset, offset + it)
					val kanaText = conversionTable.get(key)
					Pair(kanaText, it)
				}.filter { !it.first.isNullOrBlank()}
				.firstOrNull()

			// If found, replace inside StringBuilder
			if (kanaText != null) {
				stringBuilder.replace(offset, offset + kanaText.second, kanaText.first!!)
				offset += kanaText.first!!.length
				continue
			}

			// If not found, keep the original and move next
			offset++
		}

		return stringBuilder
	}

	// Romaji -> Kana
	internal fun replaceLongVowelsWithChoonpu(stringBuilder: StringBuilder): StringBuilder {
		(1 until stringBuilder.length)
			.asSequence()
			.filter {
				val currentChar = stringBuilder[it]
				currentChar == stringBuilder[it - 1] && isRomanVowel(currentChar)
			}.forEach {
				stringBuilder
					.setCharAt(it, choonpuChar)
			}

		return stringBuilder
	}

	internal fun replaceDoubleConsonantsWithSokuon(stringBuilder: StringBuilder, kana: Kana): StringBuilder {
		(1 until stringBuilder.length)
			.asSequence()
			.filter {
				val currentChar = stringBuilder[it]
				val previousChar = stringBuilder[it - 1]

				val isLongVowel = currentChar == previousChar &&
						!isRomanVowel(currentChar) &&
						currentChar != 'n'

				// "tch" is a double consonant for "ch"
				val isSpecialCase = currentChar == 'c' &&
						previousChar == 't'

				isLongVowel || isSpecialCase
			}.forEach {
				stringBuilder
					.setCharAt(it - 1, kana.sokuon)
			}

		return stringBuilder
	}

	// Kana -> Romaji
	internal fun replaceChoonpuWithLongVowels(stringBuilder: StringBuilder): StringBuilder {
		val allIndices = stringBuilder
			.allIndicesOf(choonpuChar)

		val erroneousChoonpu = allIndices
			.filter { it == 0 || !isRomanVowel(stringBuilder[it - 1]) }

		allIndices
			.filter { it !in erroneousChoonpu }
			.forEach {
				val previousChar = stringBuilder[it - 1]
				stringBuilder.setCharAt(it, previousChar)
			}

		// Perform after correct cases and in reverse order so that indices are not messed up
		erroneousChoonpu
			.asReversed()
			.forEach { stringBuilder.deleteCharAt(it) }

		return stringBuilder
	}

	internal fun replaceSokuonWithDoubleConsonants(stringBuilder: StringBuilder): StringBuilder {
		val allIndices = stringBuilder.allIndicesOf(Kana.Hiragana.sokuon, Kana.Katakana.sokuon)
		val lastIndex = stringBuilder.length - 1

		val erroneousSokuon = allIndices
			.filter {
				it == 0 || it == lastIndex || isRomanVowel(stringBuilder[it + 1])
			}

		allIndices
			.filter { it !in erroneousSokuon }
			.forEach {
				val nextChar = stringBuilder[it + 1]
					.runIf({ x -> x == 'c' }, { 't' })
				
				stringBuilder.setCharAt(it, nextChar)
			}

		// Perform after correct cases and in reverse order so that indices are not messed up
		erroneousSokuon
			.asReversed()
			.forEach { stringBuilder.deleteCharAt(it) }

		return stringBuilder
	}

	private fun isRomanVowel(char: Char) =
		when (char) {
			'a', 'i', 'u', 'e', 'o' -> true
			else -> false
		}

	private fun loadTable(conversionType: ConversionType): ConversionTable {
		val filePath = combinePath("/kanaConversion", "${conversionType.fileName}.csv")

		return getResourceStreamReader(filePath) {
			it.readLines()
				.map { x ->
					val key = x.substringBefore(keyValueDelimiter)
					val value = x.substringAfter(keyValueDelimiter)
					return@map Pair(key, value)
				}.toMap()
				.run { ConversionTable(this) }
		}
	}
}

internal enum class Kana(val sokuon: Char) {

	Hiragana('っ'),
	Katakana('ッ')
}

internal enum class ConversionType(val fileName: String) {

	KanaToRomaji("kana_to_romaji"),
	RomajiToHiragana("romaji_to_hiragana"),
	RomajiToKatakana("romaji_to_katakana")
}

private class ConversionTable(private val map: Map<String, String>) {

	val maxKeyLength = map.keys
		.asSequence()
		.map(String::length)
		.max() ?: 0

	fun get(key: String) = map[key]
}