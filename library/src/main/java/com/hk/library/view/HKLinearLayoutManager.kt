package com.hk.library.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

class HKLinearLayoutManager(context: Context): LinearLayoutManager(context, LinearLayoutManager
        .VERTICAL, false) {
    lateinit var headerView: View
    private var scrollX = 0
    private var scrollWidth = 0
    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        val newScrollX = scrollX + dx
        if (newScrollX <= 0){
            scrollX = 0
            return 0
        }
        if (newScrollX >= scrollWidth){
            scrollX = scrollWidth
            return 0
        }
        for (i in 0 until childCount){
            val child = getChildAt(i)
            child.scrollBy(dx, 0)
            headerView.scrollBy(dx, 0)
        }
        scrollX = newScrollX
        return dx
    }

    private fun synchronizeScrollX(){
        for (i in 0 until childCount){
            val child = getChildAt(i)
            child.scrollX = scrollX
            headerView.scrollX = scrollX
        }
    }

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        if (scrollWidth == 0)scrollWidth = getChildAt(0).measuredWidth - width
    }


    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        synchronizeScrollX()
        return super.scrollVerticallyBy(dy, recycler, state)
    }
}