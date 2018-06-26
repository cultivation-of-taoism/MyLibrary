package com.hk.library.retrofit

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.hk.library.presenter.IPresenter
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.internal.subscribers.LambdaSubscriber
import org.reactivestreams.Subscription

open class RetrofitSubscriber<T>(val iPresenter: IPresenter, private val task:Int){
    val lambdaSubscriber = LambdaSubscriber<T>(Consumer{
        onNext(it)
    }, Consumer { onError(it) }, Action { onComplete() }, Consumer { onSubscribe(it) })
    open val isList: Boolean = false

    open fun onNext(t: T) {
        checkResult(t)
        iPresenter.onSuccess(t!!,task)
    }

    private fun onError(e: kotlin.Throwable?){
        if (e?.message != null) {
            LogUtils.v("Novate", e.message)
        } else {
            LogUtils.v("Novate", "Throwable  || Message == Null")
        }

        if (e is ApiException) {
            LogUtils.e("Novate", "--> e instanceof Throwable")
            LogUtils.e("Novate", "--> " + e.cause.toString())
            this.onError(e)
        } else {
            LogUtils.e("Novate", "e !instanceof Throwable")
            var detail = ""
            if (e!!.cause != null) {
                detail = e.cause!!.message!!
            }

            LogUtils.e("Novate", "--> $detail")
            this.onError(RetrofitException.handleException(e))
        }

        this.onComplete()
    }

    open fun onError(p0: ApiException) {
        iPresenter.onError(p0,task)
    }



    open fun onComplete() {
        iPresenter.controlProgress(false)
    }

    open fun onSubscribe(s: Subscription) {
        s.request(Long.MAX_VALUE)
        onStart()
    }

    open fun onStart() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort("似乎没网O")
            onComplete()
            return
        }
        if (!isList)
            iPresenter.controlProgress(true)
    }

    @Throws(Throwable::class)
    open fun checkResult(t: T){}

}