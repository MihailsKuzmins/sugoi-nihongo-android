package jp.mihailskuzmins.sugoinihongoapp.helpers.utils

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import jp.mihailskuzmins.sugoinihongoapp.extensions.firebase.isAuthenticated
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.resources.Action
import jp.mihailskuzmins.sugoinihongoapp.resources.AuthResult

class AuthUtil {

    private val mAuth = FirebaseAuth.getInstance()

    val isAuthenticated: Boolean
        get() = mAuth.isAuthenticated

    val userId: String?
        get() {
            val currentUser = mAuth.currentUser

            return if (currentUser?.isEmailVerified == true) currentUser.uid
                else null
        }

	val userEmail: String
		get() = mAuth.currentUser?.email ?: ""

	private val mCurrentUser: FirebaseUser
		get() = mAuth.currentUser!!

    fun signIn(email: String, password: String, onErrorAction: (AuthResult) -> Unit) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                task.exception?.let {
                    getAuthResult(it)
                        .invoke(onErrorAction)

                    return@addOnCompleteListener
                }

                task.result?.let {
                    val result = if (it.user?.isEmailVerified == true) AuthResult.Success
                        else AuthResult.EmailNotVerified

                    onErrorAction(result)
                    return@addOnCompleteListener
                }

                onErrorAction(AuthResult.Unknown)
            }
    }

    fun signUp(email: String, password: String, onAuthResult: (AuthResult) -> Unit) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                task.exception?.let {
	                getAuthResult(it)
                        .invoke(onAuthResult)

                    return@addOnCompleteListener
                }

	            sendEmailVerification(onAuthResult)
            }
    }

	fun sendPasswordReset(email: String, onAuthResult: (AuthResult) -> Unit) {
		mAuth.sendPasswordResetEmail(email)
			.addOnCompleteListener { task ->
				task.exception?.let {
					getAuthResult(it)
						.invoke(onAuthResult)

					return@addOnCompleteListener
				}

				onAuthResult.invoke(AuthResult.Success)
			}
	}

    fun sendEmailVerification(onAuthResult: ((AuthResult) -> Unit)? = null) {
	    val check = when {
		    mAuth.currentUser == null -> AuthResult.NotAuthenticated
		    mAuth.currentUser?.isEmailVerified == true -> AuthResult.EmailAlreadyVerified
		    else -> AuthResult.Success
	    }

        if (check != AuthResult.Success) {
	        onAuthResult?.invoke(check)
	        return
        }

	    mAuth.currentUser
		    ?.sendEmailVerification()
		    ?.addOnCompleteListener { task ->
			    task.exception?.let {
				    getAuthResult(it)
					    .invoke { x -> onAuthResult?.invoke(x) }

				    return@addOnCompleteListener
			    }

			    onAuthResult?.invoke(AuthResult.Success)
		    }
    }

	fun changePassword(currentPassword: String, newPassword: String, onAuthResult: (AuthResult) -> Unit) {
		reauthenticate(currentPassword, onAuthResult) {
			mCurrentUser.updatePassword(newPassword)
				.addOnCompleteListener { task ->
					task.exception?.let {
						getAuthResult(it).invoke(onAuthResult)
						return@addOnCompleteListener
					}

					onAuthResult(AuthResult.Success)
				}
		}
	}

	fun signOut() =
		mAuth.signOut()

	fun deleteAccount(email: String, password: String, onAuthResult: (AuthResult) -> Unit) {
		val check = when {
			mAuth.currentUser == null -> AuthResult.NotAuthenticated
			email != userEmail -> AuthResult.InvalidEmail
			else -> AuthResult.Success
		}

		if (check != AuthResult.Success) {
			onAuthResult(check)
				.invoke { return@deleteAccount }
		}

		reauthenticate(password, onAuthResult) {
			mAuth.currentUser
				?.delete()
				?.addOnCompleteListener { task ->
					task.exception?.let {
						onAuthResult(AuthResult.ServerNotReachable)
						return@addOnCompleteListener
					}

					onAuthResult(AuthResult.Success)
				}
		}
	}

	private fun reauthenticate(password: String, onAuthResult: (AuthResult) -> Unit, action: Action) {
		val authCredential = EmailAuthProvider.getCredential(userEmail, password)

		mCurrentUser
			.reauthenticate(authCredential)
			.addOnCompleteListener { reauthTask ->
				reauthTask.exception?.let {
					getAuthResult(it).invoke(onAuthResult)
					return@addOnCompleteListener
				}

				action()
			}
	}

    private fun getAuthResult(exception: Exception): AuthResult {
        if (exception !is FirebaseAuthException)
            return AuthResult.ServerNotReachable

        return when(exception.errorCode) {
            "ERROR_INVALID_EMAIL" -> AuthResult.InvalidEmail
	        "ERROR_EMAIL_ALREADY_IN_USE" -> AuthResult.EmailAlreadyInUse
            "ERROR_USER_NOT_FOUND" -> AuthResult.UserNotFound
            "ERROR_WRONG_PASSWORD" -> AuthResult.WrongPassword
            else -> AuthResult.Unknown
        }
    }
}