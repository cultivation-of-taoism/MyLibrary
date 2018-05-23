package com.hk.library.retrofit

import com.hk.library.presenter.Presenter
import com.hk.library.ui.IView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


/**
 * Created by 23641 on 2017/10/19.
 */
open class FilePresenter(view:IView): Presenter(view), ProgressListener {
    var hasWrittenLen = 0L
    var totalLen = 0L
    var hasFinish = false
    lateinit var dispose:Disposable
    override fun invoke(hasWrittenLen: Long, totalLen: Long, hasFinish: Boolean) {
        this.hasWrittenLen = hasWrittenLen
        this.totalLen = totalLen
        this.hasFinish = hasFinish
    }
    /**
     *  开始下载时,创建一个可观察对象，每隔一秒将进度发送出去
     *  订阅者在主线程收到进度后，更新UI界面,下载失败或下载成功是
     *  取消订阅
     */
    fun start(string: String){
        dispose = Flowable.interval(1, TimeUnit.SECONDS)
                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    val progress = if (totalLen == 0L) 0 else hasWrittenLen/totalLen
                    view.loadProgressDialog.setMessage("$string $progress%")

                }
    }
    fun finish(){
        dispose.dispose()
    }
}