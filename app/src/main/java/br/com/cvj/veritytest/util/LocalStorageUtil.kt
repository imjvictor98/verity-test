package br.com.cvj.veritytest.util

import android.content.Context
import br.com.cvj.veritytest.model.data.UserInfoResponse
import br.com.cvj.veritytest.model.network.MoshiWrapper
import com.squareup.moshi.JsonAdapter

class LocalStorageUtil(context: Context) {

    companion object {
        private const val VERITY_STORAGE = "VERITY_STORAGE"
        private val moshi = MoshiWrapper.moshi

        object UserInfo {
            const val USER_INFO_STORED = "USER_INFO_STORED"
            val jsonAdapter: JsonAdapter<UserInfoResponse> =
                moshi.adapter(UserInfoResponse::class.java)
        }
    }

    private val sharedPref by lazy {
        context.getSharedPreferences(
            VERITY_STORAGE,
            Context.MODE_PRIVATE
        )
    }

    fun saveUser(userInfoResponse: UserInfoResponse) {
        sharedPref
            .edit()
            .putString(UserInfo.USER_INFO_STORED, UserInfo.jsonAdapter.toJson(userInfoResponse))
            .apply()
    }

    fun retrieveUser(): UserInfoResponse? {
        val jsonFromSharedPref = sharedPref.getString(UserInfo.USER_INFO_STORED, "")

        return try {
            UserInfo.jsonAdapter.fromJson(jsonFromSharedPref ?: "")
        } catch (e: Exception) {
            null
        }
    }

    fun removeUser() {
        sharedPref
            .edit()
            .remove(UserInfo.USER_INFO_STORED)
            .apply()
    }
}