package br.com.cvj.veritytest.util

import android.content.res.Resources
import android.graphics.Color
import androidx.annotation.RawRes
import org.json.JSONObject
import timber.log.Timber

object RawUtil {

    fun getColor(resources: Resources, @RawRes jsonFile: Int, colorName: String): Int? {
        try {
            val inputStream = resources.openRawResource(jsonFile)

            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val jsonObject = JSONObject(jsonString)

            val colorInJson = jsonObject.getString(colorName)

            return Color.parseColor(colorInJson)
        } catch (e: Exception) {
            Timber.e(e)
        }

        return null
    }

    fun getJson(resources: Resources, @RawRes jsonFile: Int): String {
        val inputStream = resources.openRawResource(jsonFile)
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        return jsonString
    }

}