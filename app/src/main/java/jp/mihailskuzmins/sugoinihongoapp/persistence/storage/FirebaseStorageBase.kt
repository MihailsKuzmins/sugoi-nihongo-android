package jp.mihailskuzmins.sugoinihongoapp.persistence.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import jp.mihailskuzmins.sugoinihongoapp.extensions.letIfNull
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.AuthUtil
import jp.mihailskuzmins.sugoinihongoapp.resources.Action
import timber.log.Timber
import java.io.File
import javax.inject.Inject

abstract class FirebaseStorageBase {

	private val mStorage = FirebaseStorage.getInstance()

	@Inject lateinit var authUtil: AuthUtil

	protected fun upload(reference: String, file: File, action: Action) {
		val userId = authUtil.userId
			.letIfNull { return@upload }

		val uri = Uri.fromFile(file)

		mStorage.getReference(reference)
			.child("$userId/${file.name}")
			.putFile(uri)
			.addOnCompleteListener { task ->
				task.exception?.let {
					Timber.e(task.exception)
					return@addOnCompleteListener
				}

				Timber.i("Uploaded file: %s", file.name)
				action()
			}
	}
}