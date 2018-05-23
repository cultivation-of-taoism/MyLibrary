package com.hk.library.retrofit

import java.io.IOException

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Okio
import okio.Source

/**
 * Created by Administrator on 2017/1/16.
 * 下载文件用到的接收体，便于获取下载进度
 */

class FileResponseBody(private val responseBody: ResponseBody, val listener: ProgressListener) :
        ResponseBody() {
    private  var bufferedSource: BufferedSource? = null



    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            internal var bytesReadied: Long = 0
            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                bytesReadied += if (bytesRead == (-1).toLong()) 0 else bytesRead
                listener(bytesReadied, contentLength(),bytesRead == (-1).toLong())
                return bytesRead
            }
        }
    }
}
