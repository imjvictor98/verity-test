package br.com.cvj.veritytest.ui.user.input

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.cvj.veritytest.R
import br.com.cvj.veritytest.databinding.ActivityUserInputBinding
import br.com.cvj.veritytest.model.data.UserInfoResponse
import br.com.cvj.veritytest.model.repository.user.info.UserInfoApiDataSource
import br.com.cvj.veritytest.ui.user.profile.UserProfileActivity
import br.com.cvj.veritytest.util.CustomDialogUtil
import br.com.cvj.veritytest.util.LocalStorageUtil
import br.com.cvj.veritytest.util.extension.gone
import br.com.cvj.veritytest.util.extension.hideKeyboard
import br.com.cvj.veritytest.util.extension.visible
import kotlinx.coroutines.launch
import javax.net.ssl.HttpsURLConnection

class UserInputActivity : AppCompatActivity() {

    companion object {
        fun startWClearTop(context: Context) {
            val intent = Intent(context, UserInputActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }
    }


    private lateinit var viewBinding: ActivityUserInputBinding
    private lateinit var viewModel: UserInputViewModel

    private var dialog: Dialog? = null
    private var localStorage: LocalStorageUtil = LocalStorageUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityUserInputBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        viewModel = UserInputViewModel.UserViewModelFactory(UserInfoApiDataSource())
            .create(UserInputViewModel::class.java)

        retrieveUser()

        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                with (uiState) uiState@ {
                    when (this) {
                        is UserInputUiState.Initial -> {}

                        is UserInputUiState.Success -> {
                            hideLoading()
                            saveUser(userInfo)
                            showProfileScreen(userInfo)
                        }

                        is UserInputUiState.Error -> {
                            if (showError) {
                                if (code == HttpsURLConnection.HTTP_NOT_FOUND) {
                                    showToastUserNotExists()
                                } else {
                                    showToastUserError()
                                }
                                hideLoading()
                            }
                        }
                    }
                }
            }
        }

        viewBinding.userInputBtn.setOnClickListener {
            val userTyped = viewBinding.userInputEt.text?.trim()

            if (userTyped?.isNotEmpty() == true) {
                viewModel.getUserInfo(userTyped.toString())
                showLoading()
                hideKeyboard()
            } else {
                showToastUserInputEmpty()
            }
        }
    }

    private fun showProfileScreen(userInfo: UserInfoResponse) {
        UserProfileActivity.start(this, userInfo)
        finish()
    }

    private fun showLoading() {
        viewBinding.userInputLoadingBtn.visible()
        viewBinding.userInputBtn.apply {
            isEnabled = false
            alpha = .2F
        }
    }

    private fun hideLoading() {
        viewBinding.userInputLoadingBtn.gone()
        viewBinding.userInputBtn.apply {
            isEnabled = true
            alpha = 1F
        }
    }

    private fun showToastUserError() {
        dialog = CustomDialogUtil(this)
            .buildDialogHorizontal(
                title = getString(R.string.user_input_dialog_title),
                subTitle = getString(R.string.user_input_dialog_description),
                primaryBtnText = getString(R.string.continue_btn),
                secondaryBtnText = getString(R.string.back_btn),
                onPrimaryClickListener = {
                    it.dismiss()
                    viewBinding.userInputBtn.performClick()
                },
                onSecondaryClickListener = { it.dismiss() }
            )

        dialog?.show()
    }

    private fun showToastUserInputEmpty() {
        dialog = CustomDialogUtil(this)
            .buildDialogHorizontal(
                title = getString(R.string.user_input_dialog_title),
                subTitle = getString(R.string.user_input_empty_dialog_description),
                primaryBtnText = getString(R.string.back_btn),
                onPrimaryClickListener = {
                    it.dismiss()
                }
            )

        dialog?.show()
    }

    private fun showToastUserNotExists() {
        dialog = CustomDialogUtil(this)
            .buildDialogHorizontal(
                title = getString(R.string.user_input_dialog_title),
                subTitle = getString(R.string.user_input_not_exists_dialog_description),
                primaryBtnText = getString(R.string.ok_btn),
                onPrimaryClickListener = {
                    it.dismiss()
                }
            )

        dialog?.show()
    }

    private fun saveUser(userInfo: UserInfoResponse) {
        localStorage.saveUser(userInfo)
    }

    private fun retrieveUser() {
        localStorage.retrieveUser()?.let {
            viewModel.onUserSaved(it)
        }
    }
}