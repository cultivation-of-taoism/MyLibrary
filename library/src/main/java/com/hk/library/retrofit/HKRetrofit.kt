package com.hk.library.retrofit

import com.blankj.utilcode.util.LogUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object HKRetrofit {
    val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                if (it.startsWith("{")) LogUtils.json(it)
                else HttpLoggingInterceptor.Logger.DEFAULT.log(it)
            }).setLevel(HttpLoggingInterceptor.Level.BODY))
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
    val retrofitBuilder = defaultProcess(Retrofit.Builder())

    private fun defaultProcess(b: Retrofit.Builder): Retrofit.Builder{
        return b.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
    }
    fun getDefaultRetrofit(url: String): Retrofit = retrofitBuilder.baseUrl(url).build()
    fun getDefaultRetrofit(url: String, process: (b: Retrofit.Builder)->Retrofit.Builder): Retrofit{
        return defaultProcess(process(Retrofit.Builder())).baseUrl(url).build()
    }
    private val mClientBuilder = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                if (it.startsWith("{")) LogUtils.json(it)
                else HttpLoggingInterceptor.Logger.DEFAULT.log(it)
            }).setLevel(HttpLoggingInterceptor.Level.BODY))
    private val mRerofitBuilder: Retrofit.Builder = Retrofit.Builder()
    fun timeOut(timeOut: Long = 10): HKRetrofit{
        mClientBuilder.readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                .connectTimeout(timeOut, TimeUnit.SECONDS)
        return this
    }
    fun addFactory(vararg factory: Converter.Factory): HKRetrofit{
        mRerofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        factory.forEach { mRerofitBuilder.addConverterFactory(it) }
        mRerofitBuilder.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
        return this
    }
    fun baseUrl(url: String): Retrofit = mRerofitBuilder.baseUrl(url).client(mClientBuilder.build())
            .build()
}