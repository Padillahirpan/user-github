package com.irpn.base.network

import android.content.SharedPreferences
import android.provider.SyncStateContract
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.irpn.base.utils.Constants
import jp.wasabeef.glide.transformations.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object NetworkBuilder {


    fun <T> create(
        baseUrl: String,
        apiType: Class<T>,
        sharedPreferences: SharedPreferences
    ) =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(gmHttpClient(sharedPreferences, baseUrl))
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(apiType)

    private fun gmHttpClient(sharedPreferences: SharedPreferences, baseUrl: String): OkHttpClient {
        val logging = HttpLoggingInterceptor()

        logging.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(logging)
            .retryOnConnectionFailure(true)


        builder.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()

//            val token = sharedPreferences.getString(Constants.USER_TOKEN, "") ?: ""
//            Timber.tag("Token").d(token)
//
//            if (token.isNotEmpty()) {
//                requestBuilder.header("Authorization",token)
//            }
            chain.proceed(requestBuilder.build())
        }

        builder.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
               // .header("Client-Id", Constants.CLIENT_ID)
//                .header("Accept", "application/json")
//                .header("Content-Type", "application/json")
//                .header("Client-Version", BuildConfig.VERSION_CODE.toString())
//                .header("x-auth-token", com.irpn.base.BuildConfig.TOKEN)
            chain.proceed(requestBuilder.build())
        }

        return builder.build()
    }

}