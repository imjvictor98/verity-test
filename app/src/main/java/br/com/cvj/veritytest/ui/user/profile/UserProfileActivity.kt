package br.com.cvj.veritytest.ui.user.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.cvj.veritytest.R
import br.com.cvj.veritytest.model.data.UserInfoResponse

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
    }
}