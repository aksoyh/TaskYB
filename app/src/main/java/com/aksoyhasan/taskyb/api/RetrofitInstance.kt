package com.aksoyhasan.taskyb.api

import com.aksoyhasan.taskyb.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {

            val client = OkHttpClient.Builder()
                .addInterceptor{ chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                            .method(original.method, original.body)
                            .build()

                    chain.proceed(request)
                }
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy {
            retrofit.create(YoorBitAPI::class.java)
        }


    }
}