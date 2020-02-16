package jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces

interface HasOverriddenBackNavigation {

	/**
	 * Returns True if custom handling has been applied
	 * Return False if the standard back navigation should be performed
	 * */
	fun overrideBackNavigation(): Boolean
}