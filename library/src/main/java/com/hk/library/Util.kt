package com.hk.library

import android.widget.TextView
import com.hk.library.presenter.Presenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

/**
 * Created by 23641 on 2017/6/15.
 */

fun Presenter.verificationInterval(tvVerify:TextView, enableColor:Int,
                                                            disEnableColor:Int){
    Observable.interval(1, TimeUnit.SECONDS).take(60)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it ->
                val int = 60 - it
                tvVerify.text = "重新发送($int s)"
                tvVerify.isEnabled = false
                tvVerify.setTextColor(disEnableColor)
            }, {}, {
                tvVerify.text = "重新发送"
                tvVerify.isEnabled = true
                tvVerify.setTextColor(enableColor)
            })

}
