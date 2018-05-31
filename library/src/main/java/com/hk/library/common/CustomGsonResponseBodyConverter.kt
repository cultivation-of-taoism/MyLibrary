package com.qianmo.stampcoin.common

import com.google.gson.Gson
import com.google.gson.TypeAdapter

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader

import okhttp3.ResponseBody
import retrofit2.Converter

import okhttp3.internal.Util.UTF_8

/**
 * Created by 23641 on 2017/6/28.
 */

abstract class CustomGsonResponseBodyConverter<T>(private val gson: Gson, private val adapter:
TypeAdapter<T>) : Converter<ResponseBody, T> {
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val response = value.string()
        checkResponse(response)
        val contentType = value.contentType()
        val charset = if (contentType != null) contentType.charset(UTF_8) else UTF_8
        val inputStream = ByteArrayInputStream(response.toByteArray())
        val reader = InputStreamReader(inputStream, charset!!)
        val jsonReader = gson.newJsonReader(reader)

        value.use {
            return adapter.read(jsonReader)
        }
    }
    @Throws(IOException::class)
    abstract fun checkResponse(response: String)

}
