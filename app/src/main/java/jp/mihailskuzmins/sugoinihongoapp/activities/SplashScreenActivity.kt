package jp.mihailskuzmins.sugoinihongoapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.createAppInjector
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.startActivityAndFinish
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.helpers.utils.AuthUtil
import javax.inject.Inject

class SplashScreenActivity : AppCompatActivity() {

    @Inject
    lateinit var authUtil: AuthUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAppInjector()
            .inject(this)

        val activityClass = if (authUtil.isAuthenticated) MainActivity::class.java
            else SignInSignUpActivity::class.java

        startActivityAndFinish(activityClass)
    }
}
