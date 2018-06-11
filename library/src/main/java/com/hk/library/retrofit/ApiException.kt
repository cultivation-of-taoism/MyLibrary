package com.hk.library.retrofit

class ApiException : Exception{
    val code: Int
    override var message: String = ""
    constructor(code:Int, message:String): super(message){
        this.code = code
        this.message = message
    }
    constructor(throwable: Throwable, code: Int): super(throwable){
        this.code = code
    }
    constructor(throwable: Throwable,code: Int, message: String): super(throwable){
        this.code = code
        this.message = message
    }

    override fun toString(): String = "错误码：$code\n错误信息：$message"
}