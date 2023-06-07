package br.com.cvj.veritytest.model.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Api {
    private fun initRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(MoshiConverterFactory.create(MoshiWrapper.moshi))
            .build()
    }

    val services: Services = initRetrofit().create(Services::class.java)
}
