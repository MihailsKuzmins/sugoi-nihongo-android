package jp.mihailskuzmins.sugoinihongoapp.extensions

import android.content.Context
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.resources.AuthResult

fun AuthResult.toTextRes() =
	when(this) {
		AuthResult.ServerNotReachable -> R.string.auth_error_server_not_reachable
		AuthResult.InvalidEmail -> R.string.auth_error_invalid_email
		AuthResult.UserNotFound -> R.string.auth_error_user_not_found
		AuthResult.WrongPassword -> R.string.auth_error_incorrect_password
		AuthResult.EmailNotVerified -> R.string.auth_error_email_not_verified
		AuthResult.Unknown -> R.string.auth_error_unknown_error
		AuthResult.EmailAlreadyInUse -> R.string.auth_error_user_already_exists
		AuthResult.EmailAlreadyVerified -> R.string.auth_error_email_already_verified
		AuthResult.NotAuthenticated -> R.string.auth_error_not_authenticated
		AuthResult.Success -> throw NotImplementedError()
	}

fun AuthResult.toMessage(context: Context): String {
	return try {
		this.toTextRes()
			.run(context::getString)
	} catch (ex: Exception) {
		""
	}
}