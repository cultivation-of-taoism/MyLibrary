package com.hk.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hk.R
import com.tamic.novate.Novate
import com.tamic.novate.callback.RxStringCallback
import kotlinx.android.synthetic.main.activity_novate.*

class NovateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novate)
        val novate = Novate.Builder(this)
                .baseUrl("https://www.baidu.com/")
                .addLog(true)
                .addCache(false)
                .build()
        novate.rxGet("", mapOf(),object: RxStringCallback() {
            override fun onCancel(tag: Any?, e: com.tamic.novate.Throwable?) {

            }

            override fun onError(tag: Any?, e: com.tamic.novate.Throwable?) {
            }

            override fun onNext(p0: Any?, p1: String?) {
                tv_rs.text = p1
            }
        })
    }
}
