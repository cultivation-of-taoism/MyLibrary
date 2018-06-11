package com.hk.entity

import com.google.gson.annotations.SerializedName

data class TResult<T>(
        val msg: String,
        val code: Int,
        @SerializedName("resultList")
        val data: T
)