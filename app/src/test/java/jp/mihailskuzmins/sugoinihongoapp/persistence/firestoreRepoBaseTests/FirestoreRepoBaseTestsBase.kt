package jp.mihailskuzmins.sugoinihongoapp.persistence.firestoreRepoBaseTests

import jp.mihailskuzmins.sugoinihongoapp.persistence.repos.firestore.FirestoreRepoBase

abstract class FirestoreRepoBaseTestsBase {

	protected class Repo : FirestoreRepoBase()

	protected fun createClass() = Repo()
}