package jp.mihailskuzmins.sugoinihongoapp.helpers.utils

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import jp.mihailskuzmins.sugoinihongoapp.BuildConfig
import jp.mihailskuzmins.sugoinihongoapp.extensions.hashMd5
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import java.nio.charset.StandardCharsets
import java.security.InvalidAlgorithmParameterException
import java.security.KeyStore
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec


class SecurePreferencesUtil(context: Context) {

	private val mPreferences = context
		.getSharedPreferences("${BuildConfig.APPLICATION_ID}.android", Context.MODE_PRIVATE)

	private val mInitialisationVectorLength = 12 // Android supports an IV of 12 for AES/GCM
	private val mAndroidKeyStore = "AndroidKeyStore"
	private val mEncoding = StandardCharsets.UTF_8

	fun getValue(key: String): String {
		val encryptedKey = key.hashMd5()
		val encryptedValue = mPreferences.getString(encryptedKey, null) ?: return ""

		val result = Base64.decode(encryptedValue, Base64.DEFAULT)
		if (result.size < mInitialisationVectorLength)
			return ""

		val secretKey = getExistingSecretKey(key)
		val initialisationVector = ByteArray(mInitialisationVectorLength)
		System.arraycopy(result, 0, initialisationVector, 0, mInitialisationVectorLength)

		val cipher = createCipher(secretKey, initialisationVector, Cipher.DECRYPT_MODE)
		val decodedBytes = getDecodedBytes(cipher, result)

		return String(decodedBytes, mEncoding)
	}

	fun setValue(key: String, value: String) {
		val secretKey = createNewSecretKey(key)

		val initialisationVector = createInitialisationVector()
		val cipher = createCipher(secretKey, initialisationVector, Cipher.ENCRYPT_MODE)
		val encodedBytes = getEncodedBytes(cipher, value)

		val result = ByteArray(initialisationVector.size + encodedBytes.size)
		System.arraycopy(initialisationVector, 0, result, 0, initialisationVector.size)
		System.arraycopy(encodedBytes, 0, result, initialisationVector.size, encodedBytes.size)

		val encryptedKey = key.hashMd5()
		val encryptedValue = Base64.encodeToString(result, Base64.DEFAULT)

		mPreferences.edit()
			.putString(encryptedKey, encryptedValue)
			.apply()
	}

	private fun createNewSecretKey(key: String): SecretKey {
		val parameterSpec = KeyGenParameterSpec.Builder(createAlias(key), KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
			.setBlockModes(KeyProperties.BLOCK_MODE_GCM)
			.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
			.setRandomizedEncryptionRequired(false)
			.build()

		return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, mAndroidKeyStore)
			.also { it.init(parameterSpec) }
			.run { generateKey() }
	}

	private fun getExistingSecretKey(key: String): SecretKey =
		KeyStore.getInstance(mAndroidKeyStore)
			.also { it.load(null) }
			.run { getEntry(createAlias(key), null) as KeyStore.SecretKeyEntry }
			.secretKey

	private fun createInitialisationVector(): ByteArray {
		val iv = ByteArray(mInitialisationVectorLength)
		SecureRandom().invoke { it.nextBytes(iv) }

		return iv
	}

	private fun getEncodedBytes(cipher: Cipher, value: String) =
		cipher.doFinal(value.toByteArray(mEncoding))

	private fun getDecodedBytes(cipher: Cipher, encodedBytes: ByteArray) =
		cipher.doFinal(encodedBytes, mInitialisationVectorLength, encodedBytes.size - mInitialisationVectorLength)

	private fun createCipher(secretKey: SecretKey, initialisationVector: ByteArray, mode: Int): Cipher {

		fun getCipher(algorithmParameterSpec: AlgorithmParameterSpec) =
			Cipher.getInstance("AES/GCM/NoPadding")
				.also { it.init(mode, secretKey, algorithmParameterSpec) }

		return try {
			getCipher(GCMParameterSpec(128, initialisationVector))
		} catch (ex: InvalidAlgorithmParameterException) {
			getCipher(IvParameterSpec(initialisationVector))
		}
	}

	private fun createAlias(key: String) =
		BuildConfig.APPLICATION_ID + key
}