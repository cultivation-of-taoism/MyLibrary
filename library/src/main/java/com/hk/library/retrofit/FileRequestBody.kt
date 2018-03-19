package rundi.investmentadviser.retrofit

import java.io.File
import java.io.IOException

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.Okio
import okio.Sink

/**
 * Created by Administrator on 2017/1/16.
 * 上传文件用到的请求体，便于获取上传进度
 */

class FileRequestBody : RequestBody {

    private var mRequestBody: RequestBody? = null
    private var mProgressListener: ProgressListener? = null

    private var bufferedSink: BufferedSink? = null


    constructor(mediaType: MediaType, file: File, progressListener: ProgressListener) {
        this.mRequestBody = RequestBody.create(mediaType, file)
        this.mProgressListener = progressListener
    }

    constructor(requestBody: RequestBody, progressListener: ProgressListener) {
        this.mRequestBody = requestBody
        this.mProgressListener = progressListener
    }

    //返回了requestBody的类型，想什么form-data/MP3/MP4/png等等等格式
    override fun contentType(): MediaType? {
        return mRequestBody!!.contentType()
    }

    //返回了本RequestBody的长度，也就是上传的totalLength
    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mRequestBody!!.contentLength()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        if (bufferedSink == null) {
            //包装
            bufferedSink = Okio.buffer(sink(sink))
        }
        //写入
        mRequestBody!!.writeTo(bufferedSink!!)
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink!!.flush()
    }

    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            //当前写入字节数
            internal var bytesWritten = 0L
            //总字节长度，避免多次调用contentLength()方法
            internal var contentLength = 0L

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength == 0L) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength()
                }
                //增加当前写入的字节数
                bytesWritten += byteCount
                //回调上传接口
                mProgressListener!!(bytesWritten, contentLength, bytesWritten == contentLength)
            }
        }
    }
}
