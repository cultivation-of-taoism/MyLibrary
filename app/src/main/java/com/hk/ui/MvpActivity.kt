package com.hk.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.hk.ApiModel
import com.hk.ApiPresenter
import com.hk.R
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
            R.id.bt_go->{
                presenter.registerToken(et_serial.text.toString())
                startActivity(Intent(this, RefreshAndLoadMoreActivity::class.java))
            }
        }
    }

    override fun showSuccess(`object`: Any, task: Int) {
        super.showSuccess(`object`, task)
        when(task){
            ApiModel.Task.REGISTER_TOKEN.ordinal->showError((`object` as Token).toString())
        }
    }

}
