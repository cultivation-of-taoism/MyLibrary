package com.hk.library.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.HorizontalScrollView
import com.blankj.utilcode.util.LogUtils
import kotlin.math.abs

class MyHorizontalScrollView : HorizontalScrollView {
    private var lastX = 0f
    private var lastY = 0f
    var isLeft:Boolean = false
    var isRight:Boolean = false
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)


    /**
     * 当滑动到边界时应该交由父view处理
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (!canScroll(ev))return false
        return super.dispatchTouchEvent(ev)
    }

    fun canScroll(ev: MotionEvent): Boolean{
        isLeft = !canScrollHorizontally(-1)
        isRight = !canScrollHorizontally(1)
        when(ev.action){
            MotionEvent.ACTION_DOWN-> {
                lastX = ev.x
                lastY = ev.y
            }
            MotionEvent.ACTION_MOVE,MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->{
                val distanceX = ev.x - lastX
                val distanceY = ev.y - lastY
                if (abs(distanceX) > abs(distanceY)) {//如果是横向滑动
                    if (isRight && distanceX < 0) {
                        return false//自己不处理 也不向下传递
                    }
                    if (isLeft && distanceX > 0) {
                        return false
                    }

                }

            }
        }
        return true
    }








}