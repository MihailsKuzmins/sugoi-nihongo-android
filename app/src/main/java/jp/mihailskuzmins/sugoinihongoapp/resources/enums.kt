package jp.mihailskuzmins.sugoinihongoapp.resources

enum class QuizQuestionResult {

	Correct,
	Wrong
}

enum class NavMode {

	Main,
	SignInSignUp
}

enum class AuthResult {

	Success,
	ServerNotReachable,
	InvalidEmail,
	UserNotFound,
	WrongPassword,
	EmailNotVerified,
	EmailAlreadyInUse,
	EmailAlreadyVerified,
	NotAuthenticated,
	Unknown
}