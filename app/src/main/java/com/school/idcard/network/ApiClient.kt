package com.school.idcard.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    private var sRetrofitClient: Retrofit? = null

    companion object {

        private var gson = GsonBuilder().setLenient().create()
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(logging)
                .writeTimeout(60,TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS)
                .connectTimeout(60,TimeUnit.SECONDS)
                .build()
            Retrofit.Builder()
                .baseUrl("https://scriwo.com/idCard/")
//                .baseUrl("http://192.168.1.16:6001/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }

        val apiInterface: ApiInterface by lazy {
            retrofit.create(ApiInterface::class.java)
        }
    }
}