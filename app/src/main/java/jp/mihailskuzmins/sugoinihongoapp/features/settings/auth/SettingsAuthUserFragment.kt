package jp.mihailskuzmins.sugoinihongoapp.features.settings.auth


import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import butterknife.OnClick
import jp.mihailskuzmins.sugoinihongoapp.R
import jp.mihailskuzmins.sugoinihongoapp.activities.SignInSignUpActivity
import jp.mihailskuzmins.sugoinihongoapp.databinding.FragmentSettingsAuthUserBinding
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.finishActivity
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.navigate
import jp.mihailskuzmins.sugoinihongoapp.extensions.android.provideViewModel
import jp.mihailskuzmins.sugoinihongoapp.extensions.invoke
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentBase
import jp.mihailskuzmins.sugoinihongoapp.features.base.FragmentConfig

class SettingsAuthUserFragment : FragmentBase<SettingsAuthUserViewModel>(R.layout.fragment_settings_auth_user) {

	override fun getNavigationId() =
		R.id.settingsAuthUserFragment

	override fun createViewModel() =
		provideViewModel<SettingsAuthUserViewModel>()

	override fun bindView() {
		getBinding<FragmentSettingsAuthUserBinding>().viewModel = viewModel
	}

	override fun createConfig() =
		FragmentConfig().apply {
			isBottomNavVisible = false
			isOpenSettingsAvailable = false
		}

	@OnClick(R.id.fragment_settings_auth_user_button_change_password)
	fun navigateToChangePassword() =
		navigate(R.id.settingsAuthUser_to_settingsAuthChangePassword)

	@OnClick(R.id.fragment_settings_auth_user_text_view_sign_out)
	fun signOut() {
		viewModel.signOut()

		Intent(context, SignInSignUpActivity::class.java)
			.invoke(context!!::startActivity)

		finishActivity()
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)

		inflater.inflate(R.menu.menu_settings_auth_user, menu)
	}

	override fun getOptionsItemSelectedFunction(itemId: Int) =
		when (itemId) {
			R.id.menu_settings_auth_user_delete_account -> { -> navigate(R.id.settingsAuthUser_to_settingsAuthDeleteAccount) }
			else -> super.getOptionsItemSelectedFunction(itemId)
		}
}
