package com.qianmo.stampcoin.common

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit



/**
 * Created by 23641 on 2017/6/28.
 */

abstract class CustomGsonConverterFactory constructor(private val gson: Gson) : Converter
.Factory() {

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>?, retrofit:
    Retrofit?): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return getResponseBodyConvert(gson, adapter as TypeAdapter<Any>)
    }
    abstract fun getResponseBodyConvert(gson: Gson, adapter: TypeAdapter<Any>):
            Converter<ResponseBody, *>

    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<Annotation>?,
                                      methodAnnotations: Array<Annotation>?, retrofit: Retrofit?)
            : Converter<*, RequestBody>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return CustomGsonRequestBodyConverter(gson, adapter)
    }

}
