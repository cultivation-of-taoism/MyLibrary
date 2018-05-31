package com.hk.ui

import android.os.Bundle
import android.view.View
import com.hk.ApiModel
import com.hk.ApiPresenter
import com.umeng.soexample.R
import com.hk.library.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_mvp.*
import rundi.investmentadviser.entity.Token

class MvpActivity : BaseActivity() {
    lateinit var presenter:ApiPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvp)
        presenter = ApiPresenter(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.bt_go->presenter.registerToken(et_serial.text.toString())
        }
    }

    override fun showSuccess(`object`: Any, task: Int) {
        super.showSuccess(`object`, task)
        when(task){
            ApiModel.Task.REGISTER_TOKEN->showError((`object` as Token).toString())
        }
    }

    override fun showError(error: Any, task: Int) {
        when(task){
            ApiModel.Task.REGISTER_TOKEN->showError("拦截到注册TOKEN失败信息：$error")
        }
    }
}
