package com.hk.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.bin.david.form.annotation.SmartColumn
import com.bin.david.form.annotation.SmartTable
import com.hk.R
import com.hk.library.presenter.IPresenter
import com.hk.library.presenter.Presenter
import com.hk.library.retrofit.*
import com.hk.library.ui.BaseActivity
import com.hk.library.ui.IView
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.activity_excel.*
import kotlinx.android.synthetic.main.content_excel.*
import retrofit2.http.Body
import retrofit2.http.POST

class ExcelActivity : BaseActivity(),IFutureContract.IFutureView {
    val presenter: FuturePresenter by lazy { FuturePresenter(this) }
    override fun showFutureList(data: List<Future>) {
        var mData = data.toMutableList()
        (0..3).forEach { mData.addAll(data) }
        table.setData(mData as List<Nothing>)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excel)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        table.config.apply {
            isShowXSequence = false
            isShowYSequence = false
            isShowTableTitle = false
        }
        presenter.loadFutureList()
    }
}
@SmartTable(name = "国内期货")
data class Future(
        @SmartColumn(id = 1, name = "合约名称", fixed = true)
        val futuresName: String,
        @SmartColumn(id = 2, name = "最新价")
        val now: String,
        @SmartColumn(id = 3, name = "涨跌")
        val differ: String,
        @SmartColumn(id = 4, name = "涨跌幅")
        val differRange: String,
        @SmartColumn(id = 5, name = "成交量")
        val volume: String,
        @SmartColumn(id = 6, name = "持仓量")
        val positions: String,
        @SmartColumn(id = 7, name = "买手")
        val buyCount: String,
        @SmartColumn(id = 8, name = "麦手")
        val sellCount: String,
        @SmartColumn(id = 9, name = "买价")
        val buyPrice: String,
        @SmartColumn(id = 10, name = "卖价")
        val sellPrice: String
)

data class Result(
        val msg: String,
        val code: Int,
        val resultList: List<Future>
)

interface IFutureContract{
    interface IFutureView: IView{
        fun showFutureList(data: List<Future>)
    }
    interface IFuturePresenter: IPresenter{
        fun loadFutureList()
    }
}

interface ApiService{
    @POST("futuresList")
    fun getFutureList(@Body params: Map<String, String>): Flowable<Result>
}

class ApiModel{
    val retrofit = HKRetrofit.getDefaultRetrofit("http://futures.srongzb.com/boot_futures/futures/")
    val apiService = retrofit.create(ApiService::class.java)
    fun getFutureList(): Flowable<Result> =
            apiService.getFutureList(mapOf(
                    "futuresType" to "1",
                    "pageSize" to "100",
                    "currentPage" to "1"
            ))

}

class FuturePresenter(val futureView: IFutureContract.IFutureView): Presenter(futureView), IFutureContract
.IFuturePresenter{
    val model = ApiModel()
    override fun loadFutureList() {
        model.getFutureList()
                .enqueue(object : RetrofitSubscriber<Result>(this,0){
                    override fun checkResult(t: Result) {
                        super.checkResult(t)
                        if (t.code != 100) throw ApiException(t.code, t.msg)
                    }
                    override fun onNext(t: Result) {
                        super.onNext(t)
                        futureView.showFutureList(t.resultList)
                    }
                })
    }
}