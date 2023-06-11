package br.com.cvj.veritytest.ui.user.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.cvj.veritytest.R
import br.com.cvj.veritytest.databinding.ActivityUserProfileBinding
import br.com.cvj.veritytest.model.data.UserInfoResponse
import br.com.cvj.veritytest.model.data.UserRepositoryItemResponse
import br.com.cvj.veritytest.model.repository.user.profile.UserProfileDataSource
import br.com.cvj.veritytest.ui.user.input.UserInputActivity
import br.com.cvj.veritytest.util.LocalStorageUtil
import br.com.cvj.veritytest.util.extension.gone
import br.com.cvj.veritytest.util.extension.setImageUrl
import br.com.cvj.veritytest.util.extension.styled
import br.com.cvj.veritytest.util.extension.visible
import br.com.cvj.veritytest.util.extension.whenIsEmpty
import br.com.cvj.veritytest.util.extension.whenIsNotEmpty
import kotlinx.coroutines.launch

class UserProfileActivity : AppCompatActivity() {
    companion object {
        private const val EXTRA_USER_INFO = "EXTRA_USER_INFO"

        fun start(context: Context, userInfo: UserInfoResponse) {
            val intent = Intent(context, UserProfileActivity::class.java).apply {
                putExtra(EXTRA_USER_INFO, userInfo)
            }
            context.startActivity(intent)
        }
    }

    private lateinit var viewBinding: ActivityUserProfileBinding

    private lateinit var viewModel: UserProfileViewModel

    private var userInfoExtra: UserInfoResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityUserProfileBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        viewModel = UserProfileViewModel.ViewModelFactory(UserProfileDataSource())
            .create(UserProfileViewModel::class.java)

        loadExtras()

        fillProfile()

        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is UserProfileUiState.Success -> showRepositories(uiState.repositories)
                    is UserProfileUiState.Error -> showError()
                    is UserProfileUiState.Empty -> {
                        if (uiState.isEmpty) {
                            showEmpty()
                        } else {
                            hideEmpty()
                        }
                    }
                    is UserProfileUiState.Loading -> {
                        if (uiState.isLoading) {
                            showLoading()
                        } else {
                            hideLoading()
                        }
                    }
                    is UserProfileUiState.Reload -> {
                        if (uiState.showReload) {
                            showReload()
                        } else {
                            hideReload()
                        }
                    }
                }
            }
        }

        viewBinding.userProfileErrorBtn.setOnClickListener {
            hideRepositories()
            viewModel.getUserRepositories(userInfoExtra?.login.toString())
        }

        viewBinding.userProfileReposReloadBtn.setOnClickListener {
            hideRepositories()
            viewModel.getUserRepositories(userInfoExtra?.login.toString())
        }

        viewModel.getUserRepositories(userInfoExtra?.login.toString())
    }

    private fun loadExtras() {
        intent.extras?.let {
            userInfoExtra = it.getSerializable(EXTRA_USER_INFO) as? UserInfoResponse
        }
    }

    private fun fillProfile() {
        viewBinding.userProfileLogout.setOnClickListener(logout())

        userInfoExtra?.run {
            viewBinding.userProfileImage.setImageUrl(avatarUrl.toString())

            name
                .whenIsNotEmpty {
                    viewBinding.userProfileName.apply {
                        text = it
                        visible()
                    }
                }
                .whenIsEmpty { viewBinding.userProfileName.gone() }

            location
                .whenIsNotEmpty {
                    viewBinding.userProfileLocation.apply {
                        text = it
                        visible()
                    }
                }.whenIsEmpty {
                    viewBinding.userProfileLocation.gone()
                }

            viewBinding.userProfileLogin.text =
                getString(R.string.user_profile_login_format, login).styled()

            company
                .whenIsNotEmpty {
                    viewBinding.userProfileCompany.apply {
                        text = it
                        visible()
                    }
                }.whenIsEmpty {
                    viewBinding.userProfileCompany.gone()
                }

            viewBinding.userProfileFollows.text =
                getString(R.string.user_profile_follows_format, followers, following).styled()
        }
    }

    private fun logout() = View.OnClickListener {
        LocalStorageUtil(this).removeUser()
        UserInputActivity.startWClearTop(this)
        finish()
    }

    private fun showLoading() {
        viewBinding.userProfileLoading.visible()
    }

    private fun hideLoading() {
        viewBinding.userProfileLoading.gone()
    }

    private fun showError() {
        viewBinding.userProfileErrorContainer.visible()
    }

    private fun hideError() {
        viewBinding.userProfileErrorContainer.gone()
    }

    private fun showEmpty() {
        viewBinding.userProfileEmpty.visible()
    }

    private fun hideEmpty() {
        viewBinding.userProfileEmpty.gone()
    }

    private fun showRepositories(repositories: List<UserRepositoryItemResponse>) {
        viewBinding.userProfileReposList.apply {
            visible()
            adapter = UserProfileAdapter(repositories)
        }
    }

    private fun hideRepositories() {
        viewBinding.userProfileReposList.gone()
    }

    private fun showReload() {
        viewBinding.userProfileReposReloadBtn.visible()
    }

    private fun hideReload() {
        viewBinding.userProfileReposReloadBtn.gone()
    }
}

