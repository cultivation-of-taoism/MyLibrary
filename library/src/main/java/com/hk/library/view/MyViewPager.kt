package com.hk.library.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

class MyViewPager : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val child = getChildAt(currentItem)
        if (childCanScroll(child, ev)) return true
        return super.dispatchTouchEvent(ev)
    }

    private fun childCanScroll(child: View, ev: MotionEvent): Boolean {
        if (child is MyHorizontalScrollView) {
            if (!child.canScroll(ev)) {
                onTouchEvent(ev)
                return true
            }
        }else if (child2CanScroll(child, ev)) return true
        return false
    }
    private fun child2CanScroll(child: View, ev: MotionEvent): Boolean{
        if (child is ViewGroup){
            for (i in 0 until child.childCount){
                val child2 = child.getChildAt(i)
                if (child2 is MyHorizontalScrollView){
                    if (!child2.canScroll(ev)) {
                        onTouchEvent(ev)
                        return true
                    }
                }else if (child2CanScroll(child2, ev)) return true
            }
        }
        return false
    }

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }
}