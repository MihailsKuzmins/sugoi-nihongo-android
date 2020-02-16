package jp.mihailskuzmins.sugoinihongoapp.helpers

import jp.mihailskuzmins.sugoinihongoapp.resources.Action

class Command(
	val execute: Action,
	val imeOptions: Int,
	val canExecute: () -> Boolean = {true})