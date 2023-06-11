package br.com.cvj.veritytest.model.data


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserProfileRepoLicense(
    @Json(name = "key")
    val key: String? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "node_id")
    val nodeId: String? = null,
    @Json(name = "spdx_id")
    val spdxId: String? = null,
    @Json(name = "url")
    val url: String? = null
)