package jp.mihailskuzmins.sugoinihongoapp.features.base.interfaces

import jp.mihailskuzmins.sugoinihongoapp.userinterface.views.ButtonMessage

interface HasAuthAction {

    val authButton: ButtonMessage
    val isAuth: Boolean
}