package com.hk.library.common

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by Administrator on 2018/3/26.
 */
class NoScrollViewPager : ViewPager {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)
    var noScroll = true
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (noScroll) false else super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (noScroll) false else super.onInterceptTouchEvent(ev)
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, !noScroll)//去除滚动效果
    }
}