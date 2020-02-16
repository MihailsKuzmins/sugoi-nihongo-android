package jp.mihailskuzmins.sugoinihongoapp.extensions.firebase

import com.google.firebase.auth.FirebaseAuth

val FirebaseAuth.isAuthenticated: Boolean
	get() = currentUser?.isEmailVerified ?: false