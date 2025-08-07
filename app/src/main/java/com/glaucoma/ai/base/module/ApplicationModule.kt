package com.glaucoma.ai.base.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.glaucoma.ai.base.utils.event.NetworkErrorHandler
import com.glaucoma.ai.data.api.ApiHelper
import com.glaucoma.ai.data.api.ApiHelperImpl
import com.glaucoma.ai.data.api.ApiService
import com.glaucoma.ai.data.api.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL
//    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Singleton
    @Provides
    fun networkErrorHandler(context: Application): NetworkErrorHandler {
        return NetworkErrorHandler(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
        .build()
    }

//    @Provides
//    @Singleton
//    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//        OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .connectTimeout(5, TimeUnit.MINUTES)
//            .writeTimeout(5, TimeUnit.MINUTES) // write timeout
//            .readTimeout(5, TimeUnit.MINUTES) // read timeout
//            /*.addInterceptor(BasicAuthInterceptor(Constants.USERNAME, Constants.PASSWORD))*/
//            .build()
//    } else OkHttpClient
//        .Builder()
//        .connectTimeout(5, TimeUnit.MINUTES)
//        .writeTimeout(5, TimeUnit.MINUTES) // write timeout
//        .readTimeout(5, TimeUnit.MINUTES) // read timeout
//        .build()


    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

    @Provides
    @Singleton
    fun provideSharedPref(application: Application): SharedPreferences {
        return application.getSharedPreferences(application.packageName, Context.MODE_PRIVATE)
    }
}