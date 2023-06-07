package br.com.cvj.veritytest.model.network

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MoshiWrapper {
    val moshi: Moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .add(Date::class.java, object : JsonAdapter<Date>() {
            @FromJson
            override fun fromJson(reader: JsonReader): Date? {
                val dateString = reader.nextString()
                return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(dateString)
            }

            @ToJson
            override fun toJson(writer: JsonWriter, value: Date?) {
                val dateString =
                    value?.let {
                        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(
                            it
                        )
                    } ?: run {
                        null
                    }
                writer.value(dateString)
            }
        })
        .build()
}