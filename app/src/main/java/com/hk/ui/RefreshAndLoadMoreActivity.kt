package com.hk.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.hk.ApiPresenter
import com.hk.R
import com.hk.adapter.NewsAdapter
import com.hk.entity.TResult
import com.hk.library.common.RefreshHelper
import com.hk.library.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_refresh_and_load_more.*

class RefreshAndLoadMoreActivity : BaseActivity() {
    val apiPresenter by lazy { ApiPresenter(this) }
    lateinit var refreshHelper: RefreshHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh_and_load_more)
        setView()
    }

    override fun setView() {
        super.setView()
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = NewsAdapter(arrayListOf())
        adapter.bindToRecyclerView(recyclerView)
        refreshHelper = RefreshHelper(adapter, layout_refresh) {  page->
            LogUtils.v("开始请求")
            apiPresenter.getInformationList(page)
        }
        layout_refresh.autoRefresh(500)

    }

    override fun showProgress(isShow: Boolean) {
    }

    override fun showSuccess(`object`: Any, task: Int) {
        LogUtils.v("请求成功")
        super.showSuccess(`object`, task)
        refreshHelper.onDataLoaded((`object` as TResult<List<Nothing>>).data)
    }

    override fun showError(error: Any, task: Int) {
        super.showError(error, task)
        LogUtils.v("请求失败")
        refreshHelper.finishRefreshOrLoadMore()
    }
}
