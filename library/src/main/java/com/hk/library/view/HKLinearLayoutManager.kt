package com.hk.library.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

class HKLinearLayoutManager(context: Context): LinearLayoutManager(context, LinearLayoutManager
        .VERTICAL, false) {
    var scrollX = 0
        set(value) {
            field = value
            synchronizeScrollX()
        }
    lateinit var onScroll: (Int)->Unit
    private var mViewPortWidth = 0
    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun onMeasure(recycler: RecyclerView.Recycler?, state: RecyclerView.State?, widthSpec: Int, heightSpec: Int) {
        mViewPortWidth = View.MeasureSpec.getSize(widthSpec)
        super.onMeasure(recycler, state, widthSpec, heightSpec)
    }


    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        val newScrollX = scrollX + dx
        if (newScrollX <= 0){
            scrollX = 0
            onScroll(scrollX)
            return 0
        }
        if (newScrollX >= width - mViewPortWidth){
            scrollX = width - mViewPortWidth
            onScroll(scrollX)
            return 0
        }
        for (i in 0 until childCount){
            val child = getChildAt(i)
            child.scrollBy(dx, 0)
        }
        onScroll(scrollX)
        scrollX = newScrollX
        return dx
    }

    private fun synchronizeScrollX(){
        for (i in 0 until childCount){
            val child = getChildAt(i)
            child.scrollX = scrollX
        }
    }



    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        synchronizeScrollX()
        return super.scrollVerticallyBy(dy, recycler, state)
    }

    override fun computeHorizontalScrollExtent(state: RecyclerView.State?): Int {
        return mViewPortWidth
    }

    override fun computeHorizontalScrollRange(state: RecyclerView.State?): Int {
        val oversrollRight = Math.max(0, width - mViewPortWidth)
        return when {
            scrollX<0 -> width - scrollX
            scrollX > oversrollRight -> width + (scrollX - oversrollRight)
            else -> width
        }
    }

    override fun computeHorizontalScrollOffset(state: RecyclerView.State?): Int {
        return Math.max(0, scrollX)
    }

}