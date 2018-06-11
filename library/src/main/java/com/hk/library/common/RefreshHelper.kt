package com.hk.library.common

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hk.library.R
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener

class RefreshHelper(var adapter: BaseQuickAdapter<out Any,out BaseViewHolder>,
                    val refreshLayout: SmartRefreshLayout,
                    val loadData: (Int)->Unit)
    : OnRefreshListener, OnLoadMoreListener {
    companion object {
        var PAGE_SIZE = 10
    }
    init {
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setOnLoadMoreListener(this)
        checkEmptyView(adapter)
    }
    private var page = 1
    override fun onRefresh(refreshlayout: RefreshLayout) {
        refreshLayout.setNoMoreData(false)
        page = 1
        if (adapter.data.isNotEmpty()){
            adapter.data.clear()
            adapter.notifyDataSetChanged()
        }
        loadData(page)
    }

    override fun onLoadMore(refreshlayout: RefreshLayout) {
        loadData(page)
    }
    fun onDataLoaded(data: List<Nothing>){
        val finish: Boolean = data.size < PAGE_SIZE
        if (data.isNotEmpty()){
            adapter.addData(data)
            adapter.notifyDataSetChanged()
        }
        if (finish&&refreshLayout.state == RefreshState.Loading)//判断是否加载完毕
            refreshLayout.finishLoadMoreWithNoMoreData()
        else
            finishRefreshOrLoadMore()
        page++
    }

    fun finishRefreshOrLoadMore(){
        when(refreshLayout.state){
            RefreshState.Refreshing -> refreshLayout.finishRefresh()
            RefreshState.Loading -> refreshLayout.finishLoadMore()
        }
    }
    fun changeAdapter(adapter: BaseQuickAdapter<out Any,out BaseViewHolder>){
        this.adapter = adapter
        checkEmptyView(adapter)
    }

    private fun checkEmptyView(adapter: BaseQuickAdapter<out Any, out BaseViewHolder>) {
        if (adapter.emptyView != null) {
            if (adapter.emptyView.parent != null)
                (adapter.emptyView.parent as ViewGroup).removeView(adapter.emptyView)
        }
        this.adapter.setEmptyView(R.layout.layout_empty)
    }
}